package cn.ms.neural.chain.core;

import java.util.Map;

import cn.ms.neural.chain.support.AbstractNeuralChain;
import cn.ms.neural.common.exception.AlarmException;
import cn.ms.neural.common.exception.ProcessorException;
import cn.ms.neural.common.exception.idempotent.IdempotentException;
import cn.ms.neural.common.spi.SPI;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.ModulerType;
import cn.ms.neural.moduler.extension.echosound.type.EchoSoundType;
import cn.ms.neural.moduler.extension.idempotent.processor.IdempotentProcessor;
import cn.ms.neural.moduler.senior.alarm.IAlarmType;
import cn.ms.neural.processor.INeuralProcessor;

/**
 * 幂等调用链
 * 
 * @author lry
 *
 * @param <REQ>
 * @param <RES>
 */
@SPI(order=6)
public class IdempotentChain<REQ, RES> extends AbstractNeuralChain<REQ, RES> {

	public IdempotentChain(Moduler<REQ, RES> moduler) {
		super(moduler);
	}

	@Override
	public RES chain(REQ req, final String neuralId, final EchoSoundType echoSoundType,
			final Map<String, Object> blackWhiteIdKeyVals, final INeuralProcessor<REQ, RES> processor, Object... args) {

		// $NON-NLS-幂等开始$
		return moduler.getIdempotent().idempotent(neuralId, req, new IdempotentProcessor<REQ, RES>() {

			@Override
			public RES processor(REQ req, Object... args) throws ProcessorException {
				return neuralChain.chain(req, neuralId, echoSoundType, blackWhiteIdKeyVals, processor, args);
			}

			/**
			 * 幂等请求校验
			 */
			@Override
			public boolean check(String neuralId, Object... args) throws IdempotentException {
				return processor.check(neuralId);
			}

			/**
			 * 获取幂等数据
			 */
			@Override
			public RES get(String neuralId, Object... args) throws IdempotentException {
				return processor.get(neuralId);
			}

			/**
			 * 幂等持久化数据
			 */
			@Override
			public void storage(REQ req, RES res, Object... args) throws IdempotentException {
				processor.storage(req, res, args);
			}

			/**
			 * 告警
			 */
			@Override
			public void alarm(IAlarmType alarmType, REQ req, RES res, Throwable t, Object... args)
					throws AlarmException {
				processor.alarm(ModulerType.Idempotent, alarmType, req, res, t, args);
			}
		});

	}

}