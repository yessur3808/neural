package cn.ms.neural.chain.core;

import java.util.Map;

import cn.ms.neural.chain.support.AbstractNeuralChain;
import cn.ms.neural.common.exception.AlarmException;
import cn.ms.neural.common.exception.ProcessorException;
import cn.ms.neural.common.spi.SPI;
import cn.ms.neural.moduler.ModulerType;
import cn.ms.neural.moduler.extension.blackwhite.processor.IBlackWhiteProcessor;
import cn.ms.neural.moduler.extension.echosound.type.EchoSoundType;
import cn.ms.neural.moduler.senior.alarm.IAlarmType;
import cn.ms.neural.processor.INeuralProcessor;

/**
 * 黑白名单调用链
 * 
 * @author lry
 *
 * @param <REQ>
 * @param <RES>
 */
@SPI(order=2)
public class BlackWhiteChain<REQ, RES> extends AbstractNeuralChain<REQ, RES> {

	@Override
	public RES chain(REQ req, final String neuralId, final EchoSoundType echoSoundType,
			final Map<String, Object> blackWhiteIdKeyVals, final INeuralProcessor<REQ, RES> processor, Object... args) {

		// $NON-NLS-黑白名单开始$
		return moduler.getBlackWhite().blackwhite(req, blackWhiteIdKeyVals, new IBlackWhiteProcessor<REQ, RES>() {

			@Override
			public RES processor(REQ req, Object... args) throws ProcessorException {
				return neuralChain.chain(req, neuralId, echoSoundType, blackWhiteIdKeyVals, processor, args);
			}

			/**
			 * 告警
			 */
			@Override
			public void alarm(IAlarmType alarmType, REQ req, RES res, Throwable t, Object... args)
					throws AlarmException {
				processor.alarm(ModulerType.BlackWhite, alarmType, req, res, t, args);
			}

		});

	}

}