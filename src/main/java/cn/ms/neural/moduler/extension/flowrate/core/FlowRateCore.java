package cn.ms.neural.moduler.extension.flowrate.core;

import cn.ms.neural.common.concurrent.LongAdder;
import cn.ms.neural.common.exception.ProcessorException;
import cn.ms.neural.processor.IProcessor;

/**
 * 流量控制
 * 
 * @author lry
 * @version v1.0
 */
public class FlowRateCore<REQ, RES> {
	
	LongAdder longAdder = new LongAdder();
	
	/**
	 * @param apiId 接口ID
	 * @param userId 用户ID
	 * @param appId 应用ID
	 * @param processor
	 */
	public void handler(String apiId, String userId, String appId, IProcessor<REQ, RES> processor) {
		
	}
	
	public static void main(String[] args) {
		FlowRateCore<String, String> core=new FlowRateCore<>();
		core.handler("queryName","user001","app001",new IProcessor<String, String>() {
			@Override
			public String processor(String req, Object... args) throws ProcessorException {
				return "这是响应报文";
			}
		});
	}
	
}
