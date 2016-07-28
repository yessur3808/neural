package cn.ms.neural.moduler.extension.gracestop;

import cn.ms.neural.moduler.IModuler;
import cn.ms.neural.moduler.extension.gracestop.processor.IGraceStopProcessor;

public interface IGraceStop<REQ, RES> extends IModuler<REQ, RES>{

	RES gracestop(REQ req, IGraceStopProcessor<REQ, RES> processor, Object... args);

}
