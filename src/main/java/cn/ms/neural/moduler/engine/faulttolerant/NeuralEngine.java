package cn.ms.neural.moduler.engine.faulttolerant;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import com.netflix.hystrix.HystrixCommand;

import cn.ms.neural.common.SystemClock;
import cn.ms.neural.common.exception.NeuralException;
import cn.ms.neural.moduler.engine.faulttolerant.entity.CallChain;
import cn.ms.neural.moduler.engine.faulttolerant.type.AlarmType;
import cn.ms.neural.moduler.engine.faulttolerant.type.ExecuteType;
import cn.ms.neural.moduler.engine.faulttolerant.type.RouteMethod;

/**
 * 神经元
 * <br>
 * @see 注意事项:每一次实例化只能使用一次
 * @author lry
 * @param <REQ> 请求对象
 * @param <RES> 响应对象
 */
public class NeuralEngine<REQ, RES> extends HystrixCommand<RES> {

	private static final Logger logger=LogManager.getLogger(NeuralEngine.class);
	
	private final REQ req;//请求对象
	private final NeuralConf conf;//配置信息
	private final IRoute<REQ, RES> route;//路由器
	private final AtomicInteger retryTimes = new AtomicInteger();// 重试计数器
	private final ConcurrentLinkedQueue<CallChain> callChain=new ConcurrentLinkedQueue<CallChain>();//记录调用链
	
	private final Map<String, String> threadContextMap=ThreadContext.getContext();
	
	/**
	 * @param conf 神经元配置信息
	 * @param route 路由器
	 * @param req 请求对象
	 */
	public NeuralEngine(NeuralConf conf, IRoute<REQ, RES> route, REQ req) {
		super(conf.getSetter());
		this.req = req;
		this.conf = conf;
		this.route = route;
	}
	
	/**
	 * 获取调用链
	 * @return
	 */
	public ConcurrentLinkedQueue<CallChain> getCallChain() {
		return callChain;
	}

	/**
	 * 依赖模块
	 */
	protected RES run() throws Exception {
		if(conf.isThreadContextEnable()){
			putThreadContext();
		}
		long run_route=SystemClock.now();
		
		try {
			try {
				return route.route(conf.getNeuralId(), req);
			} finally {
				callChain.add(CallChain.build(RouteMethod.ROUTE, ExecuteType.RUN_ROUTE_SUCCESS, run_route));/**调用链接1-1**/
			}
		} catch (Throwable t) {
			callChain.add(CallChain.build(RouteMethod.ROUTE, ExecuteType.RUN_ROUTE_FAILURE, run_route));/**调用链接1-2**/
			logger.error("Run route is failure, error is ["+t.getMessage()+"]",t);
			t.printStackTrace();

			//失败通知
			doAlarm(ExecuteType.RUN_ROUTE_FAILURE, AlarmType.RUN_ROUTE, t);
			
			if (conf.getMaxRetryNum() < 1 && conf.getMaxRetryNum() != -1) {//不进入容错模式
				if (conf.isMockEnable()) {// 检查mock开关
					if(logger.isInfoEnabled()){
						logger.info("Run mockEnable is open, please waitting.. ");
					}
					
					long run_mock=SystemClock.now();
					try {
						try {
							return route.mock(conf.getNeuralId(), req);// 调用mock服务进行返回
						} finally {
							callChain.add(CallChain.build(RouteMethod.MOCK, ExecuteType.RUN_MOCK_SUCCESS, run_mock));/**调用链接2-1**/
						}
					} catch (Throwable e) {
						callChain.add(CallChain.build(RouteMethod.MOCK, ExecuteType.RUN_MOCK_FAILURE, run_mock));/**调用链接2-2**/
						logger.error("Run mock is failure, error is ["+e.getMessage()+"]",e);
						e.printStackTrace();
						
						//失败通知
						doAlarm(ExecuteType.RUN_MOCK_FAILURE, AlarmType.RUN_MOCK, e);
						throw new NeuralException(e);//必须向外抛出mock失败异常
					}
				}
			}
			throw new NeuralException(t);//进入容错重试,-1表示无限重试,且必须向外抛出route失败异常
		} finally {
			if(conf.isThreadContextEnable()){
				ThreadContext.clearAll();
			}
		}
	}

