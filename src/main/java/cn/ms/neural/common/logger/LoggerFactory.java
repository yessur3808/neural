package cn.ms.neural.common.logger;

import cn.ms.neural.common.logger.impl.JdkLoggerFactory;
import cn.ms.neural.common.logger.impl.Log4j2LoggerFactory;

/**
 * 日志工厂
 * 
 * @author lry
 */
public class LoggerFactory {
	
	public static interface InternalLoggerFactory {
		ILogger getLogger(Class<?> clazz);
		ILogger getLogger(String name);
	}
	
	protected static InternalLoggerFactory factory;
	static {
		initDefaultFactory();
	}
	
	public static void setLoggerFactory(InternalLoggerFactory factory) {
		if (factory != null) {
			LoggerFactory.factory = factory;
		}
	}
	
	public static ILogger getLogger(Class<?> clazz) {
		return factory.getLogger(clazz);
	}
	
	public static ILogger getLogger(String name) {
		return factory.getLogger(name);
	}
	
	
	public static void initDefaultFactory() {
		if (factory != null){
			return ;
		}
		try {
			factory = new Log4j2LoggerFactory();
			return;
		} catch (Exception e) {  
		}
		
		if(factory == null){
			factory = new JdkLoggerFactory();
		}
	} 

}