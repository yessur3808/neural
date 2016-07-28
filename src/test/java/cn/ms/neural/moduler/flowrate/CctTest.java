package cn.ms.neural.moduler.flowrate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import cn.ms.neural.common.URL;
import cn.ms.neural.common.exception.ProcessorException;
import cn.ms.neural.common.spi.ExtensionLoader;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.extension.flowrate.IFlowRate;
import cn.ms.neural.moduler.extension.flowrate.entity.FlowRateData;
import cn.ms.neural.moduler.extension.flowrate.processor.IFlowRateProcessor;

public class CctTest {

	@SuppressWarnings("unchecked")
	IFlowRate<String, String> flowRate=ExtensionLoader.getExtensionLoader(IFlowRate.class).getAdaptiveExtension();
	
	/**
	 * 并发测试
	 */
	@Test
	public void semaphoreFlowrate() {
		try {
			//[流控类型]:[流控子开关]:[许可数]:[热身期]:[failfast]:[超时时间]:[资源KEY]
			Moduler<String, String> moduler=new Moduler<String, String>();
			moduler.setUrl(URL.valueOf("http://127.0.0.1:8080/fr/?switch=true&cctswitch=true&flowrate.list=CCT:true:5:0:true:5000:KEY1"));
			
			flowRate.setModuler(moduler);
			final List<FlowRateData> flowRateKVData=new ArrayList<FlowRateData>();
			flowRateKVData.add(new FlowRateData());
			ExecutorService exec = Executors.newFixedThreadPool(10);
			for (int i = 0; i < 50; i++) {
				Runnable rb = new Runnable() {  
	                public void run() {  
	                    try { 
	                    	String res=flowRate.flowrate("请求报文", flowRateKVData, new IFlowRateProcessor<String, String>() {
								@Override
								public String processor(String req, Object... args) throws ProcessorException {
									return "响应报文";
								}
							});
	                    	System.out.println("-->"+res);
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
	
}
