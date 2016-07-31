package cn.ms.neural.moduler;

import cn.ms.neural.common.Constants;
import cn.ms.neural.common.URL;
import cn.ms.neural.common.spi.ExtensionLoader;
import cn.ms.neural.common.utils.NetUtils;
import cn.ms.neural.moduler.extension.blackwhite.IBlackWhite;
import cn.ms.neural.moduler.extension.degrade.IDegrade;
import cn.ms.neural.moduler.extension.echosound.IEchoSound;
import cn.ms.neural.moduler.extension.gracestop.IGraceStop;
import cn.ms.neural.moduler.extension.idempotent.Idempotent;
import cn.ms.neural.moduler.extension.pipescaling.IPipeScaling;
import cn.ms.neural.moduler.neure.INeure;

/**
 * 1.泛化引用、泛化实现<br>
 * 2.链路追踪、容量规划、实时监控<br>
 * 3.优雅停机→黑白名单→管道缩放→流量控制→资源鉴权→服务降级→幂等保障→灰度路由→回声探测→[熔断拒绝→超时控制→舱壁隔离→服务容错→慢性尝试]<br>
 * 
 * @author lry
 * @version v1.0
 */
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
	IDegrade<REQ, RES> degrade=ExtensionLoader.getExtensionLoader(IDegrade.class).getAdaptiveExtension();
	/**
	 * 幂等模块
	 */
	@SuppressWarnings("unchecked")
	Idempotent<REQ, RES> idempotent=ExtensionLoader.getExtensionLoader(Idempotent.class).getAdaptiveExtension();
	
	/**
	 * 回声探测
	 */
	@SuppressWarnings("unchecked")
	IEchoSound<REQ, RES> echoSound=ExtensionLoader.getExtensionLoader(IEchoSound.class).getAdaptiveExtension();
	
	/**
	 * 容错内核
	 */
	@SuppressWarnings("unchecked")
	INeure<REQ, RES> neure=ExtensionLoader.getExtensionLoader(INeure.class).getAdaptiveExtension();

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

	public IDegrade<REQ, RES> getDegrade() {
		return degrade;
	}

	public void setDegrade(IDegrade<REQ, RES> degrade) {
		this.degrade = degrade;
	}

	public Idempotent<REQ, RES> getIdempotent() {
		return idempotent;
	}

	public void setIdempotent(Idempotent<REQ, RES> idempotent) {
		this.idempotent = idempotent;
	}

	public IEchoSound<REQ, RES> getEchoSound() {
		return echoSound;
	}

	public void setEchoSound(IEchoSound<REQ, RES> echoSound) {
		this.echoSound = echoSound;
	}

	public INeure<REQ, RES> getNeure() {
		return neure;
	}

	public void setNeure(INeure<REQ, RES> neure) {
		this.neure = neure;
	}
	
}
