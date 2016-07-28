package cn.ms.neural.moduler.extension.echosound;

import cn.ms.neural.moduler.extension.echosound.conf.EchoSoundConf;
import cn.ms.neural.moduler.extension.echosound.processor.IEchoSoundProcessor;

/**
 * 回声探测
 * <br>
 * 1.回声测试用于检测服务是否可用，回声测试按照正常请求流程执行，能够测试整个调用是否通畅，可用于监控。
 * 2.所有服务自动实现EchoService接口，只需将任意服务引用强制转型为EchoService，即可使用。
 * @author lry
 */
public interface IEchoSound<REQ, RES> {

	Object $echo(Object req) throws Throwable;
	
	/**
	 * 探测反弹
	 * 
	 * @param echoSoundConf
	 * @param echoSoundHandler
	 * @param echoSoundREQ
	 * @return
	 * @throws Throwable
	 */
	Object rebound(EchoSoundConf echoSoundConf, IEchoSoundProcessor<REQ, RES> echoSoundHandler, REQ echoSoundREQ) throws Throwable;
	
}
