package cn.ms.neural.moduler.extension.degrade.processor;

import cn.ms.neural.moduler.extension.degrade.conf.DegradeConf;

/**
 * 服务降级处理器
 * 
 * @author lry
 *
 * @param <REQ>
 * @param <RES>
 */
public interface IDegradeProcessor<REQ, RES> {

	/**
	 * 服务降级处理中心
	 * 
	 * @param degradeConf
	 * @param degradeREQ
	 * @return
	 * @throws Throwable
	 */
	RES processor(DegradeConf degradeConf, REQ degradeREQ) throws Throwable;

	/**
	 * 服务降级MOCK
	 * 
	 * @param degradeConf
	 * @param degradeREQ
	 * @return
	 * @throws Throwable
	 */
	RES mock(DegradeConf degradeConf, REQ degradeREQ) throws Throwable;

}
