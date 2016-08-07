package cn.ms.neural.processor;

import cn.ms.neural.moduler.extension.blackwhite.processor.IBlackWhiteProcessor;
import cn.ms.neural.moduler.extension.degrade.processor.IDegradeProcessor;
import cn.ms.neural.moduler.extension.echosound.processor.IEchoSoundProcessor;
import cn.ms.neural.moduler.extension.gracestop.processor.IGraceStopProcessor;
import cn.ms.neural.moduler.extension.idempotent.processor.IdempotentProcessor;
import cn.ms.neural.moduler.extension.pipescaling.processor.IPipeScalingProcessor;
import cn.ms.neural.moduler.neure.processor.INeureProcessor;

public interface INeuralProcessor<REQ, RES> extends 
	IProcessor<REQ, RES>, 
	IGraceStopProcessor<REQ, RES>,
	IBlackWhiteProcessor<REQ, RES>,
	IPipeScalingProcessor<REQ, RES>,
	IDegradeProcessor<REQ, RES>,
	IdempotentProcessor<REQ, RES>,
	IEchoSoundProcessor<REQ, RES>,
	INeureProcessor<REQ, RES>{

}
