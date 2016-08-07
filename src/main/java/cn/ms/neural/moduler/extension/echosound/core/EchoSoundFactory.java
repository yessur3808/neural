package cn.ms.neural.moduler.extension.echosound.core;

import cn.ms.neural.Conf;
import cn.ms.neural.common.exception.echosound.EchoSoundException;
import cn.ms.neural.common.spi.Adaptive;
//github.com/yu120/neural
import cn.ms.neural.moduler.Moduler;
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
@Adaptive
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
	public RES echosound(EchoSoundType echoSoundType, REQ req, IEchoSoundProcessor<REQ, RES> processor, Object... args) throws EchoSoundException {
		if(!echoSoundSwitch){//开关校验
			return processor.processor(req, args);
		}
		
		switch (echoSoundType) {
		case REQ:
			REQ echoREQ=processor.$echo(req, args);//模拟请求
			return processor.processor(echoREQ, args);
		case RES:
			return processor.$rebound(req, args);//模拟响应
		default:
			return processor.processor(req, args);//费回声探测
		}
	}
	
	@Override
	public void destory() throws Throwable {
		
	}

}
