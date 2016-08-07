package cn.ms.neural.moduler.extension.echosound;

import cn.ms.neural.common.exception.echosound.EchoSoundException;

public interface IEcho<REQ, RES> {

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
