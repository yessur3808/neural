package cn.ms.neural.moduler.extension.degrade.processor;

import cn.ms.neural.IProcessor;
import cn.ms.neural.common.exception.degrade.DegradeException;

/**
 * 服务降级处理器
 * 
 * @author lry
 *
 * @param <REQ>
 * @param <RES>
 */
public interface IDegradeProcessor<REQ, RES> extends IProcessor<REQ, RES> {

	RES mock(REQ req, Object...args) throws DegradeException;
	
	/**
	 * 业务降级处理器
	 * 
	 * @param req
	 * @param args
	 * @return
	 * @throws Throwable
	 */
	RES bizProcessor(REQ req, Object... args) throws DegradeException;

}
