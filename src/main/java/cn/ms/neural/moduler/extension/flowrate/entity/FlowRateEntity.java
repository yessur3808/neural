package cn.ms.neural.moduler.extension.flowrate.entity;

import cn.ms.neural.moduler.extension.flowrate.type.FlowRateType;

/**
 * 流量控制
 * <br>
 * [流控类型]:[流控子开关]:[许可数]:[热身期]:[failfast]:[超时时间]:[资源KEY]
 * @author lry
 * @version v1.0
 */
public class FlowRateEntity {

	/**
	 * 流控类型
	 */
	FlowRateType flowRateType;
	/**
	 * 流控子开关
	 */
	boolean subswitch;
	/**
	 * 许可数
	 */
	int permits;
	/**
	 * 热身期
	 */
	double warmup;
	/**
	 * 是否快速获取结果
	 */
	boolean acquire;
	/**
	 * 获取许可超时时间ms
	 */
	long timeout;
	/**
	 * 流控资源KEY
	 */
	String frkey;
	
	public FlowRateType getFlowRateType() {
		return flowRateType;
	}
	public void setFlowRateType(FlowRateType flowRateType) {
		this.flowRateType = flowRateType;
	}
	public boolean isSubswitch() {
		return subswitch;
	}
	public void setSubswitch(boolean subswitch) {
		this.subswitch = subswitch;
	}
	public int getPermits() {
		return permits;
	}
	public void setPermits(int permits) {
		this.permits = permits;
	}
	public double getWarmup() {
		return warmup;
	}
	public void setWarmup(double warmup) {
		this.warmup = warmup;
	}
	public boolean isAcquire() {
		return acquire;
	}
	public void setAcquire(boolean acquire) {
		this.acquire = acquire;
	}
	public long getTimeout() {
		return timeout;
	}
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	public String getFrkey() {
		return frkey;
	}
	public void setFrkey(String frkey) {
		this.frkey = frkey;
	}
	
}
