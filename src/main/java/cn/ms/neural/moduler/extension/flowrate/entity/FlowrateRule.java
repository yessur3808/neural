package cn.ms.neural.moduler.extension.flowrate.entity;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import cn.ms.neural.moduler.extension.flowrate.type.FlowRateType;

/**
 * 流量控制规则实体
 * @author lry
 */
public class FlowrateRule implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//$NON-NLS-公共$
	private String key;
	private FlowRateType flowrateType;
	/**
	 * unit the time unit of the warmupPeriod argument
	 */
	private String timeUnit=TimeUnit.MILLISECONDS.toString();
		
	//$NON-NLS-流速控制$
	/**
	 * permitsPerSecond the rate of the returned RateLimiter, measured in how many permits become available per second
	 */
	private double permitsPerSecond=0;
	/**
	 * warmupPeriod the duration of the period where the RateLimiter ramps up its rate, before reaching its stable (maximum) rate
	 */
	private long warmupPeriod=1000*10;
	
	//$NON-NLS-并发控制$
	/**
	 * permits the initial number of permits available. This value may be negative, in which case releases must occur before any acquires will be granted.
	 */
	private int permits=100;
	/**
	 * fair true if this semaphore will guarantee first-in first-out granting of permits under contention, else false
	 */
	private boolean fair=true;
	/**
	 * timeout the maximum time to wait for a permit
	 */
	private long timeout=0;
	
	public FlowrateRule() {
	}
	
	/**
	 * 
	 * @param key
	 * @param flowrateType 
	 * @param timeUnit
	 * @param permitsPerSecond QPS
	 * @param warmupPeriod
	 */
	public FlowrateRule(String key, FlowRateType flowrateType, String timeUnit, double permitsPerSecond, long warmupPeriod) {
		this.key = key;
		this.flowrateType = flowrateType;
		this.timeUnit = timeUnit;
		this.permitsPerSecond = permitsPerSecond;
		this.warmupPeriod = warmupPeriod;
	}
	
	public FlowrateRule(String key, FlowRateType flowrateType, String timeUnit, int permits, boolean fair, long timeout) {
		this.key = key;
		this.flowrateType = flowrateType;
		this.timeUnit = timeUnit;
		this.permits = permits;
		this.fair = fair;
		this.timeout = timeout;
	}
	
	public FlowrateRule(String key, FlowRateType flowrateType, String timeUnit, double permitsPerSecond, long warmupPeriod,
			int permits, boolean fair, long timeout) {
		this.key = key;
		this.flowrateType = flowrateType;
		this.timeUnit = timeUnit;
		this.permitsPerSecond = permitsPerSecond;
		this.warmupPeriod = warmupPeriod;
		this.permits = permits;
		this.fair = fair;
		this.timeout = timeout;
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public FlowRateType getFlowrateType() {
		return flowrateType;
	}
	public void setFlowrateType(FlowRateType flowrateType) {
		this.flowrateType = flowrateType;
	}
	public String getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}
	public double getPermitsPerSecond() {
		return permitsPerSecond;
	}
	public void setPermitsPerSecond(double permitsPerSecond) {
		this.permitsPerSecond = permitsPerSecond;
	}
	public long getWarmupPeriod() {
		return warmupPeriod;
	}
	public void setWarmupPeriod(long warmupPeriod) {
		this.warmupPeriod = warmupPeriod;
	}
	public int getPermits() {
		return permits;
	}
	public void setPermits(int permits) {
		this.permits = permits;
	}
	public boolean isFair() {
		return fair;
	}
	public void setFair(boolean fair) {
		this.fair = fair;
	}
	public long getTimeout() {
		return timeout;
	}
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public String toString() {
		return "FlowrateEntity [key=" + key + ", flowrateType=" + flowrateType + ", timeUnit=" + timeUnit
				+ ", permitsPerSecond=" + permitsPerSecond + ", warmupPeriod=" + warmupPeriod + ", permits=" + permits
				+ ", fair=" + fair + ", timeout=" + timeout + "]";
	}
	
}
