package cn.ms.neural.moduler.extension.flowrate.conf;

public class FlowRateConf {
	
	/**
	 * 流控总开关
	 */
	public static final String FLOWRATE_SWITCH_KEY = "switch";
	public static final boolean FLOWRATE_SWITCH_DEF_VAL = false;
	
	/**
	 * 并发子开关
	 */
	public static final String CCT_SWITCH_KEY = "cctswitch";
	public static final boolean CCT_SWITCH_DEF_VAL = false;
	
	/**
	 * QPS子开关
	 */
	public static final String QPS_SWITCH_KEY = "qpsswitch";
	public static final boolean QPS_SWITCH_DEF_VAL = false;
	
	/**
	 * 流控规则数据
	 */
	public static final String FLOWRATE_LIST_KEY = "list";
	
	
}
