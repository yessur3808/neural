package cn.ms.neural.support;

import cn.ms.neural.INotify;
import cn.ms.neural.common.URL;
import cn.ms.neural.moduler.IModuler;
import cn.ms.neural.moduler.Moduler;

/**
 * 微服务神经元抽象工厂
 * 
 * @author lry
 * @version v1.0
 */
public abstract class AbstractNeuralFactory<REQ, RES> implements IModuler<REQ, RES>,INotify<URL> {
	
	protected Moduler<REQ, RES> moduler=new Moduler<REQ, RES>();
	
	/**
	 * 配置变更后广播事件
	 */
	@Override
	public void notify(URL msg) {
		moduler.setUrl(msg);
		notify(moduler);
	}
	
	/**
	 * 设置模块中心
	 */
	@Override
	public void notify(Moduler<REQ, RES> moduler) {
		this.moduler=moduler;
		
		//$NON-NLS-按照顺序$
		this.moduler.getGraceStop().notify(this.moduler);
		this.moduler.getBlackWhite().notify(this.moduler);
		this.moduler.getPipeScaling().notify(this.moduler);
		this.moduler.getDegrade().notify(this.moduler);
		this.moduler.getIdempotent().notify(this.moduler);
		this.moduler.getNeure().notify(this.moduler);
	}
	
	@Override
	public void init() throws Throwable {
		moduler.getGraceStop().init();
	}
	
	@Override
	public void destory() throws Throwable {
		
	}

}
