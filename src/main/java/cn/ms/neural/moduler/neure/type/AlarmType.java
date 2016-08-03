package cn.ms.neural.moduler.neure.type;

/**
 * 告警来源类型
 * 
 * @author lry
 */
public enum AlarmType {

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
