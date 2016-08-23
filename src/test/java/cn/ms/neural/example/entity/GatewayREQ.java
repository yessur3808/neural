package cn.ms.neural.example.entity;

public class GatewayREQ {

	/**
	 * 交易流水
	 */
	String bizNo;
	/**
	 * 响应报文
	 */
	String content;

	public String getBizNo() {
		return bizNo;
	}

	public void setBizNo(String bizNo) {
		this.bizNo = bizNo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
