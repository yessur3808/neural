package cn.ms.neural.moduler.extension.degrade.processor;

import cn.ms.neural.common.exception.degrade.DegradeException;
import cn.ms.neural.moduler.senior.alarm.processor.IAlarmTypeProcessor;

/**
 * 业务服务降级
 * 
 * @author lry
 */
public interface IBizDegradeProcessor<REQ, RES> extends IAlarmTypeProcessor<REQ, RES> {

	/**
	 * 业务降级处理器
	 * 
	 * @param req
	 * @param processor
	 * @param args
	 * @return
	 * @throws Throwable
	 */
	RES processor(REQ req, IDegradeProcessor<REQ, RES> processor, Object... args) throws DegradeException;
	
}
