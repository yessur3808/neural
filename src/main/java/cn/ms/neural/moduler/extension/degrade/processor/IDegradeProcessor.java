package cn.ms.neural.moduler.extension.degrade.processor;

import cn.ms.neural.common.exception.degrade.DegradeException;
import cn.ms.neural.processor.IProcessor;

/**
 * 服务降级处理器
 * 
 * @author lry
 *
 * @param <REQ>
 * @param <RES>
 */
public interface IDegradeProcessor<REQ, RES> extends IProcessor<REQ, RES> {

	/**
	 * Mock降级
	 * 
	 * @param req
	 * @param args
	 * @return
	 * @throws DegradeException
	 */
	RES mock(REQ req, Object...args) throws DegradeException;
	
	/**
	 * 业务降级处理器
	 * 
	 * @param req
	 * @param args
	 * @return
	 * @throws Throwable
	 */
	RES bizDegrade(REQ req, Object... args) throws DegradeException;

}
