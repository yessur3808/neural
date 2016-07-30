package cn.ms.neural.moduler.extension.echosound;

import cn.ms.neural.common.exception.EchoSoundException;

public interface IEcho<REQ, RES> {

	/**
	 * 发射探测
	 * 
	 * @param req
	 * @param processor
	 * @param args
	 * @return
	 * @throws EchoSoundException
	 */
	REQ $echo(Object...args) throws EchoSoundException;
	
	/**
	 * 探测反弹
	 * 
	 * @param req
	 * @param processor
	 * @param args
	 * @return
	 * @throws EchoSoundException
	 */
	RES $rebound(Object...args) throws EchoSoundException;
	
}
