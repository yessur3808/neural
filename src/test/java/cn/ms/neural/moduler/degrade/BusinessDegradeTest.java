package cn.ms.neural.moduler.degrade;

import org.junit.Assert;
import org.junit.Test;

import cn.ms.neural.alarm.AlarmType;
import cn.ms.neural.common.URL;
import cn.ms.neural.common.exception.AlarmException;
import cn.ms.neural.common.exception.ProcessorException;
import cn.ms.neural.common.exception.degrade.DegradeException;
import cn.ms.neural.common.spi.ExtensionLoader;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.extension.degrade.IDegrade;
import cn.ms.neural.moduler.extension.degrade.processor.IDegradeProcessor;
import cn.ms.neural.moduler.extension.degrade.type.DegradeType;

/**
 * 业务降级测试
 * 
 * @author lry
 */
public class BusinessDegradeTest {

	@SuppressWarnings("unchecked")
	IDegrade<String, String> degrade=ExtensionLoader.getExtensionLoader(IDegrade.class).getAdaptiveExtension();
	
	@Test
	public void degrade_BUSINESS() throws Throwable {
		try {
			Moduler<String, String> moduler=new Moduler<>();
			moduler.setUrl(URL.valueOf("http://127.0.0.1:8080/degrade/?"
					+ "degrade.switch=true&"
					+ "degrade.degradetype="+DegradeType.BUSINESS));
			
			degrade.notify(moduler);
			
			String res=degrade.degrade("请求报文", new IDegradeProcessor<String, String>() {
				@Override
				public String processor(String req, Object... args) throws ProcessorException {
					return "这是响应报文";
				}
				@Override
				public String mock(String req, Object... args) throws DegradeException {
					return "这是MOCK响应报文";
				}
				@Override
				public String bizDegrade(String req, Object... args) throws DegradeException {
					return "这是业务降级响应报文";
				}
				@Override
				public void alarm(AlarmType alarmType, String req, String res, Throwable t, Object... args)
						throws AlarmException {
				}
			});
			
			Assert.assertEquals("这是业务降级响应报文", res);
		} catch (Throwable t) {
			Assert.assertTrue(false);
		}
	}
	
	@Test
	public void degrade_BUSINESS_processor() throws Throwable {
		try {
			Moduler<String, String> moduler=new Moduler<>();
			moduler.setUrl(URL.valueOf("http://127.0.0.1:8080/degrade/?"
					+ "degrade.switch=true&"
					+ "degrade.degradetype="+DegradeType.BUSINESS));
			
			degrade.notify(moduler);
			
			String res=degrade.degrade("请求报文", new IDegradeProcessor<String, String>() {
				@Override
				public String processor(String req, Object... args) throws ProcessorException {
					return "这是响应报文";
				}
				@Override
				public String mock(String req, Object... args) throws DegradeException {
					return "这是MOCK响应报文";
				}
				@Override
				public String bizDegrade(String req, Object... args) throws DegradeException {
					if(req.equals("请求报文")){
						return processor(req, args);
					}else{
						return "这是业务降级响应报文";
					}
				}
				@Override
				public void alarm(AlarmType alarmType, String req, String res, Throwable t, Object... args)
						throws AlarmException {
				}
			});
			
			Assert.assertEquals("这是响应报文", res);
		} catch (Throwable t) {
			Assert.assertTrue(false);
		}
	}
	
	@Test
	public void degrade_BUSINESS_mock() throws Throwable {
		try {
			Moduler<String, String> moduler=new Moduler<>();
			moduler.setUrl(URL.valueOf("http://127.0.0.1:8080/degrade/?"
					+ "degrade.switch=true&"
					+ "degrade.degradetype="+DegradeType.BUSINESS));
			
			degrade.notify(moduler);
			
			String res=degrade.degrade("请求报文", new IDegradeProcessor<String, String>() {
				@Override
				public String processor(String req, Object... args) throws ProcessorException {
					throw new RuntimeException("这是模拟业务执行失败");
				}
				@Override
				public String mock(String req, Object... args) throws DegradeException {
					return "这是MOCK响应报文";
				}
				@Override
				public String bizDegrade(String req, Object... args) throws DegradeException {
					if(req.equals("请求报文")){
						try {
							return processor(req, args);
						} catch (Exception e) {
							return mock(req, args);
						}
					}else{
						return "这是业务降级响应报文";
					}
				}
				@Override
				public void alarm(AlarmType alarmType, String req, String res, Throwable t, Object... args)
						throws AlarmException {
				}
			});
			
			Assert.assertEquals("这是MOCK响应报文", res);
		} catch (Throwable t) {
			Assert.assertTrue(false);
		}
	}
	
}
