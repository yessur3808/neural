package cn.ms.neural.common.logger;

/**
 * 日志中心扩展
 * 
 * @author lry
 */
public class LoggerManager extends LoggerFactory {

	public static final String SYS_BOOT_LOG_NAME = "sys-boot";
	public static final String SYS_DEFAULT_LOG_NAME = "sys-default";
	public static final String SYS_METRICS_LOG_NAME = "sys-metrics";
	public static final String SYS_PERF_LOG_NAME = "sys-perf";
	public static final String SYS_ERROR_LOG_NAME = "sys-error";

	public static final String BIZ_DEFAULT_LOG_NAME = "biz-default";
	public static final String BIZ_METRICS_LOG_NAME = "biz-metrics";
	public static final String BIZ_PERF_LOG_NAME = "biz-perf";
	public static final String BIZ_ERROR_LOG_NAME = "biz-error";

	// $NON-NLS-System log$
	public static ILogger getSysDefaultLog() {
		return getLogger(SYS_DEFAULT_LOG_NAME);
	}
	public static ILogger getSysBootLog() {
		return getLogger(SYS_BOOT_LOG_NAME);
	}
	public static ILogger getSysMetricsLog() {
		return getLogger(SYS_METRICS_LOG_NAME);
	}
	public static ILogger getSysPerfLog() {
		return getLogger(SYS_PERF_LOG_NAME);
	}
	public static ILogger getSysErrorLog() {
		return getLogger(SYS_ERROR_LOG_NAME);
	}

	// $NON-NLS-Business log$
	public static ILogger getBizDefaultLog() {
		return getLogger(BIZ_DEFAULT_LOG_NAME);
	}
	public static ILogger getBizMetricsLog() {
		return getLogger(BIZ_METRICS_LOG_NAME);
	}
	public static ILogger getBizPerfLog() {
		return getLogger(BIZ_PERF_LOG_NAME);
	}
	public static ILogger getBizErrorLog() {
		return getLogger(BIZ_ERROR_LOG_NAME);
	}

}
