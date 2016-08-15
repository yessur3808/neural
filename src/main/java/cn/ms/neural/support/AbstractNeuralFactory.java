package cn.ms.neural.support;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.ms.neural.INotify;
import cn.ms.neural.common.URL;
import cn.ms.neural.common.exception.neure.NeureException;
import cn.ms.neural.common.spi.ExtensionLoader;
import cn.ms.neural.moduler.IModuler;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.ModulerType;
import cn.ms.neural.moduler.extension.blackwhite.IBlackWhite;
import cn.ms.neural.moduler.extension.degrade.IDegrade;
import cn.ms.neural.moduler.extension.echosound.IEchoSound;
import cn.ms.neural.moduler.extension.flowrate.IFlowRate;
import cn.ms.neural.moduler.extension.gracestop.IGraceStop;
import cn.ms.neural.moduler.extension.idempotent.Idempotent;
import cn.ms.neural.moduler.extension.pipescaling.IPipeScaling;
import cn.ms.neural.moduler.neure.INeure;

/**
 * 微服务神经元抽象工厂
 * 
 * @author lry
 * @version v1.0
 */
public abstract class AbstractNeuralFactory<REQ, RES> implements IModuler<REQ, RES>,INotify<URL> {
	
	protected Moduler<REQ, RES> moduler=new Moduler<REQ, RES>();
	public List<ModulerType> modulerTypes=ModulerType.getModulerTypes();
	public Map<ModulerType,IModuler<REQ, RES>> modulers=new LinkedHashMap<ModulerType,IModuler<REQ, RES>>();
	
	/**
	 * 初始化
	 */
	@Override
	public void init() throws Throwable {
		//$NON-NLS-模块读取$
		for (ModulerType modulerType:modulerTypes) {
			@SuppressWarnings("unchecked")
			IModuler<REQ, RES> tempModuler= (IModuler<REQ, RES>) ExtensionLoader.getExtensionLoader(modulerType.getClazz()).getAdaptiveExtension();
			modulers.put(modulerType, tempModuler);
			
			switch (modulerType) {
			case GraceStop:
				moduler.setGraceStop((IGraceStop<REQ, RES>) tempModuler);
				break;
			case BlackWhite:
				moduler.setBlackWhite((IBlackWhite<REQ, RES>) tempModuler);
				break;
			case PipeScaling:
				moduler.setPipeScaling((IPipeScaling<REQ, RES>) tempModuler);
				break;
			case Degrade:
				moduler.setDegrade((IDegrade<REQ, RES>) tempModuler);
				break;
			case EchoSound:
				moduler.setEchoSound((IEchoSound<REQ, RES>) tempModuler);
				break;
			case FlowRate:
				moduler.setFlowRate((IFlowRate<REQ, RES>) tempModuler);
				break;
			case Idempotent:
				moduler.setIdempotent((Idempotent<REQ, RES>) tempModuler);
				break;
			case Neure:
				moduler.setNeure((INeure<REQ, RES>) tempModuler);
				break;
			default:
				throw new NeureException("未知类型");
			}
		}
		
		//$NON-NLS-初始化$
		for (IModuler<REQ,RES> initModuler:modulers.values()) {
			initModuler.init();
		}
	}
	
	/**
	 * 通知变更广播
	 */
	@Override
	public void notify(URL msg) {
		moduler.setUrl(msg);
		this.notify(moduler);
	}
	
	/**
	 * 通知变更
	 */
	@Override
	public void notify(Moduler<REQ, RES> moduler) {
		this.moduler=moduler;
		
		//$NON-NLS-按照顺序$
		this.moduler.getGraceStop().notify(this.moduler);
		this.moduler.getBlackWhite().notify(this.moduler);
		this.moduler.getPipeScaling().notify(this.moduler);
		this.moduler.getFlowRate().notify(this.moduler);
		this.moduler.getDegrade().notify(this.moduler);
		this.moduler.getIdempotent().notify(this.moduler);
		this.moduler.getEchoSound().notify(this.moduler);
		this.moduler.getNeure().notify(this.moduler);
	}
	
	/**
	 * 销毁
	 */
	@Override
	public void destory() throws Throwable {
		//$NON-NLS-销毁$
		for (IModuler<REQ,RES> initModuler:modulers.values()) {
			initModuler.destory();
		}
	}

}
