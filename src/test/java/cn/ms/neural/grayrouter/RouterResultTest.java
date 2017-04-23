package cn.ms.neural.grayrouter;

import org.junit.Assert;
import org.junit.Test;

import cn.ms.neural.ResultType;
import cn.ms.neural.Result;

public class RouterResultTest {

	@Test
	public void testRouterResult() throws Exception {
		Result result = new Result();
		Assert.assertNotNull(result);
		Assert.assertNull(result.getValue());
		Assert.assertNull(result.getResultType());
	}

	@Test
	public void testName() throws Exception {
		Result result = new Result();
		result.setResultType(ResultType.SUCCESS);
		Assert.assertEquals(ResultType.SUCCESS, result.getResultType());

		result.setValue("1.0.0");
		Assert.assertEquals("1.0.0", result.getValue());
		
		String toString = result.toString();
		Assert.assertNotNull(toString);
	}

}
