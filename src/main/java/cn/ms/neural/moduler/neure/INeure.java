package cn.ms.neural.moduler.neure;

import cn.ms.neural.common.exception.neure.NeureException;
import cn.ms.neural.moduler.IModuler;
import cn.ms.neural.moduler.neure.handler.INeureHandler;

public interface INeure<REQ, RES> extends IModuler<REQ, RES> {

	RES neure(REQ req, INeureHandler<REQ, RES> handler, Object...args) throws NeureException;
	
}
