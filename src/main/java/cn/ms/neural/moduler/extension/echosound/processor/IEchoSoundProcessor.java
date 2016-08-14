package cn.ms.neural.moduler.extension.echosound.processor;

import cn.ms.neural.common.exception.echosound.EchoSoundException;
import cn.ms.neural.moduler.senior.alarm.processor.IAlarmTypeProcessor;
import cn.ms.neural.processor.IProcessor;

/**
 * 回声探测
 * @author lry
 * @version v1.0
 */
public interface IEchoSoundProcessor<REQ, RES> extends IProcessor<REQ, RES>, IAlarmTypeProcessor<REQ, RES> {

	/**
	 * 发射探测
	 * 
	 * @param req
	 * @param args
	 * @return
	 * @throws EchoSoundException
	 */
	REQ $echo(REQ req, Object...args) throws EchoSoundException;
	
	/**
	 * 探测反弹
	 * 
	 * @param req
	 * @param args
	 * @return
	 * @throws EchoSoundException
	 */
	RES $rebound(REQ req, Object...args) throws EchoSoundException;
	
}
