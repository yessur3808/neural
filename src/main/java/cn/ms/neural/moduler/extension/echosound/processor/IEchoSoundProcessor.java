package cn.ms.neural.moduler.extension.echosound.processor;

public interface IEchoSoundProcessor<REQ, RES> {

	RES handler(REQ req) throws Throwable;
	
}
