package cn.ms.neural.moduler.blackwhite;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import cn.ms.neural.common.URL;
import cn.ms.neural.common.exception.ProcessorException;
import cn.ms.neural.common.exception.blackwhite.BlackListException;
import cn.ms.neural.common.exception.blackwhite.WhiteListException;
import cn.ms.neural.common.spi.ExtensionLoader;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.extension.blackwhite.IBlackWhite;
import cn.ms.neural.moduler.extension.blackwhite.processor.IBlackWhiteProcessor;
import cn.ms.neural.moduler.extension.blackwhite.type.BlackWhiteType;

public class BlackWhiteTest {

	@SuppressWarnings("unchecked")
	IBlackWhite<String, String> blackWhite=ExtensionLoader.getExtensionLoader(IBlackWhite.class).getAdaptiveExtension();
	
	public BlackWhiteTest() {
		Moduler<String, String> moduler=new Moduler<>();
		moduler.setUrl(URL.valueOf("http://127.0.0.1:8080/bw/?"
				+ "blackwhite.switch=true&"//总开关
				+ "blackwhite.disableRecordSwitch=true&"
				+ "blackwhite.visitRecordSwitch=true&"
				+ "blackwhite.list="
				+ "10.24.1.10:"+BlackWhiteType.WHITE+":true:ip,"//在线白名单
				+ "10.24.1.11:"+BlackWhiteType.WHITE+":false:ip,"//离线白名单
				+ "10.24.1.12:"+BlackWhiteType.BLACK+":true:ip,"//在线黑名单
				+ "10.24.1.13:"+BlackWhiteType.BLACK+":false:ip"));//离线黑名单
		blackWhite.notify(moduler);
	}
	
	/**
	 * 在线白名单校验
	 */
	@Test
	public void onlineWhiteTest() {
		try {
			//设置过滤KEY-VALUE
			Map<String, Object> blackWhiteIdKeyVals=new HashMap<>();
			blackWhiteIdKeyVals.put("ip","10.24.1.10");
			
			//拉取过滤
			String res=blackWhite.blackwhite("这是请求报文", blackWhiteIdKeyVals, new IBlackWhiteProcessor<String, String>() {
				@Override
				public String processor(String req, Object... args) throws ProcessorException {
					return "这是响应报文";
				}
			});
			Assert.assertTrue("这是响应报文".equals(res));
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	/**
	 * 离线白名单校验
	 */
	@Test
	public void offlineWhiteTest() {
		try {
			Map<String, Object> blackWhiteIdKeyVals=new HashMap<>();
			blackWhiteIdKeyVals.put("ip","10.24.1.11");
			blackWhite.blackwhite("这是请求报文", blackWhiteIdKeyVals, new IBlackWhiteProcessor<String, String>() {
				@Override
				public String processor(String req, Object... args) throws ProcessorException {
					return "这是响应报文";
				}
			});
			Assert.assertTrue(false);
		} catch (Throwable t) {
			if(t instanceof WhiteListException){
				Assert.assertTrue(true);
			}else{
				Assert.assertTrue(false);
			}
		}
	}
	
	/**
	 * 在线黑名单校验
	 */
	@Test
	public void onlineBlackTest() {
		try {
			//设置过滤KEY-VALUE
			Map<String, Object> blackWhiteIdKeyVals=new HashMap<>();
			blackWhiteIdKeyVals.put("ip","10.24.1.12");
			
			//拉取过滤
			blackWhite.blackwhite("这是请求报文", blackWhiteIdKeyVals, new IBlackWhiteProcessor<String, String>() {
				@Override
				public String processor(String req, Object... args) throws ProcessorException {
					return "这是响应报文";
				}
			});
			Assert.assertTrue(false);
		} catch (Throwable t) {
			if(t instanceof BlackListException){
				Assert.assertTrue(true);
			}else{
				Assert.assertTrue(false);
			}
		}
	}
	
	/**
	 * 离线黑名单校验
	 */
	@Test
	public void offlineBlackTest() {
		try {
			Map<String, Object> blackWhiteIdKeyVals=new HashMap<>();
			blackWhiteIdKeyVals.put("ip","10.24.1.13");
			blackWhite.blackwhite("这是请求报文", blackWhiteIdKeyVals, new IBlackWhiteProcessor<String, String>() {
				@Override
				public String processor(String req, Object... args) throws ProcessorException {
					return "这是响应报文";
				}
			});
			Assert.assertTrue(false);
		} catch (Throwable t) {
			if(t instanceof WhiteListException){
				Assert.assertTrue(true);
			}else{
				Assert.assertTrue(false);
			}
		}
	}
	
}
