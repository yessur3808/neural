package cn.ms.neural.moduler.senior.alarm;

public enum AlarmModulerType {

	/**
	 * 优雅停机
	 */
	GraceStop,
	/**
	 * 黑白名单
	 */
	BlackWhite,
	/**
	 * 管道缩放
	 */
	PipeScaling,
	/**
	 * 流量控制
	 */
	FlowRate,
	/**
	 * 服务降级
	 */
	Degrade,
	/**
	 * 幂等模块
	 */
	Idempotent,
	/**
	 * 回声探测
	 */
	EchoSound,
	/**
	 * 容错内核
	 */
	Neure;
	
}
