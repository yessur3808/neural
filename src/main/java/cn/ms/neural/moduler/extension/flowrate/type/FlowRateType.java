package cn.ms.neural.moduler.extension.flowrate.type;

import cn.ms.neural.type.multitype.ITypeAdaptor;

/**
 * The flow rate type.
 * 
 * @author lry
 */
public enum FlowRateType implements ITypeAdaptor {

	/**
	 * The rate limiter.
	 */
	QPS("QPS", "The rate limiter."),
	
	/**
	 * The concurrent number.
	 */
	CCT("CCT", "The concurrent number.");
	
	String val;
	String msg;
	
	FlowRateType(String val, String msg) {
		this.val=val;
		this.msg=msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val=val;
	}
	
}