	/**
	 * 失败容错
	 */
	protected RES getFallback() {
		if(conf.isThreadContextEnable()){
			putThreadContext();
		}
		
		int retryFTNum = 0;// 已经重试容错次数
		long startTime=SystemClock.now();//记录容错重试开始时间
		
		try {
			if(conf.getMaxRetryNum()<1 && conf.getMaxRetryNum() != -1){
				throw new NeuralException("Run route is failure, and maxRetryNum="+conf.getMaxRetryNum(),getExecutionException());
			}
			
			while (retryFTNum < conf.getMaxRetryNum() || conf.getMaxRetryNum() == -1) {// 循环进入容错重试流
				long fallback_route = 0;
				try {
					retryFTNum = retryTimes.addAndGet(1);// 容错执行次数
					if(logger.isInfoEnabled()){
						logger.info("Is fallback route retry "+retryFTNum+" times, please waitting.. ");
					}
					
					breathCycle(retryFTNum, startTime);//休眠等待重试
					fallback_route=SystemClock.now();
					
					try {
						return route.route(conf.getNeuralId(), req);
					} finally {
						callChain.add(CallChain.build(RouteMethod.ROUTE, ExecuteType.FALLBACK_ROUTE_SUCCESS, fallback_route));/**调用链接3-1**/
					}
				} catch (Throwable t) {
					callChain.add(CallChain.build(RouteMethod.ROUTE, ExecuteType.FALLBACK_ROUTE_FAILURE, fallback_route));/**调用链接3-2**/
					logger.error("Fallback route is failure, error is ["+t.getMessage()+"]",t);
					t.printStackTrace();
				
					//失败通知
					doAlarm(ExecuteType.FALLBACK_ROUTE_FAILURE, AlarmType.FALLBACK_ROUTE, t);
						
					if (conf.getMaxRetryNum() == -1) {// 进入无限循环状态
						if(logger.isDebugEnabled()){
							logger.debug("The value of the current -1 is maxRetryNum, and the state of infinite loop retry is not recommended.");
						}
						continue;
					} else if (retryFTNum >= conf.getMaxRetryNum()) {// 重试完毕,则该退出了
						if(logger.isInfoEnabled()){
							logger.info("Fallback route retry already finished.");
						}
						if (conf.isMockEnable()) {// 检查mock开关
							if(logger.isInfoEnabled()){
								logger.info("Fallback mockEnable is open, please waitting.. ");
							}
							
							long fallback_mock=SystemClock.now();
							try {
								try {
									return route.mock(conf.getNeuralId(), req);// 调用mock服务进行返回
								} finally {
									callChain.add(CallChain.build(RouteMethod.MOCK, ExecuteType.FALLBACK_MOCK_SUCCESS, fallback_mock));/**调用链接4-1**/
								}
							} catch (Throwable e) {
								callChain.add(CallChain.build(RouteMethod.MOCK, ExecuteType.FALLBACK_MOCK_FAILURE, fallback_mock));/**调用链接4-2**/
								logger.error("Fallback mock is failure, error is ["+e.getMessage()+"]",e);
								e.printStackTrace();
								
								//失败通知
								doAlarm(ExecuteType.FALLBACK_MOCK_FAILURE, AlarmType.FALLBACK_MOCK, e);
								throw new NeuralException(e);
							}
						} else {
							if(logger.isInfoEnabled()){
								logger.info("Fallback mockEnable is close.");
							}
						}
						throw new NeuralException(t);
					}
				}
			}
			return null;
		} finally {
			if(conf.isThreadContextEnable()){
				ThreadContext.clearAll();
			}
		}
	}
	
	public void doAlarm(ExecuteType executeType, AlarmType alarmType,Throwable t) {
		long run_failNotify=SystemClock.now();
		try {
			route.alarm(conf.getNeuralId(), executeType, req, t);
			callChain.add(CallChain.build(RouteMethod.ALARM, ExecuteType.ALARM_SUCCESS, run_failNotify));/**调用链接6-1**/
		} catch (Throwable fn) {
			callChain.add(CallChain.build(RouteMethod.ALARM, ExecuteType.ALARM_FAILURE, run_failNotify));/**调用链接6-2**/
			logger.error("The '%s' alarm is fail, error is: %s.", executeType ,fn);
			fn.printStackTrace();
		}
	}
	
	private void breathCycle(int retryFTNum, long startTime) {
		long fallback_breathCycle=SystemClock.now();
		try {//呼吸周期计算并呼吸
			long sleepTime = route.breathCycle(conf.getNeuralId(), retryFTNum,
					conf.getMaxRetryNum(), conf.getRetryCycle(), SystemClock.now()-startTime);// 计算本次容错休眠时间
			callChain.add(CallChain.build(RouteMethod.BREATHCYCLE, ExecuteType.BREATHCYCLE_SUCCESS, fallback_breathCycle));/**调用链接5-1**/
			if (sleepTime > 0) {
				Thread.sleep(sleepTime);
			}else{
				if(logger.isDebugEnabled()){
					logger.debug("The breathCycle less than 1ms, recommended a greater than 0");
				}
			}					
		} catch (Throwable t) {
			callChain.add(CallChain.build(RouteMethod.BREATHCYCLE, ExecuteType.BREATHCYCLE_FAILURE, fallback_breathCycle));/**调用链接5-2**/
			logger.error("Fallback breathCycle is failure, error is ["+t.getMessage()+"]",t);
			t.printStackTrace();
			
			//失败通知
			doAlarm(ExecuteType.BREATHCYCLE_FAILURE, AlarmType.BREATHCYCLE, t);
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
