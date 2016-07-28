package cn.ms.neural.moduler.flowrate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import cn.ms.neural.common.exception.ProcessorException;
import cn.ms.neural.moduler.extension.flowrate.core.ratelimiter.NIORateLimiterFlowrate;
import cn.ms.neural.moduler.extension.flowrate.core.semaphore.NIOSemaphoreFlowrate;
import cn.ms.neural.moduler.extension.flowrate.entity.FlowrateRule;
import cn.ms.neural.moduler.extension.flowrate.processor.IFlowRateProcessor;
import cn.ms.neural.moduler.extension.flowrate.type.FlowRateType;

public class FlowrateTest {

	public static String KEY = "service_id_001";
	public static String DATA = "并发测试报文";
	
	/**
	 * 流速QPS测试
	 */
	@Test
	public void limiterFlowrate() {
		FlowrateRule flowrateRule = new FlowrateRule(KEY, FlowRateType.QPS, "MILLISECONDS", 5, 2);
		NIORateLimiterFlowrate<String, String> flowrate=new NIORateLimiterFlowrate<>(flowrateRule);
		try {
			for (int i = 0; i < 20; i++) {
				boolean rs=flowrate.rateLimiter();
				if(i%4==0){
					Assert.assertTrue(rs);
				}else {
					Assert.assertTrue(!rs);
				}
				Thread.sleep(50);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 并发测试
	 */
	@Test
	public void semaphoreFlowrate() {
		final FlowrateRule flowrateRule = new FlowrateRule(KEY, FlowRateType.CCT, "MILLISECONDS", 10, true, 0);
		final NIOSemaphoreFlowrate<String, String> flowrate=new NIOSemaphoreFlowrate<>(flowrateRule);
		
		try {
			ExecutorService exec = Executors.newFixedThreadPool(10);
			for (int i = 0; i < 50; i++) {
				Runnable rb = new Runnable() {  
	                public void run() {  
	                    try { 
	                    	flowrate.semaphore(null, DATA, new IFlowRateProcessor<String, String>(){
								@Override
								public String processor(String req, Object... args) throws ProcessorException {
									return req;
								}
	                    	}); // 获取许可
	                    	Assert.assertTrue(true);
	                    } catch (Throwable e) {
	                    	Assert.assertTrue(false);
	                    }  
	                }  
	            };  
	            exec.execute(rb);  
			}
			
		    exec.shutdown();
		    exec.awaitTermination(10, TimeUnit.SECONDS);
			
			// 退出线程池  
	        exec.shutdown();  
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 并发-流速测试
	 */
	@Test
    public void flowrate() {
		final FlowrateRule flowrateRule = new FlowrateRule(KEY, FlowRateType.CCT, "MILLISECONDS", 5, true, 0);//运行10个并发,等待0ms
		final NIOSemaphoreFlowrate<String, String> flowrate=new NIOSemaphoreFlowrate<>(flowrateRule);
		final FlowrateRule flowrateRule1 = new FlowrateRule(KEY, FlowRateType.QPS, "MILLISECONDS", 10000, 2);//每秒钟允许提交3534个任务,在2ms内稳定增长速率
		final NIORateLimiterFlowrate<String, String> flowrate1=new NIORateLimiterFlowrate<>(flowrateRule1);
		
		try {
			ExecutorService exec = Executors.newFixedThreadPool(5);//模拟5个并发
			for (int i = 0; i < 50; i++) {
				Runnable rb = new Runnable() {  
	                public void run() {  
	                	try{
	                    	String res = flowrate.semaphore(null, DATA, new IFlowRateProcessor<String, String>(){//并发控制
								@Override
								public String processor(String req, Object... args) throws ProcessorException {
									try {
										boolean rs = flowrate1.rateLimiter();//流速控制
										System.out.println("*********QPS允许状态:"+rs);
										Thread.sleep(100);
										Assert.assertTrue(true);
									} catch (Throwable e) {
										e.printStackTrace();
									}
	                    			return req;
								}
	                    	});
	                    	Assert.assertEquals(DATA, res);
	                    } catch (Throwable e) {
	                    	Assert.assertTrue(false);
	                    }  
	                }  
	            };  
	            exec.execute(rb);  
			}
			
		    exec.shutdown();
		    exec.awaitTermination(10, TimeUnit.SECONDS);
			
			// 退出线程池  
	        exec.shutdown();  
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
}
