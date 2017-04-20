package cn.ms.neural.sequence;

import cn.ms.neural.extension.Scope;
import cn.ms.neural.extension.Spi;

/**
 * 分布式序列/ID
 * 
 * @author lry
 *
 * @param <T>
 */
@Spi(scope = Scope.SINGLETON)
public interface Sequence {

	Long nextLong();
	
	String next();

}
