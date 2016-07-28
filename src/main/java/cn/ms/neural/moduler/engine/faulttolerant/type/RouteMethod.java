package cn.ms.neural.moduler.engine.faulttolerant.type;

/**
 * 路由方法
 * 
 * @author lry
 */
public enum RouteMethod {

	/**
	 * 路由服务
	 */
	ROUTE,

	/**
	 * MOCK服务
	 */
	MOCK,

	/**
	 * 呼吸服务
	 */
	BREATHCYCLE,

	/**
	 * 回调服务
	 */
	CALLBACK,

	/**
	 * 失败告警服务
	 */
	ALARM;

}
