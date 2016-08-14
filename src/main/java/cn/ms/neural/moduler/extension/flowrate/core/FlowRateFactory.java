package cn.ms.neural.moduler.extension.flowrate.core;

import java.util.concurrent.Semaphore;

import com.google.common.util.concurrent.RateLimiter;

import cn.ms.neural.Conf;
import cn.ms.neural.common.exception.flowrate.CctException;
import cn.ms.neural.common.exception.flowrate.CctOverFlowException;
import cn.ms.neural.common.exception.flowrate.FlowrateException;
import cn.ms.neural.common.exception.flowrate.QpsException;
import cn.ms.neural.common.exception.flowrate.QpsOverFlowException;
import cn.ms.neural.common.spi.Adaptive;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.extension.flowrate.IFlowRate;
import cn.ms.neural.moduler.extension.flowrate.conf.FlowRateConf;
import cn.ms.neural.moduler.extension.flowrate.processor.IFlowRateProcessor;

/**
 * 流控中心
 * 
 * @author lry
 * @version v1.0
 */
@Adaptive
public class FlowRateFactory<REQ, RES> implements IFlowRate<REQ, RES> {

	private Moduler<REQ, RES> moduler;

	private boolean flowrateSwitch=false;
	
	//$NON-NLS-并发流控$
	private Semaphore semaphore;
	private boolean cctSwitch=false;
	private int permits=FlowRateConf.CCT_NUM_DEF_VAL;
	
	//$NON-NLS-QPS流控$
	private RateLimiter rateLimiter;
	private boolean qpsSwitch=false;
	private double permitsPerSecond=FlowRateConf.QPS_NUM_DEF_VAL;
	
	@Override
	public void notify(Moduler<REQ, RES> moduler) {
		this.moduler=moduler;
		
		//流控总开关
		flowrateSwitch = this.moduler.getUrl().getModulerParameter(Conf.FLOWRATE, FlowRateConf.FLOWRATE_SWITCH_KEY, FlowRateConf.FLOWRATE_SWITCH_DEF_VAL);
		
		//$NON-NLS-并发数$
		cctSwitch = this.moduler.getUrl().getModulerParameter(Conf.FLOWRATE, FlowRateConf.CCT_SWITCH_KEY, FlowRateConf.CCT_SWITCH_DEF_VAL);
		permits = this.moduler.getUrl().getModulerParameter(Conf.FLOWRATE, FlowRateConf.CCT_NUM_KEY, FlowRateConf.CCT_NUM_DEF_VAL);
		semaphore=new Semaphore(permits, true);
		
		//$NON-NLS-速率大小$
		qpsSwitch = this.moduler.getUrl().getModulerParameter(Conf.FLOWRATE, FlowRateConf.QPS_SWITCH_KEY, FlowRateConf.QPS_SWITCH_DEF_VAL);
		permitsPerSecond = this.moduler.getUrl().getModulerParameter(Conf.FLOWRATE, FlowRateConf.QPS_NUM_KEY, FlowRateConf.QPS_NUM_DEF_VAL);
		rateLimiter=RateLimiter.create(permitsPerSecond);
	}

	@Override
	public void init() throws Throwable {
		
	}

	/**
	 * 流控
	 */
	@Override
	public RES flowrate(REQ req, IFlowRateProcessor<REQ, RES> processor, Object... args) throws FlowrateException {
		if (!flowrateSwitch) {// 流控总开关
			return processor.processor(req, args);
		}
		
		if(cctSwitch){//并发开关
			try {
				if (semaphore.tryAcquire()) {//并发控制
					return rateLimiter(req, processor, args);	
				}else{//并发溢出
					throw new CctOverFlowException("并发溢出");
				}
			} catch (Exception e) {
				throw new CctException(e);//并发异常
			} finally {
				semaphore.release();//释放资源
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
		if(qpsSwitch){//流控开关打开
			try {
				if(rateLimiter.tryAcquire()){//流速控制
					return processor.processor(req, args);
				}else{
				    throw new QpsOverFlowException("流速溢出");
				}
			} catch (Exception e) {
				throw new QpsException();
			}
		}else{//流控开关关闭
			return processor.processor(req, args);
		}
	}

	@Override
	public void destory() throws Throwable {
		
	}
	
}
