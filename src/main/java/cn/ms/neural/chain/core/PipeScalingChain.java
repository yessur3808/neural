package cn.ms.neural.chain.core;

import java.util.Map;

import cn.ms.neural.chain.INeuralChain;
import cn.ms.neural.chain.support.AbstractNeuralChain;
import cn.ms.neural.common.exception.AlarmException;
import cn.ms.neural.common.exception.ProcessorException;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.ModulerType;
import cn.ms.neural.moduler.extension.echosound.type.EchoSoundType;
import cn.ms.neural.moduler.extension.pipescaling.processor.IPipeScalingProcessor;
import cn.ms.neural.moduler.senior.alarm.IAlarmType;
import cn.ms.neural.processor.INeuralProcessor;

public class PipeScalingChain<REQ, RES> extends AbstractNeuralChain<REQ, RES> implements INeuralChain<REQ, RES> {

	public PipeScalingChain(Moduler<REQ, RES> moduler) {
		super(moduler);
	}

	@Override
	public RES chain(REQ req, final String neuralId, final EchoSoundType echoSoundType, final Map<String, Object> blackWhiteIdKeyVals,
			final INeuralProcessor<REQ, RES> processor, Object... args) {
		//$NON-NLS-管道缩放开始$
		return moduler.getPipeScaling().pipescaling(req, new IPipeScalingProcessor<REQ, RES>() {
			@Override
			public RES processor(REQ req, Object... args) throws ProcessorException {
				return getNeuralChain().chain(req, neuralId, echoSoundType, blackWhiteIdKeyVals, processor, args);
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