package cn.ms.neural;

import cn.ms.neural.common.URL;
import cn.ms.neural.moduler.IModuler;
import cn.ms.neural.moduler.Moduler;

public class NeuralFactory<REQ, RES> implements IModuler<REQ, RES>,INotify<URL> {
	
	protected Moduler<REQ, RES> moduler;
	
	@Override
	public void init() throws Throwable {
		moduler.getGraceStop().init();
		
	}

	/**
	 * 设置模块中心
	 */
	@Override
	public void notify(Moduler<REQ, RES> moduler) {
		if(moduler==null){
			this.moduler=new Moduler<REQ, RES>();
		}else{
			this.moduler=moduler;
		}
		
		//$NON-NLS-按照顺序$
		this.moduler.getGraceStop().notify(this.moduler);
	}
	
	/**
	 * 配置变更后广播事件
	 */
	@Override
	public void notify(URL msg) {
		moduler.setUrl(msg);
		notify(moduler);
	}
	
	
	@Override
	public void destory() throws Throwable {
		
	}

	
}
