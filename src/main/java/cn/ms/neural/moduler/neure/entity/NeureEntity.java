package cn.ms.neural.moduler.neure.entity;

import com.netflix.hystrix.HystrixCommand.Setter;

import cn.ms.neural.moduler.neure.support.HystrixSetterSupport;

/**
 * 模块配置
 * 
 * @author lry
 */
public class NeureEntity {

	/**
	 * 容错配置
	 */
	private Setter setter=HystrixSetterSupport.buildSetter();
	
	/**
	 * Log4j threadContext Switch
	 */
	private boolean threadContextSwitch=true;
	/**
	 * 降级开关
	 */
	private boolean fallbackSwitch=true;
	
	/**
	 * 重试次数
	 */
	private int retryTimes=0;
	
	
	public Setter getSetter() {
		return setter;
	}
	public void setSetter(Setter setter) {
		this.setter = setter;
	}
	public boolean isThreadContextSwitch() {
		return threadContextSwitch;
	}
	public void setThreadContextSwitch(boolean threadContextSwitch) {
		this.threadContextSwitch = threadContextSwitch;
	}
	public boolean isFallbackSwitch() {
		return fallbackSwitch;
	}
	public void setFallbackSwitch(boolean fallbackSwitch) {
		this.fallbackSwitch = fallbackSwitch;
	}
	public int getRetryTimes() {
		return retryTimes;
	}
	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}
	
}
