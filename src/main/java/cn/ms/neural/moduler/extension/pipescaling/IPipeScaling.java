package cn.ms.neural.moduler.extension.pipescaling;

import cn.ms.neural.common.exception.pipescaling.PipeScalingException;
import cn.ms.neural.moduler.IModuler;
import cn.ms.neural.moduler.extension.pipescaling.processor.IPipeScalingProcessor;

/**
 * 管道缩放
 * 
 * @author lry
 * @version v1.0
 */
public interface IPipeScaling<REQ, RES> extends IModuler<REQ, RES> {

	/**
	 * 
	 * @param req
	 * @param processor
	 * @param args
	 * @return
	 * @throws PipeScalingException
	 */
	RES pipescaling(REQ req, IPipeScalingProcessor<REQ, RES> processor, Object... args) throws PipeScalingException;
	
}
