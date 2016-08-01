package cn.ms.neural.common.spi;

/**
 * 扩展点适配器
 * 
 * @author lry
 * @version v1.0
 */
public @interface Adaptive {

	String value() default "default";
	
}
