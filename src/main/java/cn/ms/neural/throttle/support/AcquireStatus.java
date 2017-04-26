package cn.ms.neural.throttle.support;

public enum AcquireStatus {

	/**
	 * Success Acquire
	 */
	ACQUIRE_SUCCESS,

	/**
	 * Failure consumption,later try to Acquire
	 */
	REACQUIRE_LATER;

}
