package cn.ms.neural.sequence;

import cn.ms.micro.extension.Scope;
import cn.ms.micro.extension.Spi;

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
