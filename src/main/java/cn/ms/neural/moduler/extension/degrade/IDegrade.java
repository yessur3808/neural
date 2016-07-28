package cn.ms.neural.moduler.extension.degrade;

import cn.ms.neural.Adaptor;
import cn.ms.neural.moduler.extension.degrade.conf.DegradeConf;
import cn.ms.neural.moduler.extension.degrade.processor.IBizDegradeProcessor;
import cn.ms.neural.moduler.extension.degrade.processor.IDegradeProcessor;

/**
 * 服务降级
 * <br>
 * 服务降级分类:<br>
 * 1.直接屏蔽降级<br>
 * 2.快速容错降级<br>
 * 3.自定义业务降级
 * <br>
 * 降级策略:<br>
 * 1.返回空<br>
 * 2.抛异常<br>
 * 3.本地mock<br>
 * 4.自定义策略
 * 
 * @author lry
 */
public interface IDegrade<REQ, RES> extends Adaptor {

	/**
	 * 服务降级处理器
	 * 
	 * @param degradeConf
	 * @param degradeREQ
	 * @param bizDegradeHandler
	 * @param degradeHandler
	 * @return
	 * @throws Throwable
	 */
	RES degrade(DegradeConf degradeConf, REQ degradeREQ, IBizDegradeProcessor<REQ, RES> bizDegradeHandler, IDegradeProcessor<REQ, RES> degradeHandler) throws Throwable;
	
}
