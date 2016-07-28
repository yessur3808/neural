package cn.ms.neural.moduler.extension.pipescaling;

import cn.ms.neural.moduler.IModuler;
import cn.ms.neural.moduler.extension.pipescaling.processor.IPipeScalingProcessor;

/**
 * 管道缩放
 * 
 * @author lry
 * @version v1.0
 */
public interface IPipeScaling<REQ, RES> extends IModuler<REQ, RES> {

	RES pipescaling(REQ req, IPipeScalingProcessor<REQ, RES> processor, Object... args);
	
}
