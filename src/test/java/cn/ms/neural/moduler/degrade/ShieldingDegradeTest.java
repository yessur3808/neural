package cn.ms.neural.moduler.degrade;

import org.junit.Assert;
import org.junit.Test;

import cn.ms.neural.common.URL;
import cn.ms.neural.common.exception.ProcessorException;
import cn.ms.neural.common.exception.degrade.DegradeException;
import cn.ms.neural.common.spi.ExtensionLoader;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.extension.degrade.IDegrade;
import cn.ms.neural.moduler.extension.degrade.processor.IDegradeProcessor;
import cn.ms.neural.moduler.extension.degrade.type.DegradeType;
import cn.ms.neural.moduler.extension.degrade.type.StrategyType;

/**
 * 屏蔽降级测试
 * 
 * @author lry
 */
public class ShieldingDegradeTest {

	@SuppressWarnings("unchecked")
	IDegrade<String, String> degrade=ExtensionLoader.getExtensionLoader(IDegrade.class).getAdaptiveExtension();
	
	@Test
	public void degrade_SHIELDING_NULL() throws Throwable {
		try {
			Moduler<String, String> moduler=new Moduler<>();
			moduler.setUrl(URL.valueOf("http://127.0.0.1:8080/degrade/?"
					+ "degrade.switch=true&"
					+ "degrade.degradetype="+DegradeType.SHIELDING
					+ "&degrade.strategytype="+StrategyType.NULL));
			
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
			});
			
			Assert.assertEquals(null, res);
		} catch (Throwable t) {
			Assert.assertTrue(false);
		}
	}
	
	@Test
	public void degrade_SHIELDING_EXCEPTION() throws Throwable {
		try {
			Moduler<String, String> moduler=new Moduler<>();
			moduler.setUrl(URL.valueOf("http://127.0.0.1:8080/degrade/?"
					+ "degrade.switch=true&"
					+ "degrade.degradetype="+DegradeType.SHIELDING
					+ "&degrade.strategytype="+StrategyType.EXCEPTION));
			
			degrade.notify(moduler);
			
			degrade.degrade("请求报文", new IDegradeProcessor<String, String>() {
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
			});
			
			Assert.assertTrue(false);
		} catch (Throwable t) {
			if(t instanceof DegradeException){
				Assert.assertTrue(true);
			}else{
				Assert.assertTrue(false);
			}
		}
	}
	
	@Test
	public void degrade_SHIELDING_MOCK() throws Throwable {
		try {
			Moduler<String, String> moduler=new Moduler<>();
			moduler.setUrl(URL.valueOf("http://127.0.0.1:8080/degrade/?"
					+ "degrade.switch=true&"
					+ "degrade.degradetype="+DegradeType.SHIELDING
					+ "&degrade.strategytype="+StrategyType.MOCK));
			
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
			});
			
			Assert.assertEquals("这是MOCK响应报文", res);
		} catch (Throwable t) {
			Assert.assertTrue(false);
		}
	}
	
}
