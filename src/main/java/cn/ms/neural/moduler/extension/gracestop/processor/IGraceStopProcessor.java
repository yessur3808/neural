package cn.ms.neural.moduler.extension.gracestop.processor;

import cn.ms.neural.moduler.senior.alarm.IAlarmType;
import cn.ms.neural.processor.IProcessor;

/**
 * 优雅停机处理器
 * 
 * @author lry
 *
 * @param <REQ>
 * @param <RES>
 */
public interface IGraceStopProcessor<REQ, RES> extends IProcessor<REQ, RES>, IAlarmType<REQ, RES> {
	
}
