package cn.ms.neural.moduler.extension.flowrate.entity;

import cn.ms.neural.moduler.extension.flowrate.type.FlowRateType;

public class FlowRateData {

	/**
	 * 限流资源分类
	 */
	private String resType;
	/**
	 * 资源数据
	 */
	private String data;
	/**
	 * 限流类型
	 */
	private FlowRateType flowRateType;
	
	public FlowRateData() {
	}
	public FlowRateData(String resType, String data, FlowRateType flowRateType) {
		super();
		this.resType = resType;
		this.data = data;
		this.flowRateType = flowRateType;
	}
	public String getResType() {
		return resType;
	}
	public void setResType(String resType) {
		this.resType = resType;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public FlowRateType getFlowRateType() {
		return flowRateType;
	}
	public void setFlowRateType(FlowRateType flowRateType) {
		this.flowRateType = flowRateType;
	}
	
}
