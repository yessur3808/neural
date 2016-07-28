package cn.ms.neural.moduler.extension.echosound.core;

import cn.ms.neural.moduler.extension.echosound.IEchoSound;
import cn.ms.neural.moduler.extension.echosound.conf.EchoSoundConf;
import cn.ms.neural.moduler.extension.echosound.processor.IEchoSoundProcessor;

/**
 * 回声探测
 * 
 * @author lry
 *
 * @param <REQ>
 * @param <RES>
 */
public class EchoSoundFactory<REQ, RES> implements IEchoSound<REQ, RES> {

	/**
	 * 发起探测
	 */
	@Override
	public Object $echo(Object req) throws Throwable {
		
		return null;
	}
	
	/**
	 * 响应探测
	 */
	@Override
	public Object rebound(EchoSoundConf echoSoundConf, IEchoSoundProcessor<REQ, RES> echoSoundHandler, REQ echoSoundREQ) throws Throwable {
		if(echoSoundConf.isEchoSound()){
			return echoSoundREQ;
		}
		return echoSoundHandler.handler(echoSoundREQ);
	}

}
