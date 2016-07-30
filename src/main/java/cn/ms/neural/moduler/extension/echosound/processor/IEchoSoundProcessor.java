package cn.ms.neural.moduler.extension.echosound.processor;

import cn.ms.neural.common.exception.EchoSoundException;

public interface IEchoSoundProcessor<REQ, RES> {

	RES echosound(REQ req, IEchoSoundProcessor<REQ, RES> processor, Object...args) throws EchoSoundException;
	
}
