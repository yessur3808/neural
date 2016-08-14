package cn.ms.neural.moduler.extension.pipescaling.processor;

import cn.ms.neural.moduler.senior.alarm.processor.IAlarmTypeProcessor;
import cn.ms.neural.processor.IProcessor;

public interface IPipeScalingProcessor<REQ, RES> extends IProcessor<REQ, RES>, IAlarmTypeProcessor<REQ, RES> {

}
