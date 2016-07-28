package cn.ms.neural.moduler.extension.gracestop.type;

import cn.ms.neural.type.multitype.ITypeAdaptor;

public enum GraceStopStatusType implements ITypeAdaptor {

	/**
	 * 在线
	 */
	ONLINE("online","This is online."),
	
	/**
	 * 离线
	 */
	OFFLINE("offline","This is offline.");
	
	String val;
	String msg;
	
	GraceStopStatusType(String val, String msg) {
		this.val=val;
		this.msg=msg;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
