package cn.ms.neural.moduler.engine.slow.processor;

import cn.ms.neural.moduler.engine.slow.ISlow;
import cn.ms.neural.moduler.engine.slow.entity.LinearEntity;

/**
 * 线性函数
 * y=ax+b
 * 
 * @author lry
 */
public class LinearSlow implements ISlow<Double, Double> {

	public void init() throws Throwable {
		
	}
	
	public Double function(LinearEntity sys, Double x) {
		return sys.getA()*x+sys.getB();
	}
	
	public void destory() throws Throwable {
		
	}

	@Override
	public <SYS> Double function(SYS sys, Double x) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
