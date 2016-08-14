package cn.ms.neural.moduler.extension.blackwhite.processor;

import cn.ms.neural.alarm.IAlarmType;
import cn.ms.neural.processor.IProcessor;

public interface IBlackWhiteProcessor<REQ, RES> extends IProcessor<REQ, RES>, IAlarmType<REQ, RES> {

}
