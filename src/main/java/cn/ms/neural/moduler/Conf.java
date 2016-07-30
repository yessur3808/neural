package cn.ms.neural.moduler;

/**
 * 公共配置模块
 * @author lry
 * @version v1.0
 */
public class Conf {
	
	/**
	 * 模块参数分割符号
	 */
	public static final String MODULER_PARAMTER_SEQ = ".";
	
	/**
	 * 数据分隔符
	 */
	public static final String SEPARATOR = ",";
	public static final String RES_KV_SEQ = ":";
	public static final String ANT = "@";
	/**
	 * 优雅停机模块
	 */
	public static final String GRACESTOP = "gracestop";
	
	/**
	 * 黑白名单模块
	 */
	public static final String BLACKWHITE = "blackwhite";
	
	/**
	 * 管道缩放模块
	 */
	public static final String PIPESCALING = "pipescaling";
	
	/**
	 * 流控模块
	 */
	public static final String FLOWRATE = "flowrate";
	
	/**
	 * 服务降级模块
	 */
	public static final String DEGRADE = "degrade";
	
	/**
	 * 幂等模块
	 */
	public static final String IDEMPOTENT="idempotent";

	/**
	 * 回声探测模块
	 */
	public static final String ECHOSOUND = "echosound";

	/**
	 * 微服务神经元模块
	 */
	public static final String NEURE = "neure";
	
}
