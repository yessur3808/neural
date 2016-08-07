package cn.ms.neural.processor;

import cn.ms.neural.moduler.extension.blackwhite.processor.IBlackWhiteProcessor;
import cn.ms.neural.moduler.extension.degrade.processor.IDegradeProcessor;
import cn.ms.neural.moduler.extension.echosound.processor.IEchoSoundProcessor;
import cn.ms.neural.moduler.extension.gracestop.processor.IGraceStopProcessor;
import cn.ms.neural.moduler.extension.idempotent.processor.IdempotentProcessor;
import cn.ms.neural.moduler.extension.pipescaling.processor.IPipeScalingProcessor;
import cn.ms.neural.moduler.neure.processor.INeureProcessor;

/**
 * 微服务神经元处理中心
 * 
 * @author lry
 * @version v1.0
 */
public interface INeuralProcessor<REQ, RES> extends IProcessor<REQ, RES>,

		/**
		 * 优雅停机
		 */
		IGraceStopProcessor<REQ, RES>,

		/**
		 * 黑白名单
		 */
		IBlackWhiteProcessor<REQ, RES>,

		/**
		 * 管道缩放
		 */
		IPipeScalingProcessor<REQ, RES>,

		/**
		 * 服务降级
		 */
		IDegradeProcessor<REQ, RES>,

		/**
		 * 幂等机制
		 */
		IdempotentProcessor<REQ, RES>,

		/**
		 * 回声探测
		 */
		IEchoSoundProcessor<REQ, RES>,

		/**
		 * 内核
		 */
		INeureProcessor<REQ, RES> {

}
