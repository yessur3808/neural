package cn.ms.neural.processor;

import cn.ms.neural.common.exception.ProcessorException;

/**
 * The Moduler Processor.
 * 
 * @author lry
 * @version v1.0
 */
public interface IProcessor<REQ, RES> {

	/**
	 * The processor center
	 * 
	 * @param req
	 * @param args
	 * @return
	 * @throws ProcessorException
	 */
	RES processor(REQ req, Object... args) throws ProcessorException;
	
}
