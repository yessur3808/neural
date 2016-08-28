package cn.ms.neural.chain.moduler;

import java.util.Map;

import cn.ms.neural.chain.INeuralChain;
import cn.ms.neural.chain.support.AbstractNeuralChain;
import cn.ms.neural.common.exception.AlarmException;
import cn.ms.neural.common.exception.ProcessorException;
import cn.ms.neural.common.exception.neure.NeureBreathException;
import cn.ms.neural.common.exception.neure.NeureCallbackException;
import cn.ms.neural.common.exception.neure.NeureFaultTolerantException;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.ModulerType;
import cn.ms.neural.moduler.extension.echosound.type.EchoSoundType;
import cn.ms.neural.moduler.neure.processor.INeureProcessor;
import cn.ms.neural.moduler.senior.alarm.IAlarmType;
import cn.ms.neural.processor.INeuralProcessor;

public class NeureChain<REQ, RES> extends AbstractNeuralChain<REQ, RES> implements INeuralChain<REQ, RES> {

	public NeureChain(Moduler<REQ, RES> moduler) {
		super(moduler);
	}

	@Override
	public RES chain(REQ req, final String neuralId, final EchoSoundType echoSoundType, final Map<String, Object> blackWhiteIdKeyVals,
			final INeuralProcessor<REQ, RES> processor, Object... args) {
		//$NON-NLS-容错内核开始(熔断拒绝→超时控制→舱壁隔离→服务容错→慢性尝试)$
		return moduler.getNeure().neure(req, new INeureProcessor<REQ, RES>() {
			@Override
			public RES processor(REQ req, Object...args) throws ProcessorException {//内核业务封装
				return processor.processor(req, args);
			}
			/**
			 * 内核容错
			 */
			@Override
			public RES faulttolerant(REQ req, Object...args) throws NeureFaultTolerantException{
				return processor.faulttolerant(req, args);
			}
			/**
			 * 内核呼吸
			 */
			@Override
			public long breath(long nowTimes, long nowExpend, long maxRetryNum, Object...args) throws NeureBreathException {
				return processor.breath(nowTimes, nowExpend, maxRetryNum, args);
			}
			/**
			 * 内核回调
			 */
			@Override
			public void callback(RES res, Object...args) throws NeureCallbackException {
				processor.callback(res, args);
			}
			/**
			 * 告警
			 */
			@Override
			public void alarm(IAlarmType alarmType, REQ req, RES res,
					Throwable t, Object... args) throws AlarmException {
				processor.alarm(ModulerType.Neure, alarmType, req, res, t, args);																		
			}
		}, args);//$NON-NLS-容错内核结束$
	}
}