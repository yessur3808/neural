package cn.ms.neural.throttle.limter;

/**
 * 执行频率<br>
 * <br>
 * ONCE_PER_MINUTE：表示一分钟执行一次
 * 
 * @author lry
 */
public final class LimiterDelayConstants {
	public static final Double ONCE_PER_SECOND = 1D;
	public static final Double ONCE_PER_MINUTE = 1D / 60;
	public static final Double ONCE_PER_HOUR = 1D / 60 / 60;
	public static final Double ONCE_PER_DAY = 1D / 6 / 24;
	public static final Double ONCE_PER_WEEK = 1D / 6 / 24 / 7;
	public static final Double ONCE_PER_MONTH = 1D / 6 / 24 / 30;
}