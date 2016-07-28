package cn.ms.neural.moduler.extension.degrade.core;

import cn.ms.neural.common.exception.degrade.DegradeException;
import cn.ms.neural.moduler.extension.degrade.IDegrade;
import cn.ms.neural.moduler.extension.degrade.conf.DegradeConf;
import cn.ms.neural.moduler.extension.degrade.processor.IBizDegradeProcessor;
import cn.ms.neural.moduler.extension.degrade.processor.IDegradeProcessor;

/**
 * 服务降级
 * 
 * @author lry
 *
 * @param <REQ>
 * @param <RES>
 */
public class DegradeFactory<REQ, RES> implements IDegrade<REQ, RES> {

	/**
	 * 初始化
	 */
	public void init() throws Throwable {
		
	}

	/**
	 * 服务降级
	 */
	public RES degrade(DegradeConf degradeConf, REQ degradeREQ, IBizDegradeProcessor<REQ, RES> bizDegradeHandler, IDegradeProcessor<REQ, RES> degradeHandler) throws Throwable {
		if(!degradeConf.isDegradeEnable()){//服务降级开关关闭,则直接拉起引擎
			return degradeHandler.processor(degradeConf, degradeREQ);
		}
		
		switch (degradeConf.getDegradeType()) {
		case SHIELDING:// 屏蔽降级
			return doDegrade(degradeConf, degradeREQ, degradeHandler);
		case FAULTTOLERANT:// 容错降级
			try {
				return degradeHandler.processor(degradeConf, degradeREQ);
			} catch (Throwable t) {
				t.printStackTrace();
				return doDegrade(degradeConf, degradeREQ, degradeHandler);// 直接失败,然后执行容错降级
			}
		case BUSINESS:// 业务降级
			return bizDegradeHandler.processor(degradeConf, degradeREQ, degradeHandler);
		default:
			throw new DegradeException("'degradeType' is illegal type.");
		}
	}
	
	/**
	 * 服务降级策略类型
	 * 
	 * @param degradeConf
	 * @param degradeREQ
	 * @param degradeHandler
	 * @return
	 * @throws Throwable
	 */
	private RES doDegrade(DegradeConf degradeConf, REQ degradeREQ, IDegradeProcessor<REQ, RES> degradeHandler) throws Throwable{
		switch (degradeConf.getStrategyType()) {
		case NULL:// 返回null降级
			return null;
		case MOCK:// 调用mock降级
			return degradeHandler.mock(degradeConf, degradeREQ);
		case EXCEPTION:// 抛异常降级
			throw new DegradeException("Service degradation: throw exception.");
		default:
			throw new DegradeException("The 'strategyType' is illegal type.");
		}
	}
	
	/**
	 * 销毁
	 */
	public void destory() throws Throwable {
		
	}

}
