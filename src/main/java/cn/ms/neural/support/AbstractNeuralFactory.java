package cn.ms.neural.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ms.neural.INotify;
import cn.ms.neural.common.URL;
import cn.ms.neural.common.spi.ExtensionLoader;
import cn.ms.neural.moduler.IModuler;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.ModulerType;

/**
 * 微服务神经元抽象工厂
 * 
 * @author lry
 * @version v1.0
 */
public abstract class AbstractNeuralFactory<REQ, RES> implements IModuler<REQ, RES>,INotify<URL> {
	
	protected Moduler<REQ, RES> moduler=new Moduler<REQ, RES>();
	public List<ModulerType> modulerTypes=ModulerType.getModulerTypes();
	public Map<ModulerType,IModuler<REQ, RES>> modulers=new HashMap<ModulerType,IModuler<REQ, RES>>();
	
	/**
	 * 初始化
	 */
	@Override
	public void init() throws Throwable {
		for (ModulerType modulerType:modulerTypes) {
			@SuppressWarnings("unchecked")
			IModuler<REQ, RES> moduler= (IModuler<REQ, RES>) ExtensionLoader.getExtensionLoader(modulerType.getClazz()).getAdaptiveExtension();
			modulers.put(modulerType, moduler);
		}
		
		this.moduler.getGraceStop().init();
		this.moduler.getBlackWhite().init();
		this.moduler.getPipeScaling().init();
		this.moduler.getFlowRate().init();
		this.moduler.getDegrade().init();
		this.moduler.getIdempotent().init();
		this.moduler.getEchoSound().init();
		this.moduler.getNeure().init();
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
		this.moduler.getGraceStop().destory();
		this.moduler.getBlackWhite().destory();
		this.moduler.getPipeScaling().destory();
		this.moduler.getFlowRate().destory();
		this.moduler.getDegrade().destory();
		this.moduler.getIdempotent().destory();
		this.moduler.getEchoSound().destory();
		this.moduler.getNeure().destory();
	}

}
