package cn.ms.neural;

import cn.ms.neural.common.exception.ProcessorException;

public interface IProcessor<REQ, RES> {

	RES processor(REQ req, Object... args) throws ProcessorException;
	
}
