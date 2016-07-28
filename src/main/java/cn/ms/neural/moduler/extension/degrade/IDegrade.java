package cn.ms.neural.moduler.extension.degrade;

import cn.ms.neural.common.exception.degrade.DegradeException;
import cn.ms.neural.moduler.IModuler;
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
public interface IDegrade<REQ, RES> extends IModuler<REQ, RES> {

	/**
	 * 服务降级
	 * 
	 * @param req
	 * @param processor
	 * @param bizprocessor
	 * @param args
	 * @return
	 * @throws DegradeException
	 */
	RES degrade(REQ req, IDegradeProcessor<REQ, RES> processor, IBizDegradeProcessor<REQ, RES> bizprocessor, Object...args) throws DegradeException;
	
}
