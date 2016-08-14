package cn.ms.neural.moduler.extension.idempotent.processor;

import cn.ms.neural.common.exception.idempotent.IdempotentException;
import cn.ms.neural.moduler.senior.alarm.IAlarmType;
import cn.ms.neural.processor.IProcessor;

/**
 * 幂等处理
 * 
 * @author lry
 * @version v1.0
 */
public interface IdempotentProcessor<REQ,RES> extends IProcessor<REQ, RES>, IAlarmType<REQ, RES> {

	/**
	 * 幂等请求校验
	 * 
	 * @param neuralId
	 * @param args
	 * @return
	 * @throws IdempotentException
	 */
	boolean check(String neuralId, Object...args) throws IdempotentException;
	
	/**
	 * 获取幂等数据
	 * 
	 * @param neuralId
	 * @param args
	 * @return
	 * @throws IdempotentException
	 */
	RES get(String neuralId, Object...args) throws IdempotentException;
	
	/**
	 * 幂等结果持久化
	 * 
	 * @param req
	 * @param res
	 * @param args
	 * @throws IdempotentException
	 */
	void storage(REQ req, RES res, Object...args) throws IdempotentException;
	
}
