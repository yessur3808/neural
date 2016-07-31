package cn.ms.neural;

import java.util.Map;

import cn.ms.neural.common.exception.EchoSoundException;
import cn.ms.neural.common.exception.ProcessorException;
import cn.ms.neural.common.exception.blackwhite.BlackWhiteListException;
import cn.ms.neural.common.exception.degrade.DegradeException;
import cn.ms.neural.common.exception.idempotent.IdempotentException;
import cn.ms.neural.common.exception.neure.NeureException;
import cn.ms.neural.moduler.extension.blackwhite.processor.IBlackWhiteProcessor;
import cn.ms.neural.moduler.extension.degrade.processor.IBizDegradeProcessor;
import cn.ms.neural.moduler.extension.degrade.processor.IDegradeProcessor;
import cn.ms.neural.moduler.extension.echosound.IEcho;
import cn.ms.neural.moduler.extension.echosound.processor.IEchoSoundProcessor;
import cn.ms.neural.moduler.extension.echosound.type.EchoSoundType;
import cn.ms.neural.moduler.extension.gracestop.processor.IGraceStopProcessor;
import cn.ms.neural.moduler.extension.idempotent.processor.IdempotentProcessor;
import cn.ms.neural.moduler.extension.pipescaling.processor.IPipeScalingProcessor;
import cn.ms.neural.moduler.neure.handler.INeureHandler;

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
	
	/**
	 * 服务降级
	 * @param req
	 * @param args
	 * @return
	 * @throws BlackWhiteListException
	 */
	public RES degrade(REQ req, Object... args) throws BlackWhiteListException{
		return moduler.getDegrade().degrade(req, new IDegradeProcessor<REQ, RES>() {
			@Override
			public RES processor(REQ req, Object... args) throws ProcessorException {
				System.out.println("这是服务降级控制："+req);
				return null;
			}
			@Override
			public RES mock(REQ req, Object... args) throws DegradeException {
				System.out.println("这是服务降级MOCK策略："+req);
				return null;
			}
		}, new IBizDegradeProcessor<REQ, RES>() {
			@Override
			public RES processor(REQ req, IDegradeProcessor<REQ, RES> processor, Object... args) throws DegradeException {
				System.out.println("这是业务服务降级："+req);
				return null;
			}
		}, args);
	}
	
	/**
	 * 这是幂等
	 * 
	 * @param idempotentKEY
	 * @param req
	 * @param args
	 * @return
	 * @throws NeureException
	 */
	public RES idempotent(String idempotentKEY, REQ req, Object... args) throws NeureException {
		return moduler.getIdempotent().idempotent(idempotentKEY, req, new IdempotentProcessor<REQ, RES>() {
			@Override
			public RES processor(REQ req, Object... args) throws ProcessorException {
				return null;
			}
			@Override
			public boolean check(String idempotentKEY) throws IdempotentException {
				return false;
			}
			@Override
			public RES get(String idempotentKEY) throws IdempotentException {
				return null;
			}
			@Override
			public void storage(REQ req, RES res, Object... args) throws IdempotentException {
			}
		}, args);
	}
	
	/**
	 * 回声探测
	 * 
	 * @param echoSoundType
	 * @param req
	 * @param args
	 * @return
	 * @throws NeureException
	 */
	public RES echosound(EchoSoundType echoSoundType, REQ req, Object... args) throws NeureException {
		return moduler.getEchoSound().echosound(echoSoundType, req, new IEchoSoundProcessor<REQ, RES>() {
			@Override
			public RES echosound(REQ req, Object... args) throws EchoSoundException {
				return null;
			}
		}, new IEcho<REQ, RES>() {
			@Override
			public REQ $echo(REQ req, Object... args) throws EchoSoundException {
				return null;
			}
			@Override
			public RES $rebound(REQ req, Object... args) throws EchoSoundException {
				return null;
			}
		}, args);
	}
	
	/**
	 * 容错内核
	 * <br>
	 * 包括:熔断拒绝→超时控制→舱壁隔离→服务容错→慢性尝试
	 * @param req
	 * @param args
	 * @return
	 * @throws NeureException
	 */
	public RES neure(REQ req, Object... args) throws NeureException {
		return moduler.getNeure().neure(req, new INeureHandler<REQ, RES>() {
			@Override
			public RES route(REQ req, Object... args) throws Throwable {
				return null;
			}
			@Override
			public RES faulttolerant(REQ req, Object... args) {
				return null;
			}
			@Override
			public long breath(long nowTimes, long nowExpend, long maxRetryNum, Object... args) throws Throwable {
				return 0;
			}
			@Override
			public void callback(RES res, Object... args) throws Throwable {
			}
			@Override
			public void alarm(REQ req, Throwable t, Object... args) throws Throwable {
			}
		}, args);
	}
	
}
