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
	 * 常规MOCK失败告警
	 */
	RUN_MOCK,
	
	/**
	 * 容错路由失败告警
	 */
	FALLBACK_ROUTE,
	
	/**
	 * 容错MOCK失败告警
	 */
	FALLBACK_MOCK,
	
	/**
	 * 容错呼吸失败告警
	 */
	BREATHCYCLE,
	
	/**
	 * 回调失败告警
	 */
	CALLBACK;
	
}
