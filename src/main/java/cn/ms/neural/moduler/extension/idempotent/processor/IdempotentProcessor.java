package cn.ms.neural.moduler.extension.idempotent.processor;

import cn.ms.neural.moduler.extension.idempotent.conf.IdempotentConf;

public interface IdempotentProcessor<REQ,RES> {

	RES handler(IdempotentConf idempotentConf, REQ idempotentREQ) throws Throwable;
	
}
