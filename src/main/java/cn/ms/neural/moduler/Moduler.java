package cn.ms.neural.moduler;

import cn.ms.neural.common.Constants;
import cn.ms.neural.common.URL;
import cn.ms.neural.common.spi.ExtensionLoader;
import cn.ms.neural.common.utils.NetUtils;
import cn.ms.neural.moduler.extension.blackwhite.IBlackWhite;
import cn.ms.neural.moduler.extension.degrade.IDegrade;
import cn.ms.neural.moduler.extension.gracestop.IGraceStop;
import cn.ms.neural.moduler.extension.pipescaling.IPipeScaling;

public class Moduler<REQ, RES> {
	
	/**
	 * 统一配置资源
	 */
	URL url=new URL(Constants.DEFAULT_PROTOCOL_VALUE, NetUtils.getLocalHostName(),  Constants.DEFAULT_PORT_VALUE, this.getClass().getName());
	
	/**
	 * 优雅停机
	 */
	@SuppressWarnings("unchecked")
	IGraceStop<REQ, RES> graceStop=ExtensionLoader.getExtensionLoader(IGraceStop.class).getAdaptiveExtension();
	/**
	 * 黑白名单
	 */
	@SuppressWarnings("unchecked")
	IBlackWhite<REQ, RES> blackWhite=ExtensionLoader.getExtensionLoader(IBlackWhite.class).getAdaptiveExtension();
	/**
	 * 管道缩放
	 */
	@SuppressWarnings("unchecked")
	IPipeScaling<REQ, RES> pipeScaling=ExtensionLoader.getExtensionLoader(IPipeScaling.class).getAdaptiveExtension();
	
	/**
	 * 服务降级
	 */
	@SuppressWarnings("unchecked")
	IDegrade<String, String> degrade=ExtensionLoader.getExtensionLoader(IDegrade.class).getAdaptiveExtension();
	
	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}
	
	public IGraceStop<REQ, RES> getGraceStop() {
		return graceStop;
	}

	public void setGraceStop(IGraceStop<REQ, RES> graceStop) {
		this.graceStop = graceStop;
	}

	public IBlackWhite<REQ, RES> getBlackWhite() {
		return blackWhite;
	}

	public void setBlackWhite(IBlackWhite<REQ, RES> blackWhite) {
		this.blackWhite = blackWhite;
	}

	public IPipeScaling<REQ, RES> getPipeScaling() {
		return pipeScaling;
	}

	public void setPipeScaling(IPipeScaling<REQ, RES> pipeScaling) {
		this.pipeScaling = pipeScaling;
	}

	public IDegrade<String, String> getDegrade() {
		return degrade;
	}

	public void setDegrade(IDegrade<String, String> degrade) {
		this.degrade = degrade;
	}

}
