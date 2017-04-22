package cn.ms.neural.grayrouter;

import org.junit.Assert;
import org.junit.Test;

public class RouterResultTest {

	@Test
	public void testRouterResult() throws Exception {
		RouterResult routerResult = new RouterResult();
		Assert.assertNotNull(routerResult);
		Assert.assertNull(routerResult.getVersion());
		Assert.assertNull(routerResult.getResultType());
	}

	@Test
	public void testName() throws Exception {
		RouterResult routerResult = new RouterResult();
		routerResult.setResultType(cn.ms.neural.grayrouter.ResultType.SUCCESS);
		Assert.assertEquals(ResultType.SUCCESS, routerResult.getResultType());

		routerResult.setVersion("1.0.0");
		Assert.assertEquals("1.0.0", routerResult.getVersion());
		
		String toString = routerResult.toString();
		Assert.assertNotNull(toString);
	}

}
