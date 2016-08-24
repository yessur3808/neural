package cn.ms.neural.moduler.extension.flowrate.core;

import java.util.concurrent.Semaphore;

import com.google.common.util.concurrent.RateLimiter;

import cn.ms.neural.Conf;
import cn.ms.neural.common.exception.flowrate.CctOverFlowException;
import cn.ms.neural.common.exception.flowrate.FlowrateException;
import cn.ms.neural.common.exception.flowrate.QpsOverFlowException;
import cn.ms.neural.common.logger.ILogger;
import cn.ms.neural.common.logger.LoggerManager;
import cn.ms.neural.common.spi.Adaptive;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.extension.flowrate.IFlowRate;
import cn.ms.neural.moduler.extension.flowrate.conf.FlowRateConf;
import cn.ms.neural.moduler.extension.flowrate.entity.FlowrateRule;
import cn.ms.neural.moduler.extension.flowrate.processor.IFlowRateProcessor;
import cn.ms.neural.moduler.senior.alarm.AlarmType;

/**
 * 流控中心
 * 
 * @author lry
 * @version v1.0
 */
@Adaptive
public class FlowRateFactory<REQ, RES> implements IFlowRate<REQ, RES> {

	private static final ILogger bizDefaultLog = LoggerManager.getBizDefaultLog();
	
	private Moduler<REQ, RES> moduler;

	//$NON-NLS-并发流控$
	private Semaphore semaphore=new Semaphore(FlowRateConf.CCT_NUM_DEF_VAL, true);
	//$NON-NLS-QPS流控$
	private RateLimiter rateLimiter=RateLimiter.create(FlowRateConf.QPS_NUM_DEF_VAL);
	private FlowrateRule flowrateRule;
	
	@Override
	public void notify(Moduler<REQ, RES> moduler) {
		this.moduler=moduler;
		
		//流控总开关
		boolean flowrateSwitch = this.moduler.getUrl().getModulerParameter(Conf.FLOWRATE, FlowRateConf.FLOWRATE_SWITCH_KEY, FlowRateConf.FLOWRATE_SWITCH_DEF_VAL);
		
		//$NON-NLS-并发数$
		boolean cctSwitch = this.moduler.getUrl().getModulerParameter(Conf.FLOWRATE, FlowRateConf.CCT_SWITCH_KEY, FlowRateConf.CCT_SWITCH_DEF_VAL);
		int permits = this.moduler.getUrl().getModulerParameter(Conf.FLOWRATE, FlowRateConf.CCT_NUM_KEY, FlowRateConf.CCT_NUM_DEF_VAL);
		
		//$NON-NLS-速率大小$
		boolean qpsSwitch = this.moduler.getUrl().getModulerParameter(Conf.FLOWRATE, FlowRateConf.QPS_SWITCH_KEY, FlowRateConf.QPS_SWITCH_DEF_VAL);
		double permitsPerSecond = this.moduler.getUrl().getModulerParameter(Conf.FLOWRATE, FlowRateConf.QPS_NUM_KEY, FlowRateConf.QPS_NUM_DEF_VAL);
		
		//$NON-NLS-配置更新$
		flowrateRule=new FlowrateRule(flowrateSwitch, cctSwitch, permits, qpsSwitch, permitsPerSecond);
		semaphore=new Semaphore(flowrateRule.getPermits(), true);
		rateLimiter.setRate(flowrateRule.getPermitsPerSecond());
	}

	@Override
	public void init() throws Throwable {
		
	}

	@Override
	public RES flowrate(REQ req, IFlowRateProcessor<REQ, RES> processor, Object... args) throws FlowrateException {
		if (!flowrateRule.isFlowrateSwitch()) {// 流控总开关
			return processor.processor(req, args);
		}
		
		if(flowrateRule.isCctSwitch()){//并发开关
			boolean hasRES=false;
			try {
				hasRES=semaphore.tryAcquire();//尝试获取许可数
				bizDefaultLog.info("当前的并发数为:"+(flowrateRule.getPermits()-semaphore.availablePermits()));
				if (hasRES) {//并发控制
					return rateLimiter(req, processor, args);	
				}else{//并发溢出
					//$NON-NLS-告警$
					processor.alarm(AlarmType.FLOWRATE_CTT_OVERFLOW, req, null, null, args);
					throw new CctOverFlowException("The concurrent overflow.");
				}
			} finally {
				if(hasRES){//获取成功才释放
					semaphore.release();//释放资源
				}
			}
		}else{
			return rateLimiter(req, processor, args);
		}
	}
	
	/**
	 * 流速控制
	 * 
	 * @param req
	 * @param processor
	 * @param args
	 * @return
	 */
	private RES rateLimiter(REQ req, IFlowRateProcessor<REQ, RES> processor, Object... args) {
		if(flowrateRule.isQpsSwitch()){//流控开关打开
			if(rateLimiter.tryAcquire()){//流速控制
				return processor.processor(req, args);
			}else{
				//$NON-NLS-告警$
				processor.alarm(AlarmType.FLOWRATE_QPS_OVERFLOW, req, null, null, args);
			    throw new QpsOverFlowException("The flow rate overflow.");
			}
		}else{//流控开关关闭
			return processor.processor(req, args);
		}
	}

	@Override
	public void destory() throws Throwable {
		
	}
	
}
