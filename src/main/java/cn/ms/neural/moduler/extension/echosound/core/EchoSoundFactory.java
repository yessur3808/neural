package cn.ms.neural.moduler.extension.echosound.core;

import cn.ms.neural.common.exception.EchoSoundException;
import cn.ms.neural.moduler.Conf;
//github.com/yu120/neural
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.extension.echosound.IEcho;
import cn.ms.neural.moduler.extension.echosound.IEchoSound;
import cn.ms.neural.moduler.extension.echosound.conf.EchoSoundConf;
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
	/**
	 * 开关
	 */
	boolean echoSoundSwitch=true;

	@Override
	public void notify(Moduler<REQ, RES> moduler) {
		this.moduler=moduler;

		echoSoundSwitch=this.moduler.getUrl().getModulerParameter(Conf.ECHOSOUND, EchoSoundConf.SWITCH_KEY, EchoSoundConf.SWITCH_DEF_VAL);
	}

	@Override
	public void init() throws Throwable {
		
	}

	@Override
	public RES echosound(EchoSoundType echoSoundType, REQ req, IEchoSoundProcessor<REQ, RES> processor, IEcho<REQ, RES> echo, Object... args) throws EchoSoundException {
		if(!echoSoundSwitch){//开关校验
			return processor.echosound(req, args);
		}
		
		switch (echoSoundType) {
		case REQ:
			REQ echoREQ=echo.$echo(req, args);//模拟请求
			return processor.echosound(echoREQ, args);
		case RES:
			return echo.$rebound(req, args);//模拟响应
		default:
			return processor.echosound(req, args);//费回声探测
		}
	}
	
	@Override
	public void destory() throws Throwable {
		
	}

}
