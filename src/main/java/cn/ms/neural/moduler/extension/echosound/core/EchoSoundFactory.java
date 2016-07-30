package cn.ms.neural.moduler.extension.echosound.core;

import cn.ms.neural.common.exception.EchoSoundException;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.extension.echosound.IEcho;
import cn.ms.neural.moduler.extension.echosound.IEchoSound;
import cn.ms.neural.moduler.extension.echosound.processor.IEchoSoundProcessor;
import cn.ms.neural.moduler.extension.echosound.type.EchoSoundType;

/**
 * 回声探测
 * 
 * @author lry
 *
 * @param <REQ>
 * @param <RES>
 */
public class EchoSoundFactory<REQ, RES> implements IEchoSound<REQ, RES> {

	Moduler<REQ, RES> moduler;

	@Override
	public void notify(Moduler<REQ, RES> moduler) {
		this.moduler=moduler;
	}

	@Override
	public void init() throws Throwable {
		
	}

	@Override
	public RES echosound(EchoSoundType echoSoundType, REQ req, IEchoSoundProcessor<REQ, RES> processor, IEcho<REQ, RES> echo, Object... args) throws EchoSoundException {
		switch (echoSoundType) {
		case REQ:
			REQ echoREQ=echo.$echo(args);//模拟请求
			return processor.echosound(echoREQ, processor, args);
		case RES:
			return echo.$rebound(args);//模拟响应
		default:
			return processor.echosound(req, processor, args);//费回声探测
		}
	}
	
	@Override
	public void destory() throws Throwable {
		
	}

}
