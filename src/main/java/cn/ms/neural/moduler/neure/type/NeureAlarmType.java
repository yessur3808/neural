package cn.ms.neural.moduler.neure.type;

import cn.ms.neural.moduler.senior.alarm.IAlarmType;

/**
 * 告警来源类型
 * 
 * @author lry
 */
public enum NeureAlarmType implements IAlarmType {

	/**
	 * 常规路由失败告警
	 */
	RUN_ROUTE,
	
	/**
	 * 呼吸失败告警
	 */
	RUN_BREATH,
	
	/**
	 * Fault-Tolerant失败告警
	 */
	FALLBACK_FAULT_TOLERANT,
	
	/**
	 * 回调失败告警
	 */
	CALLBACK;
	
}
