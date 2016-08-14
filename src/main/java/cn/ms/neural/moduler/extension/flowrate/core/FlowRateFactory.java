package cn.ms.neural.moduler.extension.flowrate.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.util.concurrent.RateLimiter;

import cn.ms.neural.Conf;
import cn.ms.neural.common.exception.flowrate.CCTRejectREQException;
import cn.ms.neural.common.exception.flowrate.FlowrateException;
import cn.ms.neural.common.spi.Adaptive;
import cn.ms.neural.common.utils.StringUtils;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.extension.flowrate.IFlowRate;
import cn.ms.neural.moduler.extension.flowrate.conf.FlowRateConf;
import cn.ms.neural.moduler.extension.flowrate.entity.FlowRateData;
import cn.ms.neural.moduler.extension.flowrate.entity.FlowRateEntity;
import cn.ms.neural.moduler.extension.flowrate.handler.FlowRateHandler;
import cn.ms.neural.moduler.extension.flowrate.processor.IFlowRateProcessor;
import cn.ms.neural.moduler.extension.flowrate.type.FlowRateType;

/**
 * 流控中心
 * 
 * @author lry
 * @version v1.0
 */
@Adaptive
public class FlowRateFactory<REQ, RES> implements IFlowRate<REQ, RES> {

	private static final Logger logger=LogManager.getLogger(FlowRateFactory.class);
	
	private Moduler<REQ, RES> moduler;
	/**
	 * 流控规则
	 * 数据结构:{[CCT/QPS]@[FRKEY],[Rule]}
	 */
	private final ConcurrentHashMap<String, FlowRateEntity> flowrateRuleMap = new ConcurrentHashMap<String, FlowRateEntity>();
	/**
	 * 并发流控
	 */
	private final ConcurrentHashMap<String, Semaphore> semaphoreFlowrateMap = new ConcurrentHashMap<String, Semaphore>();
	/**
	 * QPS流控
	 */
	private final ConcurrentHashMap<String, RateLimiter> rateLimiterFlowrateMap = new ConcurrentHashMap<String, RateLimiter>();
	private boolean flowrateSwitch=false;
	private boolean cctSwitch=false;
	private boolean qpsSwitch=false;
	
	@Override
	public void notify(Moduler<REQ, RES> moduler) {
		this.moduler=moduler;
		
		flowrateRuleMap.clear();
		semaphoreFlowrateMap.clear();
		rateLimiterFlowrateMap.clear();
		
		//流控总开关
		flowrateSwitch = this.moduler.getUrl().getModulerParameter(Conf.FLOWRATE, FlowRateConf.FLOWRATE_SWITCH_KEY, FlowRateConf.FLOWRATE_SWITCH_DEF_VAL);
		cctSwitch = this.moduler.getUrl().getModulerParameter(Conf.FLOWRATE, FlowRateConf.CCT_SWITCH_KEY, FlowRateConf.CCT_SWITCH_DEF_VAL);
		qpsSwitch = this.moduler.getUrl().getModulerParameter(Conf.FLOWRATE, FlowRateConf.QPS_SWITCH_KEY, FlowRateConf.QPS_SWITCH_DEF_VAL);
		
		Map<String,FlowRateEntity> freMap=FlowRateHandler.getFlowRateEntityMap(this.moduler.getUrl());
		if(!freMap.isEmpty()){
			flowrateRuleMap.putAll(freMap);	
		}
		for (Map.Entry<String, FlowRateEntity> entry:flowrateRuleMap.entrySet()) {
			FlowRateEntity entity=entry.getValue();
			if(FlowRateType.CCT.getVal().equals(entity.getFlowRateType().getVal())){//并发
				Semaphore semaphore=new Semaphore(entity.getPermits(), true);
				semaphoreFlowrateMap.put(entity.getFrkey(), semaphore);
			}else if(FlowRateType.QPS.getVal().equals(entity.getFlowRateType().getVal())){//QPS
				RateLimiter rateLimiter=RateLimiter.create(entity.getPermits());
				rateLimiterFlowrateMap.put(entity.getFrkey(), rateLimiter);
			}
		}
	}

	@Override
	public void init() throws Throwable {
		
	}

	/**
	 * 流控
	 */
	@Override
	public RES flowrate(REQ req, List<FlowRateData> flowRateDatas, IFlowRateProcessor<REQ, RES> processor, Object... args) throws FlowrateException {
		if (!flowrateSwitch) {// 流控总开关
			return processor.processor(req, args);
		}
		
		for (FlowRateData flowRateData:flowRateDatas) {
			if(flowRateData.getFlowRateType()==null||StringUtils.isBlank(
					flowRateData.getResType())||StringUtils.isBlank(flowRateData.getData())){
				continue;
			}
			
			FlowRateEntity entity=flowrateRuleMap.get(flowRateData.getFlowRateType().getVal()+Conf.ANT+flowRateData.getResType());
			if(FlowRateType.CCT.getVal().equals(flowRateData.getFlowRateType().getVal())){//并发
				if(cctSwitch){//并发子开关
					doCct(entity, req, processor, args);					
				}
			}else if(FlowRateType.QPS.getVal().equals(flowRateData.getFlowRateType().getVal())){//QPS
				if(qpsSwitch){//流速子开关
					//RateLimiter rateLimiter=rateLimiterFlowrateMap.get(entity.getFrkey());
				}
			}
		}
		
		return processor.processor(req, args);
	}

	/**
	 * 并发控制
	 * 
	 * @param entity
	 * @param req
	 * @param processor
	 * @param args
	 * @return
	 * @throws FlowrateException
	 */
	private RES doCct(FlowRateEntity entity,REQ req, IFlowRateProcessor<REQ, RES> processor, Object... args) throws FlowrateException {
		boolean check;
		Semaphore semaphore = null;
		try {
			semaphore=semaphoreFlowrateMap.get(entity.getFrkey());
			if(entity.isAcquire()){
				check=semaphore.tryAcquire();
			}else{
				check=semaphore.tryAcquire(entity.getTimeout(), TimeUnit.MILLISECONDS);
			}
			if(logger.isFatalEnabled()){
				logger.fatal("线程" + Thread.currentThread().getName() + "进入,当前已有" + (entity.getPermits()-semaphore.availablePermits()) + "个并发");
			}
			
			if(check){
				if(semaphore.availablePermits()>=0){
					return processor.processor(req, args);
				}else{
					throw new CCTRejectREQException("并发超额,拒绝请求");
				}
			}else{
				throw new CCTRejectREQException("并发拒绝请求");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(semaphore!=null){
				semaphore.release();
				if(logger.isFatalEnabled()){
					logger.fatal("线程" + Thread.currentThread().getName() + "已离开，当前有" + (entity.getPermits()-semaphore.availablePermits()) + "个并发");    
				}						
			}
		}
		
		return null;
		
	}

	@Override
	public void destory() throws Throwable {
		
	}
	
}
