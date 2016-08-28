package cn.ms.neural.chain.moduler;

import java.util.Map;

import cn.ms.neural.chain.NeuralChainHandler;
import cn.ms.neural.chain.support.AbstractNeuralChainHandler;
import cn.ms.neural.common.exception.AlarmException;
import cn.ms.neural.common.exception.ProcessorException;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.ModulerType;
import cn.ms.neural.moduler.extension.echosound.type.EchoSoundType;
import cn.ms.neural.moduler.extension.pipescaling.processor.IPipeScalingProcessor;
import cn.ms.neural.moduler.senior.alarm.IAlarmType;
import cn.ms.neural.processor.INeuralProcessor;

public class PipeScalingHandler<REQ, RES> extends AbstractNeuralChainHandler<REQ, RES> implements NeuralChainHandler<REQ, RES> {

	public PipeScalingHandler(Moduler<REQ, RES> moduler) {
		super(moduler);
	}

	@Override
	public RES neural(REQ req, final String neuralId, final EchoSoundType echoSoundType, final Map<String, Object> blackWhiteIdKeyVals,
			final INeuralProcessor<REQ, RES> processor, Object... args) {
		//$NON-NLS-管道缩放开始$
		return moduler.getPipeScaling().pipescaling(req, new IPipeScalingProcessor<REQ, RES>() {
			@Override
			public RES processor(REQ req, Object... args) throws ProcessorException {
				return getNeuralChainHandler().neural(req, neuralId, echoSoundType, blackWhiteIdKeyVals, processor, args);
			}
			/**
			 * 告警
			 */
			@Override
			public void alarm(IAlarmType alarmType, REQ req, RES res, Throwable t, Object... args) throws AlarmException {
				processor.alarm(ModulerType.PipeScaling, alarmType, req, res, t, args);
			}
		});
	}
}