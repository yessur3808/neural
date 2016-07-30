package cn.ms.neural.moduler.neure.handler.support;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.exception.HystrixBadRequestException;

import cn.ms.neural.common.exception.neure.NeureAlarmException;
import cn.ms.neural.common.exception.neure.NeureBreathException;
import cn.ms.neural.common.exception.neure.NeureException;
import cn.ms.neural.common.exception.neure.NeureFaultTolerantException;
import cn.ms.neural.moduler.neure.entity.NeureEntity;
import cn.ms.neural.moduler.neure.handler.INeureHandler;

/**
 * 神经元
 * <br>
 * @see 注意事项:每一次实例化只能使用一次
 * @author lry
 * @param <REQ> 请求对象
 * @param <RES> 响应对象
 */
public class NeureHandler<REQ, RES> extends HystrixCommand<RES> {

	private static final Logger logger=LogManager.getLogger(NeureHandler.class);
	
	private final REQ req;//请求对象
	private final Object[] args;//其他参数
	private final int maxRetryTimes;//最大重试次数(包括非重试次数)
	private final NeureEntity neureEntity;//配置信息
	private final INeureHandler<REQ, RES> handler;//路由器
	private final CountDownLatch retryTimes;// 需要重试次数
	
	private final Map<String, String> threadContextMap=ThreadContext.getContext();
	
	
	public NeureHandler(REQ req, NeureEntity neureEntity, INeureHandler<REQ, RES> handler, Object...args) {
		super(neureEntity.getSetter());
		this.req = req;
		this.neureEntity = neureEntity;
		this.handler = handler;
		this.args=args;
		this.maxRetryTimes=neureEntity.getRetryTimes()+1;
		this.retryTimes=new CountDownLatch(maxRetryTimes);
	}
	
	/**
	 * 依赖模块
	 */
	protected RES run() throws Exception {
		if(neureEntity.isThreadContextSwitch()){
			putThreadContext();//线程之间参数传递
		}
		RES res=null;
		
		try {
			while (retryTimes.getCount()>0) {
				long retryStart=System.currentTimeMillis();
				try {
					res = handler.route(req, args);
					return res;
				} catch (Throwable t) {
					logger.error("The run-route is failure, error is:"+t.getCause(), t);
					t.printStackTrace();
					
					try {
						handler.alarm(req, t, args);//告警
					} catch (Throwable th) {
						throw new NeureAlarmException(th.getMessage(), th);
					}
					
					//最后一次重试,则向外抛异常
					if(retryTimes.getCount()<2){
						throw new Throwable(t.getMessage(), t);
					}
				} finally {
					if(neureEntity.getRetryTimes()>0){//需要重试
						long nowExpend=System.currentTimeMillis()-retryStart;//计算本次重试耗时
						try {
							long breathTime=handler.breath(retryTimes.getCount(), nowExpend, maxRetryTimes, args);//计算慢性休眠时间
							if(retryTimes.getCount()>1){//非最后一次重试,都需要休眠;相反,最后一次重试则快速失败,需要要休眠
								Thread.sleep(breathTime);//休眠后重试						
							}
						} catch (Throwable t) {
							throw new NeureBreathException(t.getMessage(), t);
						}
					}
					
					retryTimes.countDown();//递减重试次数
				}
			}
		}catch(Throwable t){//捕获重试完成后抛出的异常
			if(neureEntity.isFallbackSwitch()){//需要降级
				throw new NeureException(t.getMessage(), t);
			}else{//不需要降级
				throw new HystrixBadRequestException(t.getMessage(), t);
			}
		} finally {
			if(neureEntity.isThreadContextSwitch()){
				ThreadContext.clearAll();
			}
		}
		
		return res;
	}

	/**
	 * 失败容错
	 */
	@Override
	protected RES getFallback() {
		try{
			if(neureEntity.isThreadContextSwitch()){
				putThreadContext();
			}
			
			//容错处理
			return handler.faulttolerant(req, args);
		}catch(Throwable t){
			throw new NeureFaultTolerantException(t.getMessage(), t);
		} finally {
			if(neureEntity.isThreadContextSwitch()){
				ThreadContext.clearAll();
			}
		}
	}
	
	private void putThreadContext() {
		if(threadContextMap!=null){
			if(!threadContextMap.isEmpty()){
				for (Map.Entry<String, String> entry:threadContextMap.entrySet()) {
					ThreadContext.put(entry.getKey(), entry.getValue());
				}
			}
		}
	}
	
}
