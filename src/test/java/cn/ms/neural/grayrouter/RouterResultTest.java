package cn.ms.neural.grayrouter;

import org.junit.Assert;
import org.junit.Test;

import cn.ms.neural.ResultType;
import cn.ms.neural.RouterResult;

public class RouterResultTest {

	@Test
	public void testRouterResult() throws Exception {
		RouterResult routerResult = new RouterResult();
		Assert.assertNotNull(routerResult);
		Assert.assertNull(routerResult.getValue());
		Assert.assertNull(routerResult.getResultType());
	}

	@Test
	public void testName() throws Exception {
		RouterResult routerResult = new RouterResult();
		routerResult.setResultType(ResultType.SUCCESS);
		Assert.assertEquals(ResultType.SUCCESS, routerResult.getResultType());

		routerResult.setValue("1.0.0");
		Assert.assertEquals("1.0.0", routerResult.getValue());
		
		String toString = routerResult.toString();
		Assert.assertNotNull(toString);
	}

}
