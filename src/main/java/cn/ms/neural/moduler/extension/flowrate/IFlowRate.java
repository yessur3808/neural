package cn.ms.neural.moduler.extension.flowrate;

import java.util.List;

import cn.ms.neural.common.exception.flowrate.FlowrateException;
import cn.ms.neural.moduler.IModuler;
import cn.ms.neural.moduler.extension.flowrate.entity.FlowRateData;
import cn.ms.neural.moduler.extension.flowrate.processor.IFlowRateProcessor;

/**
 * 流量控制
 * 
 * @author lry
 */
public interface IFlowRate<REQ, RES> extends IModuler<REQ, RES> {

	/**
	 * 流量控制器
	 * <br>
	 * 第一步:并发流控<br>
	 * 第二步:QPS流控<br>
	 * @param req
	 * @param flowRateDatas
	 * @param processor
	 * @param args
	 * @return
	 * @throws FlowrateException
	 */
	RES flowrate(REQ req, List<FlowRateData> flowRateDatas, IFlowRateProcessor<REQ, RES> processor, Object... args) throws FlowrateException;
	
}