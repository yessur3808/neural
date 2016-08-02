package cn.ms.neural.moduler.neure;

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
public class Neuron<REQ, RES> extends HystrixCommand<RES> {

	private static final Logger logger=LogManager.getLogger(Neuron.class);
	
	private final REQ req;//请求对象
	private final Object[] args;//其他参数
	private final int maxExecuteTimes;//最大执行次数(重试次数+默认执行次数)
	private final NeureEntity neureEntity;//配置信息
	private final INeureHandler<REQ, RES> handler;//路由器
	private final CountDownLatch retryExecuteTimes;// 需要重试次数
	
	//线程参数:用于记录当前线程中的参数，以便于传递至其他线程中
	private final Map<String, String> threadContextMap=ThreadContext.getContext();
	
	public Neuron(REQ req, NeureEntity neureEntity, INeureHandler<REQ, RES> handler, Object...args) {
		super(neureEntity.getSetter());
		this.req = req;
		this.neureEntity = neureEntity;
		this.handler = handler;
		this.args=args;
		this.maxExecuteTimes=neureEntity.getMaxRetryTimes()+1;
		this.retryExecuteTimes=new CountDownLatch(maxExecuteTimes);
	}
	
	/**
	 * 执行器
	 */
	protected RES run() throws Exception {
		RES res=null;
		try {
			if(neureEntity.isThreadContext()){
				putThreadContextParameters();//线程之间参数传递
			}
			
			while (retryExecuteTimes.getCount()>0) {
				long retryStartTime=System.currentTimeMillis();
				try {
					return handler.route(req, args);//执行route
				} catch (Throwable t) {
					logger.error("The run-route is failure, error is:"+t.getCause(), t);
					t.printStackTrace();
					
					try {
						handler.alarm(req, t, args);//告警
					} catch (Throwable th) {
						throw new NeureAlarmException(th.getMessage(), th);
					}
					
					//最后一次重试,则向外抛异常
					if(retryExecuteTimes.getCount()<2){
						throw new Throwable(t.getMessage(), t);
					}
				} finally {
					if(neureEntity.getMaxRetryTimes()>0){//需要重试
						long nowExpend=System.currentTimeMillis()-retryStartTime;//计算本次重试耗时
						try {
							long breathTime=handler.breath(retryExecuteTimes.getCount(), nowExpend, maxExecuteTimes, args);//计算慢性休眠时间
							if(retryExecuteTimes.getCount()>1){//非最后一次重试,都需要休眠;相反,最后一次重试则快速失败,不需要休眠
								Thread.sleep(breathTime);//休眠后重试						
							}
						} catch (Throwable t) {
							throw new NeureBreathException(t.getMessage(), t);
						}
					}
					
					retryExecuteTimes.countDown();//递减重试次数
				}
			}
		}catch(Throwable t){//捕获重试完成后抛出的异常
			if(neureEntity.isFallback()){//需要降级
				throw new NeureException(t.getMessage(), t);
			}else{//不需要降级
				throw new HystrixBadRequestException(t.getMessage(), t);
			}
		} finally {
			if(neureEntity.isThreadContext()){
				ThreadContext.clearAll();//清理本次传递数据
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
			if(neureEntity.isThreadContext()){
				putThreadContextParameters();
			}
			
			//容错处理
			return handler.faulttolerant(req, args);
		}catch(Throwable t){
			throw new NeureFaultTolerantException(t.getMessage(), t);
		} finally {
			if(neureEntity.isThreadContext()){
				ThreadContext.clearAll();
			}
		}
	}
	
	/**
	 * 参数注入线程
	 */
	private void putThreadContextParameters() {
		if(threadContextMap!=null){
			if(!threadContextMap.isEmpty()){
				for (Map.Entry<String, String> entry:threadContextMap.entrySet()) {
					ThreadContext.put(entry.getKey(), entry.getValue());
				}
			}
		}
	}
	
}
