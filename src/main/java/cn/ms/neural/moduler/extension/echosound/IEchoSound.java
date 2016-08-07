package cn.ms.neural.moduler.extension.echosound;

import cn.ms.neural.common.exception.EchoSoundException;
import cn.ms.neural.moduler.IModuler;
import cn.ms.neural.moduler.extension.echosound.processor.IEchoSoundProcessor;
import cn.ms.neural.moduler.extension.echosound.type.EchoSoundType;

/**
 * 回声探测
 * <br>
 * 1.回声测试用于检测服务是否可用，回声测试按照正常请求流程执行，能够测试整个调用是否通畅，可用于监控。
 * 2.所有服务自动实现EchoService接口，只需将任意服务引用强制转型为EchoService，即可使用。
 * @author lry
 */
public interface IEchoSound<REQ, RES> extends IModuler<REQ, RES> {

	RES echosound(EchoSoundType echoSoundType, REQ req, IEchoSoundProcessor<REQ, RES> processor, Object...args) throws EchoSoundException;
	
}
