package cn.ms.neural.status;

import cn.ms.neural.extension.Scope;
import cn.ms.neural.extension.Spi;

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