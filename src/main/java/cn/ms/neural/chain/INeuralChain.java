package cn.ms.neural.chain;

import java.util.Map;

import cn.ms.neural.moduler.extension.echosound.type.EchoSoundType;
import cn.ms.neural.processor.INeuralProcessor;

public interface INeuralChain<REQ, RES> {

	RES chain(REQ req, 
			String neuralId, 
			EchoSoundType echoSoundType, 
			Map<String, Object> blackWhiteIdKeyVals,
			INeuralProcessor<REQ, RES> processor, 
			Object... args);

}
