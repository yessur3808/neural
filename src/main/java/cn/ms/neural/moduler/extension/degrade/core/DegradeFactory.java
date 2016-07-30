package cn.ms.neural.moduler.extension.degrade.core;

import cn.ms.neural.common.exception.degrade.DegradeException;
import cn.ms.neural.moduler.Conf;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.extension.degrade.IDegrade;
import cn.ms.neural.moduler.extension.degrade.conf.DegradeConf;
import cn.ms.neural.moduler.extension.degrade.processor.IBizDegradeProcessor;
import cn.ms.neural.moduler.extension.degrade.processor.IDegradeProcessor;
import cn.ms.neural.moduler.extension.degrade.type.DegradeType;
import cn.ms.neural.moduler.extension.degrade.type.StrategyType;

/**
 * 服务降级
 * 
 * @author lry
 *
 * @param <REQ>
 * @param <RES>
 */
public class DegradeFactory<REQ, RES> implements IDegrade<REQ, RES> {

	Moduler<REQ, RES> moduler;
	/**
	 * 服务降级开关
	 */
	boolean degradeSwitch=false;
	/**
	 * 服务降级类型
	 */
	DegradeType degradeType;
	/**
	 * 服务降级处理措施
	 */
	StrategyType strategyType;
	
	@Override
	public void setModuler(Moduler<REQ, RES> moduler) {
		this.moduler=moduler;
		
		degradeSwitch=this.moduler.getUrl().getModulerParameter(Conf.DEGRADE, DegradeConf.SWITCH_KEY, DegradeConf.SWITCH_DEF_VALUE);
		
		//服务降级类型
		String degradeTypeStr=this.moduler.getUrl().getModulerParameter(Conf.DEGRADE, DegradeConf.DEGRADETYPE_KEY, DegradeConf.DEGRADETYPE_DEF_VALUE);
		degradeType=DegradeType.valueOf(degradeTypeStr);
		
		//服务降级处理措施
		String strategyTypeStr=this.moduler.getUrl().getModulerParameter(Conf.DEGRADE, DegradeConf.STRATEGYTYPE_KEY, DegradeConf.STRATEGYTYPE_DEF_VALUE);
		strategyType=StrategyType.valueOf(strategyTypeStr);
	}
	
	/**
	 * 初始化
	 */
	@Override
	public void init() throws Throwable {
		
	}

	@Override
	public RES degrade(REQ req, IDegradeProcessor<REQ, RES> processor, IBizDegradeProcessor<REQ, RES> bizprocessor, Object... args) throws DegradeException {
		if(!degradeSwitch){//服务降级开关关闭,则直接拉起引擎
			return processor.processor(req, args);
		}
		
		switch (degradeType) {
		case SHIELDING:// 屏蔽降级
			return doDegrade(req, strategyType, processor, args);
		case FAULTTOLERANT:// 容错降级
			try {
				return processor.processor(req, args);
			} catch (Throwable t) {
				t.printStackTrace();
				return doDegrade(req, strategyType, processor, args);
			}
		case BUSINESS:// 业务降级
			return bizprocessor.processor(req, processor, args);
		default:
			throw new DegradeException("'degradeType' is illegal type.");
		}
	}
	
	/**
	 * 服务降级策略类型
	 * @param req
	 * @param strategyType
	 * @param processor
	 * @param args
	 * @return
	 * @throws DegradeException
	 */
	private RES doDegrade(REQ req, StrategyType strategyType, IDegradeProcessor<REQ, RES> processor, Object...args) throws DegradeException {
		switch (strategyType) {
		case NULL:// 返回null降级
			return null;
		case MOCK:// 调用mock降级
			return processor.mock(req, args);
		case EXCEPTION:// 抛异常降级
			throw new DegradeException("Service degradation: throw exception.");
		default:
			throw new DegradeException("The 'strategyType' is illegal type.");
		}
	}
	
	/**
	 * 销毁
	 */
	@Override
	public void destory() throws Throwable {
		
	}

}
