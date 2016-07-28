package cn.ms.neural.moduler.extension.degrade.processor;

import cn.ms.neural.moduler.extension.degrade.conf.DegradeConf;

/**
 * 业务服务降级
 * 
 * @author lry
 */
public interface IBizDegradeProcessor<REQ, RES> {

	/**
	 * 业务降级处理器
	 * 
	 * @param bizDegradeConf
	 * @param bizDegradeREQ
	 * @param runner
	 * @return
	 * @throws Throwable
	 */
	RES processor(DegradeConf bizDegradeConf, REQ bizDegradeREQ, IDegradeProcessor<REQ, RES> runner) throws Throwable;
	
}
