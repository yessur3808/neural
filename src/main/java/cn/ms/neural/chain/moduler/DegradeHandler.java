package cn.ms.neural.chain.moduler;

import java.util.Map;

import cn.ms.neural.chain.NeuralChainHandler;
import cn.ms.neural.chain.support.AbstractNeuralChainHandler;
import cn.ms.neural.common.exception.AlarmException;
import cn.ms.neural.common.exception.ProcessorException;
import cn.ms.neural.common.exception.degrade.DegradeException;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.ModulerType;
import cn.ms.neural.moduler.extension.degrade.processor.IDegradeProcessor;
import cn.ms.neural.moduler.extension.echosound.type.EchoSoundType;
import cn.ms.neural.moduler.senior.alarm.IAlarmType;
import cn.ms.neural.processor.INeuralProcessor;

public class DegradeHandler<REQ, RES> extends AbstractNeuralChainHandler<REQ, RES> implements NeuralChainHandler<REQ, RES> {

	public DegradeHandler(Moduler<REQ, RES> moduler) {
		super(moduler);
	}

	@Override
	public RES chain(REQ req, final String neuralId, final EchoSoundType echoSoundType, final Map<String, Object> blackWhiteIdKeyVals,
			final INeuralProcessor<REQ, RES> processor, Object... args) {
		//$NON-NLS-服务降级开始$
		return moduler.getDegrade().degrade(req, new IDegradeProcessor<REQ, RES>() {
			@Override
			public RES processor(REQ req, Object... args) throws ProcessorException {
				return getNeuralChainHandler().chain(req, neuralId, echoSoundType, blackWhiteIdKeyVals, processor, args);
			}
			/**
			 * 降级mock
			 */
			@Override
			public RES mock(REQ req, Object...args) throws DegradeException {
				return processor.mock(req, args);
			}
			/**
			 * 业务降级
			 */
			@Override
			public RES bizDegrade(REQ req, Object...args) throws DegradeException {
				return processor.bizDegrade(req, args);
			}
			/**
			 * 告警
			 */
			@Override
			public void alarm(IAlarmType alarmType,REQ req, RES res, Throwable t, Object... args) throws AlarmException {
				processor.alarm(ModulerType.Degrade, alarmType, req, res, t, args);												
			}
		});
	}
}