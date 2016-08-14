package cn.ms.neural.moduler.extension.flowrate.processor;

import cn.ms.neural.moduler.senior.alarm.processor.IAlarmTypeProcessor;
import cn.ms.neural.processor.IProcessor;

public interface IFlowRateProcessor<REQ, RES> extends IProcessor<REQ, RES>, IAlarmTypeProcessor<REQ, RES> {
	
}