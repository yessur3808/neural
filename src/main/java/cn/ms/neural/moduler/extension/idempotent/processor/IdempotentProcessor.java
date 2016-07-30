package cn.ms.neural.moduler.extension.idempotent.processor;

import cn.ms.neural.IProcessor;
import cn.ms.neural.common.exception.idempotent.IdempotentException;

public interface IdempotentProcessor<REQ,RES> extends IProcessor<REQ, RES> {

	/**
	 * 校验
	 * 
	 * @param idempotentKEY
	 * @return
	 * @throws IdempotentException
	 */
	boolean check(String idempotentKEY) throws IdempotentException;
	
	/**
	 * 获取幂等数据
	 * 
	 * @param idempotentKEY
	 * @return
	 * @throws IdempotentException
	 */
	RES get(String idempotentKEY) throws IdempotentException;
	
	/**
	 * 持久化
	 * 
	 * @param req
	 * @param res
	 * @param args
	 * @throws IdempotentException
	 */
	void storage(REQ req, RES res, Object...args) throws IdempotentException;
	
}
