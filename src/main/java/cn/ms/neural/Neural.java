package cn.ms.neural;

import java.util.Map;

import cn.ms.neural.common.exception.ProcessorException;
import cn.ms.neural.common.exception.degrade.DegradeException;
import cn.ms.neural.common.exception.echosound.EchoSoundException;
import cn.ms.neural.common.exception.idempotent.IdempotentException;
import cn.ms.neural.common.exception.neure.NeureAlarmException;
import cn.ms.neural.common.exception.neure.NeureBreathException;
import cn.ms.neural.common.exception.neure.NeureCallbackException;
import cn.ms.neural.common.exception.neure.NeureFaultTolerantException;
import cn.ms.neural.moduler.extension.blackwhite.processor.IBlackWhiteProcessor;
import cn.ms.neural.moduler.extension.degrade.processor.IDegradeProcessor;
import cn.ms.neural.moduler.extension.echosound.processor.IEchoSoundProcessor;
import cn.ms.neural.moduler.extension.echosound.type.EchoSoundType;
import cn.ms.neural.moduler.extension.gracestop.processor.IGraceStopProcessor;
import cn.ms.neural.moduler.extension.idempotent.processor.IdempotentProcessor;
import cn.ms.neural.moduler.extension.pipescaling.processor.IPipeScalingProcessor;
import cn.ms.neural.moduler.neure.processor.INeureProcessor;
import cn.ms.neural.moduler.neure.type.AlarmType;
import cn.ms.neural.processor.INeuralProcessor;
import cn.ms.neural.support.AbstractNeuralFactory;

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
public class Neural<REQ, RES> extends AbstractNeuralFactory<REQ, RES>{

	/**
	 * 微服务神经元
	 * 
	 * @param req
	 * @param neuralId
	 * @param echoSoundType
	 * @param blackWhiteIdKeyVals
	 * @param processor
	 * @param args
	 * @return
	 */
	public RES neural(REQ req,
			final String neuralId, 
			final EchoSoundType echoSoundType, 
			final Map<String, Object> blackWhiteIdKeyVals, 
			final INeuralProcessor<REQ, RES> processor, 
			Object...args) {
		
		//$NON-NLS-优雅停机开始$
		return moduler.getGraceStop().gracestop(req, new IGraceStopProcessor<REQ, RES>() {
			@Override
			public RES processor(REQ req, Object...args) throws ProcessorException {
				
				//$NON-NLS-黑白名单开始$
				return moduler.getBlackWhite().blackwhite(req, blackWhiteIdKeyVals, new IBlackWhiteProcessor<REQ, RES>() {
					@Override
					public RES processor(REQ req, Object...args) throws ProcessorException {
						
						//$NON-NLS-管道缩放开始$
						return moduler.getPipeScaling().pipescaling(req, new IPipeScalingProcessor<REQ, RES>() {
							@Override
							public RES processor(REQ req, Object...args) throws ProcessorException {
								
								//$NON-NLS-服务降级开始$
								return moduler.getDegrade().degrade(req, new IDegradeProcessor<REQ, RES>() {
									@Override
									public RES processor(REQ req, Object...args) throws ProcessorException {
										
										//$NON-NLS-幂等开始$
										return moduler.getIdempotent().idempotent(neuralId, req, new IdempotentProcessor<REQ, RES>() {
											@Override
											public RES processor(REQ req, Object...args) throws ProcessorException {
												
												//$NON-NLS-回声探测开始$
												return moduler.getEchoSound().echosound(echoSoundType, req, new IEchoSoundProcessor<REQ, RES>() {
													@Override
													public RES processor(REQ req, Object...args) throws ProcessorException {
														
														//$NON-NLS-容错内核开始(熔断拒绝→超时控制→舱壁隔离→服务容错→慢性尝试)$
														return moduler.getNeure().neure(req, new INeureProcessor<REQ, RES>() {
															@Override
															public RES processor(REQ req, Object...args) throws ProcessorException {//内核业务封装
																return processor.processor(req, args);
															}
															/**
															 * 内核容错
															 */
															@Override
															public RES faulttolerant(REQ req, Object...args) throws NeureFaultTolerantException{
																return processor.faulttolerant(req, args);
															}
															/**
															 * 内核呼吸
															 */
															@Override
															public long breath(long nowTimes, long nowExpend, long maxRetryNum, Object...args) throws NeureBreathException {
																return processor.breath(nowTimes, nowExpend, maxRetryNum, args);
															}
															/**
															 * 内核回调
															 */
															@Override
															public void callback(RES res, Object...args) throws NeureCallbackException {
																processor.callback(res, args);
															}
															/**
															 * 内核异常告警
															 */
															@Override
															public void alarm(AlarmType alarmType, REQ req, Throwable t, Object...args) throws NeureAlarmException {
																processor.alarm(alarmType, req, t, args);
															}
														}, args);//$NON-NLS-容错内核结束$
													}
													/**
													 * 回声探测请求
													 */
													@Override
													public REQ $echo(REQ req, Object...args) throws EchoSoundException {
														return processor.$echo(req, args);
													}
													/**
													 * 回声探测响应
													 */
													@Override
													public RES $rebound(REQ req, Object...args) throws EchoSoundException {
														return processor.$rebound(req, args);
													}
												}, args);//$NON-NLS-回声探测结束$
											}
											/**
											 * 幂等请求校验
											 */
											@Override
											public boolean check(String idempotentKEY) throws IdempotentException {
												return processor.check(idempotentKEY);
											}
											/**
											 * 获取幂等数据
											 */
											@Override
											public RES get(String idempotentKEY) throws IdempotentException {
												return processor.get(idempotentKEY);
											}
											/**
											 * 幂等持久化数据
											 */
											@Override
											public void storage(REQ req, RES res, Object...args) throws IdempotentException {
												processor.storage(req, res, args);
											}
										}, args);//$NON-NLS-幂等结束$
									}
									/**
									 * 降级mock
									 */
									@Override
									public RES mock(REQ req, Object...args) throws DegradeException {
										return processor.mock(req, args);
									}
									/**
									 * 业务降级
									 */
									@Override
									public RES bizProcessor(REQ req, Object...args) throws DegradeException {
										return processor.bizProcessor(req, args);
									}
								}, args);//$NON-NLS-服务降级结束$
							}
						}, args);//$NON-NLS-管道缩放结束$
					}
				}, args);//$NON-NLS-黑白名单结束$
			}
		}, args);//$NON-NLS-优雅停机结束$
	}
	
}
