package cn.ms.neural;

import java.util.Map;

import cn.ms.neural.chain.moduler.BlackWhiteChain;
import cn.ms.neural.chain.moduler.DegradeChain;
import cn.ms.neural.chain.moduler.EchoSoundChain;
import cn.ms.neural.chain.moduler.FlowRateChain;
import cn.ms.neural.chain.moduler.GraceStopChain;
import cn.ms.neural.chain.moduler.IdempotentChain;
import cn.ms.neural.chain.moduler.NeureChain;
import cn.ms.neural.chain.moduler.PipeScalingChain;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.extension.echosound.type.EchoSoundType;
import cn.ms.neural.processor.INeuralProcessor;
import cn.ms.neural.support.AbstractNeuralFactory;

public class Neural<REQ, RES> extends AbstractNeuralFactory<REQ, RES> {

	GraceStopChain<REQ, RES> graceStopHandler = null;
	BlackWhiteChain<REQ, RES> blackWhiteHandler = null;
	PipeScalingChain<REQ, RES> pipeScalingHandler = null;
	FlowRateChain<REQ, RES> flowRateHandler = null;
	DegradeChain<REQ, RES> degradeHandler = null;
	IdempotentChain<REQ, RES> idempotentHandler = null;
	EchoSoundChain<REQ, RES> echoSoundHandler = null;
	NeureChain<REQ, RES> neureHandler = null;
	
	public Neural(Moduler<REQ, RES> moduler) {
		try {
			super.init();
			super.notify(moduler);//通知节点配置信息
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
		//$NON-NLS-建造模块功能$
		graceStopHandler = new GraceStopChain<REQ, RES>(moduler);
		blackWhiteHandler = new BlackWhiteChain<REQ, RES>(moduler);
		pipeScalingHandler = new PipeScalingChain<REQ, RES>(moduler);
		flowRateHandler = new FlowRateChain<REQ, RES>(moduler);
		degradeHandler = new DegradeChain<REQ, RES>(moduler);
		idempotentHandler = new IdempotentChain<REQ, RES>(moduler);
		echoSoundHandler = new EchoSoundChain<REQ, RES>(moduler);
		neureHandler = new NeureChain<REQ, RES>(moduler);
		
		// 优雅停机 -->  黑白名单
		graceStopHandler.setNeuralChainHandler(blackWhiteHandler);
		// 黑白名单 --> 管道缩放
		blackWhiteHandler.setNeuralChainHandler(pipeScalingHandler);
		// 管道缩放 --> 流量控制
		pipeScalingHandler.setNeuralChainHandler(flowRateHandler);
		// 流量控制 --> 服务降级
		flowRateHandler.setNeuralChainHandler(degradeHandler);
		// 服务降级 --> 幂等机制
		degradeHandler.setNeuralChainHandler(idempotentHandler);
		// 幂等机制 --> 回声探测
		idempotentHandler.setNeuralChainHandler(echoSoundHandler);
		// 回声探测 --> 服务容错(熔断拒绝→超时控制→舱壁隔离→服务容错→慢性尝试)
		echoSoundHandler.setNeuralChainHandler(neureHandler);
		
	}
	
	public void name() {
		
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
		
		return graceStopHandler.chain(req, neuralId, echoSoundType, blackWhiteIdKeyVals, processor, args);
	}
	
}