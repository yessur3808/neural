package cn.ms.neural.moduler.gracestop;

import org.junit.Assert;
import org.junit.Test;

import cn.ms.neural.alarm.AlarmType;
import cn.ms.neural.common.URL;
import cn.ms.neural.common.exception.AlarmException;
import cn.ms.neural.common.exception.gracestop.GraceStopedException;
import cn.ms.neural.common.spi.ExtensionLoader;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.extension.gracestop.IGraceStop;
import cn.ms.neural.moduler.extension.gracestop.processor.IGraceStopProcessor;
import cn.ms.neural.moduler.extension.gracestop.type.GraceStopStatusType;

public class GraceStopTest {

	/**
	 * 关机状态测试
	 */
	@Test
	public void test_OFFLINE() {
		String reqData="请求报文";
		
		Moduler<String, String> moduler=new Moduler<String, String>();
		moduler.setUrl(URL.valueOf("http://127.0.0.1:8080/gs/?"
				+ "gracestop.status="+GraceStopStatusType.OFFLINE
				+ "&gracestop.switch=boot"));
		
		@SuppressWarnings("unchecked")
		IGraceStop<String, String> graceStop=ExtensionLoader.getExtensionLoader(IGraceStop.class).getAdaptiveExtension();
		graceStop.notify(moduler);//注入模块
		
		try {
			graceStop.gracestop(reqData, new IGraceStopProcessor<String, String>() {
				@Override
				public String processor(String req, Object... args) {
					return req;
				}

				@Override
				public void alarm(AlarmType alarmType, String req, String res, Throwable t, Object... args)
						throws AlarmException {
				}
			});
			
			Assert.assertTrue(false);
		} catch (Exception e) {
			if(e instanceof GraceStopedException){
				Assert.assertTrue(true);	
			}else{
				Assert.assertTrue(false);
			}
		}
	}
	
	/**
	 * 开机状态测试
	 */
	@Test
	public void test_ONLINE() {
		String reqData="请求报文";
		
		Moduler<String, String> moduler=new Moduler<String, String>();
		moduler.setUrl(URL.valueOf("http://127.0.0.1:8080/gs/?"
				+ "gracestop.status="+GraceStopStatusType.ONLINE
				+ "&gracestop.switch=boot"));
		
		@SuppressWarnings("unchecked")
		IGraceStop<String, String> graceStop=ExtensionLoader.getExtensionLoader(IGraceStop.class).getAdaptiveExtension();
		graceStop.notify(moduler);//注入模块
		
		try {
			String res=graceStop.gracestop(reqData, new IGraceStopProcessor<String, String>() {
				@Override
				public String processor(String req, Object... args) {
					return req;
				}

				@Override
				public void alarm(AlarmType alarmType, String req, String res, Throwable t, Object... args)
						throws AlarmException {
				}
			});
			
			Assert.assertEquals(reqData, res);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}
	
}
