package cn.ms.neural;

import java.util.Map;

import cn.ms.neural.chain.core.BlackWhiteChain;
import cn.ms.neural.chain.core.DegradeChain;
import cn.ms.neural.chain.core.EchoSoundChain;
import cn.ms.neural.chain.core.FlowRateChain;
import cn.ms.neural.chain.core.GraceStopChain;
import cn.ms.neural.chain.core.IdempotentChain;
import cn.ms.neural.chain.core.NeureChain;
import cn.ms.neural.chain.core.PipeScalingChain;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.extension.echosound.type.EchoSoundType;
import cn.ms.neural.processor.INeuralProcessor;
import cn.ms.neural.support.AbstractNeuralFactory;

/**
 * 微服务神经元 <br>
 * <br>
 * 注意:使用时请单例化使用<br>
 * 1.泛化引用、泛化实现<br>
 * 2.链路追踪、容量规划、实时监控<br>
 * 3.优雅停机→黑白名单→管道缩放→流量控制→资源鉴权→服务降级→幂等保障→灰度路由→回声探测→[熔断拒绝→超时控制→舱壁隔离→服务容错→慢性尝试]<br>
 * <br>
 * 待实现:
 * <br>
 * 链路追踪<br>
 * 容量规划<br>
 * 实时监控<br>
 * 资源鉴权<br>
 * 灰度路由<br>
 * @author lry
 *
 * @param <REQ> 请求对象
 * @param <RES> 响应对象
 */
public class Neural<REQ, RES> extends AbstractNeuralFactory<REQ, RES> {

	GraceStopChain<REQ, RES> graceStopChain = null;
	BlackWhiteChain<REQ, RES> blackWhiteChain = null;
	PipeScalingChain<REQ, RES> pipeScalingChain = null;
	FlowRateChain<REQ, RES> flowRateChain = null;
	DegradeChain<REQ, RES> degradeChain = null;
	IdempotentChain<REQ, RES> idempotentChain = null;
	EchoSoundChain<REQ, RES> echoSoundChain = null;
	NeureChain<REQ, RES> neureChain = null;
	
	public Neural(Moduler<REQ, RES> moduler) {
		try {
			super.init();
			super.notify(moduler);//通知节点配置信息
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
		//$NON-NLS-建造模块功能$
		graceStopChain = new GraceStopChain<REQ, RES>(moduler);
		blackWhiteChain = new BlackWhiteChain<REQ, RES>(moduler);
		pipeScalingChain = new PipeScalingChain<REQ, RES>(moduler);
		flowRateChain = new FlowRateChain<REQ, RES>(moduler);
		degradeChain = new DegradeChain<REQ, RES>(moduler);
		idempotentChain = new IdempotentChain<REQ, RES>(moduler);
		echoSoundChain = new EchoSoundChain<REQ, RES>(moduler);
		neureChain = new NeureChain<REQ, RES>(moduler);
		
		//$NON-NLS-责任链链接$
		// 优雅停机 -->  黑白名单
		graceStopChain.setNeuralChain(blackWhiteChain);
		// 黑白名单 --> 管道缩放
		blackWhiteChain.setNeuralChain(pipeScalingChain);
		// 管道缩放 --> 流量控制
		pipeScalingChain.setNeuralChain(flowRateChain);
		// 流量控制 --> 服务降级
		flowRateChain.setNeuralChain(degradeChain);
		// 服务降级 --> 幂等机制
		degradeChain.setNeuralChain(idempotentChain);
		// 幂等机制 --> 回声探测
		idempotentChain.setNeuralChain(echoSoundChain);
		// 回声探测 --> 服务容错(熔断拒绝→超时控制→舱壁隔离→服务容错→慢性尝试)
		echoSoundChain.setNeuralChain(neureChain);
		
	}
	
	/**
	 * 微服务神经元
	 * 
	 * @param req 请求对象
	 * @param neuralId 神经元请求ID(用于幂等控制)
	 * @param echoSoundType 回声探测类型
	 * @param blackWhiteIdKeyVals 黑白名单KEY-VALUE
	 * @param processor 微服务神经元处理器
	 * @param args 其他参数
	 * @return
	 */
	public RES neural(REQ req,
			String neuralId, 
			EchoSoundType echoSoundType, 
			Map<String, Object> blackWhiteIdKeyVals, 
			INeuralProcessor<REQ, RES> processor, Object...args) {
		
		//$NON-NLS-通过优雅停机开始进入模块链$
		return graceStopChain.chain(req, neuralId, echoSoundType, blackWhiteIdKeyVals, processor, args);
	}
	
}