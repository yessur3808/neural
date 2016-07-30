package cn.ms.neural.moduler.neure.entity;

public class HystrixSetter {

	private String commandGroupKey="neural-command-group-key";//Command分组KEY
	private String commandKey="neural-command-key";//Command KEY
	
	private int executionIsolationThreadTimeoutInMilliseconds=1000;//执行隔离线程超时毫秒,默认为1000ms
	private int executionTimeoutInMilliseconds=1000;//执行超时时间,默认为1000ms

	//调用线程允许请求HystrixCommand.GetFallback()的最大数量，默认10。超出时将会有异常抛出，注意：该项配置对于THREAD隔离模式也起作用
	private int fallbackIsolationSemaphoreMaxConcurrentRequests=10;
	
	////$NON-NLS-断路器$
	private int circuitBreakerRequestVolumeThreshold=20;//当在配置时间窗口内达到此数量的失败后,进行短路,默认20个
	private int circuitBreakerSleepWindowInMilliseconds=5;//短路多久以后开始尝试是否恢复,默认5s
	private int circuitBreakerErrorThresholdPercentage=50;//出错百分比阈值,当达到此阈值后,开始短路,默认50%
	
	////$NON-NLS-线程池KEY$
	private String threadPoolKey="neural-threadpool-key";//线程池KEY
	private int threadPoolCoreSize=10;//线程池设置:线程池核心线程数,默认为10
	private int threadPoolQueueSizeRejectionThreshold=5;//排队线程数量阈值，默认为5，达到时拒绝，如果配置了该选项，队列的大小是该队列
	
	//$NON-NLS-默认参数$
	/**
	 * 标准版配置
	 * @return
	 */
	public static HystrixSetter build() {
		return new HystrixSetter();
	}
	/**
	 * 较高版配置
	 * @return
	 */
	public static HystrixSetter buildSenior() {
		return new HystrixSetter(80000, 80000, 30, 50, 50, 10);
	}
	/**
	 * 高级版配置
	 * @return
	 */
	public static HystrixSetter buildSuper() {
		return new HystrixSetter(80000, 80000, 50, 50, 100, 15);
	}
	
	public HystrixSetter() {
	}
	public HystrixSetter(int executionIsolationThreadTimeoutInMilliseconds, int executionTimeoutInMilliseconds, 
			int circuitBreakerRequestVolumeThreshold, int circuitBreakerErrorThresholdPercentage, 
			int threadPoolCoreSize, int threadPoolQueueSizeRejectionThreshold) {
		this.executionIsolationThreadTimeoutInMilliseconds = executionIsolationThreadTimeoutInMilliseconds;
		this.executionTimeoutInMilliseconds = executionTimeoutInMilliseconds;
		this.circuitBreakerRequestVolumeThreshold = circuitBreakerRequestVolumeThreshold;
		this.circuitBreakerErrorThresholdPercentage = circuitBreakerErrorThresholdPercentage;
		this.threadPoolCoreSize = threadPoolCoreSize;
		this.threadPoolQueueSizeRejectionThreshold = threadPoolQueueSizeRejectionThreshold;
	}
	
	
	public String getCommandGroupKey() {
		return commandGroupKey;
	}
	public void setCommandGroupKey(String commandGroupKey) {
		this.commandGroupKey = commandGroupKey;
	}
	public String getCommandKey() {
		return commandKey;
	}
	public void setCommandKey(String commandKey) {
		this.commandKey = commandKey;
	}
	public int getExecutionIsolationThreadTimeoutInMilliseconds() {
		return executionIsolationThreadTimeoutInMilliseconds;
	}
	public void setExecutionIsolationThreadTimeoutInMilliseconds(int executionIsolationThreadTimeoutInMilliseconds) {
		this.executionIsolationThreadTimeoutInMilliseconds = executionIsolationThreadTimeoutInMilliseconds;
	}
	public int getExecutionTimeoutInMilliseconds() {
		return executionTimeoutInMilliseconds;
	}
	public void setExecutionTimeoutInMilliseconds(int executionTimeoutInMilliseconds) {
		this.executionTimeoutInMilliseconds = executionTimeoutInMilliseconds;
	}
	public int getFallbackIsolationSemaphoreMaxConcurrentRequests() {
		return fallbackIsolationSemaphoreMaxConcurrentRequests;
	}
	public void setFallbackIsolationSemaphoreMaxConcurrentRequests(int fallbackIsolationSemaphoreMaxConcurrentRequests) {
		this.fallbackIsolationSemaphoreMaxConcurrentRequests = fallbackIsolationSemaphoreMaxConcurrentRequests;
	}
	public int getCircuitBreakerRequestVolumeThreshold() {
		return circuitBreakerRequestVolumeThreshold;
	}
	public void setCircuitBreakerRequestVolumeThreshold(int circuitBreakerRequestVolumeThreshold) {
		this.circuitBreakerRequestVolumeThreshold = circuitBreakerRequestVolumeThreshold;
	}
	public int getCircuitBreakerSleepWindowInMilliseconds() {
		return circuitBreakerSleepWindowInMilliseconds;
	}
	public void setCircuitBreakerSleepWindowInMilliseconds(int circuitBreakerSleepWindowInMilliseconds) {
		this.circuitBreakerSleepWindowInMilliseconds = circuitBreakerSleepWindowInMilliseconds;
	}
	public int getCircuitBreakerErrorThresholdPercentage() {
		return circuitBreakerErrorThresholdPercentage;
	}
	public void setCircuitBreakerErrorThresholdPercentage(int circuitBreakerErrorThresholdPercentage) {
		this.circuitBreakerErrorThresholdPercentage = circuitBreakerErrorThresholdPercentage;
	}
	public String getThreadPoolKey() {
		return threadPoolKey;
	}
	public void setThreadPoolKey(String threadPoolKey) {
		this.threadPoolKey = threadPoolKey;
	}
	public int getThreadPoolCoreSize() {
		return threadPoolCoreSize;
	}
	public void setThreadPoolCoreSize(int threadPoolCoreSize) {
		this.threadPoolCoreSize = threadPoolCoreSize;
	}
	public int getThreadPoolQueueSizeRejectionThreshold() {
		return threadPoolQueueSizeRejectionThreshold;
	}
	public void setThreadPoolQueueSizeRejectionThreshold(int threadPoolQueueSizeRejectionThreshold) {
		this.threadPoolQueueSizeRejectionThreshold = threadPoolQueueSizeRejectionThreshold;
	}
	
	@Override
	public String toString() {
		return "HystrixSetter [commandGroupKey=" + commandGroupKey + ", commandKey=" + commandKey
				+ ", executionIsolationThreadTimeoutInMilliseconds=" + executionIsolationThreadTimeoutInMilliseconds
				+ ", executionTimeoutInMilliseconds=" + executionTimeoutInMilliseconds
				+ ", fallbackIsolationSemaphoreMaxConcurrentRequests=" + fallbackIsolationSemaphoreMaxConcurrentRequests
				+ ", circuitBreakerRequestVolumeThreshold=" + circuitBreakerRequestVolumeThreshold
				+ ", circuitBreakerSleepWindowInMilliseconds=" + circuitBreakerSleepWindowInMilliseconds
				+ ", circuitBreakerErrorThresholdPercentage=" + circuitBreakerErrorThresholdPercentage
				+ ", threadPoolKey=" + threadPoolKey + ", threadPoolCoreSize=" + threadPoolCoreSize
				+ ", threadPoolQueueSizeRejectionThreshold=" + threadPoolQueueSizeRejectionThreshold + "]";
	}
	
}
