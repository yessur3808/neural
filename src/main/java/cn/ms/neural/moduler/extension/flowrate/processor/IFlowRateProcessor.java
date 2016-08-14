package cn.ms.neural.moduler.extension.flowrate.processor;

import cn.ms.neural.alarm.IAlarmType;
import cn.ms.neural.processor.IProcessor;

public interface IFlowRateProcessor<REQ, RES> extends IProcessor<REQ, RES>, IAlarmType<REQ, RES> {
	
}