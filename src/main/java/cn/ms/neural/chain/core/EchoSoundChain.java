package cn.ms.neural.chain.core;

import java.util.Map;

import cn.ms.neural.chain.support.AbstractNeuralChain;
import cn.ms.neural.common.exception.AlarmException;
import cn.ms.neural.common.exception.ProcessorException;
import cn.ms.neural.common.exception.echosound.EchoSoundException;
import cn.ms.neural.common.spi.SPI;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.ModulerType;
import cn.ms.neural.moduler.extension.echosound.processor.IEchoSoundProcessor;
import cn.ms.neural.moduler.extension.echosound.type.EchoSoundType;
import cn.ms.neural.moduler.senior.alarm.IAlarmType;
import cn.ms.neural.processor.INeuralProcessor;

/**
 * 回声探测调用链
 * 
 * @author lry
 *
 * @param <REQ>
 * @param <RES>
 */
@SPI(order=7)
public class EchoSoundChain<REQ, RES> extends AbstractNeuralChain<REQ, RES> {

	public EchoSoundChain(Moduler<REQ, RES> moduler) {
		super(moduler);
	}

	@Override
	public RES chain(REQ req, final String neuralId, final EchoSoundType echoSoundType, final Map<String, Object> blackWhiteIdKeyVals,
			final INeuralProcessor<REQ, RES> processor, Object... args) {
		
		// $NON-NLS-回声探测开始$
		return moduler.getEchoSound().echosound(echoSoundType, req, new IEchoSoundProcessor<REQ, RES>() {
			
			@Override
			public RES processor(REQ req, Object... args) throws ProcessorException {
				return neuralChain.chain(req, neuralId, echoSoundType, blackWhiteIdKeyVals, processor, args);
			}
			
			/**
			 * 回声探测请求
			 */
			@Override
			public REQ $echo(REQ req, Object...args) throws EchoSoundException {
				return processor.$echo(req, args);
			}
			
			/**
			 * 回声探测响应
			 */
			@Override
			public RES $rebound(REQ req, Object...args) throws EchoSoundException {
				return processor.$rebound(req, args);
			}
			
			/**
			 * 告警
			 */
			@Override
			public void alarm(IAlarmType alarmType, REQ req, RES res, Throwable t, Object... args) throws AlarmException {
				processor.alarm(ModulerType.EchoSound, alarmType, req, res, t, args);																
			}
		});
		
	}
	
}