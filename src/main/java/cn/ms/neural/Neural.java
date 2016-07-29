package cn.ms.neural;

import java.util.Map;

import cn.ms.neural.common.exception.ProcessorException;
import cn.ms.neural.common.exception.blackwhite.BlackWhiteListException;
import cn.ms.neural.moduler.extension.blackwhite.processor.IBlackWhiteProcessor;
import cn.ms.neural.moduler.extension.gracestop.processor.IGraceStopProcessor;
import cn.ms.neural.moduler.extension.pipescaling.processor.IPipeScalingProcessor;

/**
 * 微服务神经元 <br>
 * 
 * 1.泛化引用、泛化实现<br>
 * 2.链路追踪、容量规划、实时监控<br>
 * 3.优雅停机→黑白名单→管道缩放→流量控制→资源鉴权→服务降级→幂等保障→灰度路由→回声探测→[熔断拒绝→超时控制→舱壁隔离→服务容错→慢性尝试]<br>
 * 
 * @author lry
 *
 * @param <REQ>
 * @param <RES>
 */
public class Neural<REQ, RES> extends NeuralFactory<REQ, RES>{

	/**
	 * 优雅停机
	 * 
	 * @param req
	 * @param args
	 * @return
	 */
	public RES gracestop(REQ req, Object... args) {
		return moduler.getGraceStop().gracestop(req, new IGraceStopProcessor<REQ, RES>() {
			@Override
			public RES processor(REQ req, Object... args) throws ProcessorException {
				System.out.println("这是优雅停机："+req);
				return null;
			}
		}, args);
	}

	/**
	 * 黑白名单
	 * 
	 * @param req
	 * @param blackWhiteIdKeyVals
	 * @param args
	 * @return
	 * @throws BlackWhiteListException
	 */
	public RES blackwhite(REQ req, Map<String, Object> blackWhiteIdKeyVals, Object... args) throws BlackWhiteListException{
		return moduler.getBlackWhite().blackwhite(req, blackWhiteIdKeyVals, new IBlackWhiteProcessor<REQ, RES>() {
			@Override
			public RES processor(REQ req, Object... args) throws ProcessorException {
				System.out.println("这是黑白名单过滤："+req);
				return null;
			}
		}, args);
	}
	
	/**
	 * 管道缩放控制
	 * 
	 * @param req
	 * @param args
	 * @return
	 * @throws BlackWhiteListException
	 */
	public RES pipescaling(REQ req, Object... args) throws BlackWhiteListException{
		return moduler.getPipeScaling().pipescaling(req, new IPipeScalingProcessor<REQ, RES>() {
			@Override
			public RES processor(REQ req, Object... args) throws ProcessorException {
				System.out.println("这是管道缩放控制："+req);
				return null;
			}
		}, args);
	}
	
}
