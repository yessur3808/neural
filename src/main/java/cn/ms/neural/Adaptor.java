package cn.ms.neural;

/**
 * Adapter for each module
 * 
 * @author lry
 */
public interface Adaptor {

	/**
	 * Module initialization
	 * 
	 * @throws Throwable
	 */
	void init() throws Throwable;
	
	/**
	 * Module destruction
	 * 
	 * @throws Throwable
	 */
	void destory() throws Throwable;
	
}
