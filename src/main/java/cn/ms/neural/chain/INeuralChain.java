package cn.ms.neural.chain;

import java.util.Map;

import cn.ms.neural.moduler.extension.echosound.type.EchoSoundType;
import cn.ms.neural.processor.INeuralProcessor;

/**
 * 微服务神经元调用链
 * 
 * @author lry
 *
 * @param <REQ>
 * @param <RES>
 */
public interface INeuralChain<REQ, RES> {

	/**
	 * 设置调用链
	 * 
	 * @param neuralChain
	 */
	void setNeuralChain(INeuralChain<REQ, RES> neuralChain);

	/**
	 * 调用链
	 * 
	 * @param req
	 * @param neuralId
	 * @param echoSoundType
	 * @param blackWhiteIdKeyVals
	 * @param processor
	 * @param args
	 * @return
	 */
	RES chain(REQ req, String neuralId, EchoSoundType echoSoundType, Map<String, Object> blackWhiteIdKeyVals,
			INeuralProcessor<REQ, RES> processor, Object... args);

}
