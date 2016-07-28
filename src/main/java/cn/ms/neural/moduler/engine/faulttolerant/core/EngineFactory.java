package cn.ms.neural.moduler.engine.faulttolerant.core;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.netflix.hystrix.HystrixCommandMetrics;

import cn.ms.neural.common.SystemClock;
import cn.ms.neural.common.exception.NeuralException;
import cn.ms.neural.moduler.engine.faulttolerant.IRoute;
import cn.ms.neural.moduler.engine.faulttolerant.NeuralConf;
import cn.ms.neural.moduler.engine.faulttolerant.NeuralEngine;
import cn.ms.neural.moduler.engine.faulttolerant.entity.CallChain;
import cn.ms.neural.moduler.engine.faulttolerant.entity.Expend;
import cn.ms.neural.moduler.engine.faulttolerant.type.AlarmType;
import cn.ms.neural.moduler.engine.faulttolerant.type.ExecuteType;
import cn.ms.neural.moduler.engine.faulttolerant.type.RouteMethod;

/**
 * 神经元处理中心

 * @author lry
 * @param <REQ>
 * @param <RES>
 */
public class EngineFactory<REQ, RES> {

	private static final Logger logger=LogManager.getLogger(EngineFactory.class);

	/**统计数据**/
	//$NON-NLS-整体成功率、失败率$
	//$NON-NLS-route/mock...成功率、失败率$
	
	//private ConcurrentHashMap<String, AtomicLong> rate=new ConcurrentHashMap<String, AtomicLong>();
	
	/**
	 * 调用链
	 */
	private ConcurrentHashMap<String, AtomicLong> callChainMap=new ConcurrentHashMap<String, AtomicLong>();
	
	
	public ConcurrentHashMap<String, AtomicLong> getCallChainMap() {
		return callChainMap;
	}
	
	public void init() throws Throwable {
		
	}

	/**
	 * 处理中心
	 * 
	 * @param conf
	 * @param route
	 * @param req
	 * @return
	 */
	public RES engine(NeuralConf conf, IRoute<REQ, RES> route, REQ req) throws Throwable {
		RES res=null;
		Expend expend=null;
		long startTime=0;
		NeuralEngine<REQ, RES> neural=null;
		HystrixCommandMetrics metrics=null;
		ConcurrentLinkedQueue<CallChain> callChain=null;
		try {
			neural=new NeuralEngine<REQ, RES>(conf, route, req);
			startTime=SystemClock.now();//总时间备忘录
			res=neural.execute();//执行神经元
			
			//后续处理
			afterHandler(conf, route, req, res, expend, startTime, neural, metrics, callChain);
			
			return res;
		} catch (Throwable t) {//后续处理
			afterHandler(conf, route, req, res, expend, startTime, neural, metrics, callChain);
			logger.error("NeuralHandler is failure, error is: "+t.getMessage(),t);
			t.printStackTrace();
			throw new NeuralException("NeuralHandler is failure, error is: "+t.getMessage(),t);
		}
	}
	
	/**
	 * 后续处理中心(如异步回调、监控统计等)
	 */
	private RES afterHandler(NeuralConf conf, IRoute<REQ, RES> route, REQ req, RES res, Expend expend, long startTime,
			NeuralEngine<REQ, RES> neural, HystrixCommandMetrics metrics, ConcurrentLinkedQueue<CallChain> callChain) {
		//获取调用链
		callChain=new ConcurrentLinkedQueue<CallChain>();
		callChain.addAll(neural.getCallChain());

		if(conf.isCallbackEnable()){//异步回调响应,如果异步回调开关打开
			if(logger.isInfoEnabled()){
				logger.info("Run callbackEnable is open, is callback.. ");
			}
			
			long callback=SystemClock.now();
			try {
				route.callback(conf.getNeuralId(), neural.getCallChain().poll().getExecuteType(), res);
				callChain.add(CallChain.build(RouteMethod.CALLBACK, ExecuteType.CALLBACK_SUCCESS, callback));/**调用链接7-1**/
			} catch (Throwable t) {
				t.printStackTrace();
				callChain.add(CallChain.build(RouteMethod.CALLBACK, ExecuteType.CALLBACK_FAILURE, callback));/**调用链接7-2**/
				
				//回调失败告警
				neural.doAlarm(ExecuteType.CALLBACK_FAILURE, AlarmType.CALLBACK, t);
				throw new NeuralException("NeuralHandler's route callback is failure, error is "+t.getMessage(),t);
			}
		}
		long allExpend=SystemClock.now()-startTime;//计算总耗时指标
		
		expend=new Expend();
		//分别计算指标之和
		Iterator<CallChain> iterator=callChain.iterator();
		while (iterator.hasNext()) {
			CallChain cc=iterator.next();
			if(cc.getRouteMethod()==RouteMethod.ROUTE){
				expend.setRoute(new AtomicLong(expend.getRoute().addAndGet(cc.getExpend().get())));
			}else if(cc.getRouteMethod()==RouteMethod.MOCK){
				expend.setMock(new AtomicLong(expend.getMock().addAndGet(cc.getExpend().get())));
			}else if(cc.getRouteMethod()==RouteMethod.BREATHCYCLE){
				expend.setBreathCycle(new AtomicLong(expend.getBreathCycle().addAndGet(cc.getExpend().get())));
			}else if(cc.getRouteMethod()==RouteMethod.CALLBACK){
				expend.setCallback(new AtomicLong(expend.getCallback().addAndGet(cc.getExpend().get())));
			}else if(cc.getRouteMethod()==RouteMethod.ALARM){
				expend.setFailNotify(new AtomicLong(expend.getFailNotify().addAndGet(cc.getExpend().get())));
			}
		}
		
		expend.setExpend(new AtomicLong(allExpend));
		metrics=neural.getMetrics();
		
		//$NON-NLS-后续统计$
//		System.out.println("Neural监控指标结果:"+expend);
//		System.out.println("Hystrix监控指标结果:"+JSON.toJSONString(metrics));
//		System.out.println("Neural调用链("+callChain.size()+"):"+callChain);
		
		return res;
	}

	public void destory() throws Throwable {
		
	}

}
