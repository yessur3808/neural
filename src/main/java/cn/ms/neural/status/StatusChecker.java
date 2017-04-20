package cn.ms.neural.status;

import cn.ms.micro.extension.Scope;
import cn.ms.micro.extension.Spi;

/**
 * StatusChecker
 * 
 * @author lry
 */
@Spi(scope = Scope.PROTOTYPE)
public interface StatusChecker {

	/**
	 * check status
	 * 
	 * @return
	 */
	Status check();

}