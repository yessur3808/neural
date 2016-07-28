package cn.ms.neural.moduler;

import cn.ms.neural.Adaptor;

public interface IModuler<REQ, RES> extends Adaptor {
	
	void setModuler(Moduler<REQ, RES> moduler);
	
}
