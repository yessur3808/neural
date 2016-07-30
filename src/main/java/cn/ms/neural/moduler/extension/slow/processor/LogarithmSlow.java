package cn.ms.neural.moduler.extension.slow.processor;

import cn.ms.neural.moduler.extension.slow.ISlow;

/**
 * 对数函数
 * y=log(a)x
 * 
 * @author lry
 */
public class LogarithmSlow implements ISlow<Double, Double> {

	public Double function(Double sys, Double x) {
		return Math.log(x)/Math.log(sys);
	}

	@Override
	public void init() throws Throwable {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destory() throws Throwable {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <SYS> Double function(SYS sys, Double x) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
