package cn.ms.neural.moduler.extension.echosound.processor;

import cn.ms.neural.common.exception.EchoSoundException;

/**
 * 回声探测
 * @author lry
 * @version v1.0
 */
public interface IEchoSoundProcessor<REQ, RES> {

	RES echosound(REQ req, Object...args) throws EchoSoundException;
	
}
