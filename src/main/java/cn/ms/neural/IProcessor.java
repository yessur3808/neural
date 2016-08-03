package cn.ms.neural;

import cn.ms.neural.common.exception.ProcessorException;

/**
 * The Moduler Processor.
 * 
 * @author lry
 * @version v1.0
 */
public interface IProcessor<REQ, RES> {

	RES processor(REQ req, Object... args) throws ProcessorException;
	
}
