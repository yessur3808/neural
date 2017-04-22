package cn.ms.neural.grayrouter;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class GrayRouterTest {

	private GrayRouter newGrayRouter() {
		GrayRouter grayRouter = new GrayRouter();
		String[] granularities = { "areaId", "channelId" };
		grayRouter.initialize(granularities);
		
		Map<String, String> routerRuleMap = new HashMap<String, String>();
		routerRuleMap.put("(shenzheng),(weixin01),(testService01)", "1.0.0");
		routerRuleMap.put("(beijing),(weixin01),(testService01)", "2.0.0");
		routerRuleMap.put("(shenzheng),(weixin02),(testService01)", "3.0.0");
		routerRuleMap.put("(shenzheng),(weixin01),(testService02)", "1.0.0");
		grayRouter.notifys(routerRuleMap);
		return grayRouter;
	}

	@Test
	public void testNonInitialize() throws Exception {
		GrayRouter grayRouter = new GrayRouter();
		RouterResult routerResult =  grayRouter.doGrayRouter(new HashMap<String, Object>(), "testService01");
		Assert.assertEquals(ResultType.NONINITIALIZE, routerResult.getResultType());
	}
	
	@Test
	public void testShenzhengWeixin01TestService01() throws Exception {
		GrayRouter grayRouter =  this.newGrayRouter();
		
		Map<String, Object> routers = new HashMap<String, Object>();
		routers.put("areaId", "shenzheng");
		routers.put("channelId", "weixin01");
		RouterResult routerResult =  grayRouter.doGrayRouter(routers, "testService01");
		Assert.assertEquals(ResultType.SUCCESS, routerResult.getResultType());
		Assert.assertEquals("1.0.0", routerResult.getVersion());
	}
	
	@Test
	public void testBeijingWeixin01TestService01() throws Exception {
		GrayRouter grayRouter =  this.newGrayRouter();
		
		Map<String, Object> routers = new HashMap<String, Object>();
		routers.put("areaId", "beijing");
		routers.put("channelId", "weixin01");
		RouterResult routerResult =  grayRouter.doGrayRouter(routers, "testService01");
		Assert.assertEquals(ResultType.SUCCESS, routerResult.getResultType());
		Assert.assertEquals("2.0.0", routerResult.getVersion());
	}
	
	@Test
	public void testShenzhengWeixin02TestService01() throws Exception {
		GrayRouter grayRouter =  this.newGrayRouter();
		
		Map<String, Object> routers = new HashMap<String, Object>();
		routers.put("areaId", "shenzheng");
		routers.put("channelId", "weixin02");
		RouterResult routerResult =  grayRouter.doGrayRouter(routers, "testService01");
		Assert.assertEquals(ResultType.SUCCESS, routerResult.getResultType());
		Assert.assertEquals("3.0.0", routerResult.getVersion());
	}
	
	@Test
	public void testShenzhengWeixin01TestService02() throws Exception {
		GrayRouter grayRouter =  this.newGrayRouter();
		
		Map<String, Object> routers = new HashMap<String, Object>();
		routers.put("areaId", "shenzheng");
		routers.put("channelId", "weixin01");
		RouterResult routerResult =  grayRouter.doGrayRouter(routers, "testService02");
		Assert.assertEquals(ResultType.SUCCESS, routerResult.getResultType());
		Assert.assertEquals("1.0.0", routerResult.getVersion());
	}
	
	@Test
	public void testGuangzhouWeixin01TestService01() throws Exception {
		GrayRouter grayRouter =  this.newGrayRouter();
		
		Map<String, Object> routers = new HashMap<String, Object>();
		routers.put("areaId", "guangzhou");
		routers.put("channelId", "weixin01");
		RouterResult routerResult =  grayRouter.doGrayRouter(routers, "testService01");
		Assert.assertEquals(ResultType.NOTFOUND, routerResult.getResultType());
	}
	
	@Test
	public void testShenzhengApp01TestService01() throws Exception {
		GrayRouter grayRouter =  this.newGrayRouter();
		
		Map<String, Object> routers = new HashMap<String, Object>();
		routers.put("areaId", "shenzheng");
		routers.put("channelId", "app01");
		RouterResult routerResult =  grayRouter.doGrayRouter(routers, "testService01");
		Assert.assertEquals(ResultType.NOTFOUND, routerResult.getResultType());
	}
	
	@Test
	public void testShenzhengWeixin01TestService03() throws Exception {
		GrayRouter grayRouter =  this.newGrayRouter();
		
		Map<String, Object> routers = new HashMap<String, Object>();
		routers.put("areaId", "shenzheng");
		routers.put("channelId", "weixin01");
		RouterResult routerResult =  grayRouter.doGrayRouter(routers, "testService03");
		Assert.assertEquals(ResultType.NOTFOUND, routerResult.getResultType());
	}

	@Test
	public void testGrapRouterRegexEquals() throws Exception {
		GrayRouter grayRouter = new GrayRouter();
		String[] granularities = {};
		grayRouter.initialize(granularities);
		
		Map<String, String> routerRuleMap = new HashMap<String, String>();
		routerRuleMap.put("testService01", "1.0.0");
		grayRouter.notifys(routerRuleMap);
		
		Map<String, Object> routers = new HashMap<String, Object>();
		RouterResult routerResult =  grayRouter.doGrayRouter(routers, "testService01");
		Assert.assertEquals(ResultType.SUCCESS, routerResult.getResultType());
		Assert.assertEquals("1.0.0", routerResult.getVersion());
	}
	
}
