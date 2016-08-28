package cn.ms.neural.chain;

import java.util.Map;

import cn.ms.neural.moduler.extension.echosound.type.EchoSoundType;
import cn.ms.neural.processor.INeuralProcessor;

public interface NeuralChainHandler<REQ, RES> {

	RES neural(REQ req, 
			String neuralId, 
			EchoSoundType echoSoundType, 
			Map<String, Object> blackWhiteIdKeyVals,
			INeuralProcessor<REQ, RES> processor, 
			Object... args);

}
