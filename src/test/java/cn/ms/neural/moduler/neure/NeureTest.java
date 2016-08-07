package cn.ms.neural.moduler.neure;

import org.junit.Assert;
import org.junit.Test;

import cn.ms.neural.common.exception.ProcessorException;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.neure.core.NeureFactory;
import cn.ms.neural.moduler.neure.processor.INeureProcessor;
import cn.ms.neural.moduler.neure.type.AlarmType;

public class NeureTest {

	@Test
	public void test_route() {
		INeure<String, String> neure=new NeureFactory<>();
		neure.notify(new Moduler<String, String>());
		String res=neure.neure("这是请求报文", new INeureProcessor<String, String>() {
			public String faulttolerant(String req, Object... args) {
				return null;
			}
			public void callback(String res, Object... args) throws Throwable {
			}
			public long breath(long nowTimes, long nowExpend, long maxRetryNum, Object... args) throws Throwable {
				return 0;
			}
			public void alarm(AlarmType alarmType, String req, Throwable t, Object... args) throws Throwable {
			}
			@Override
			public String processor(String req, Object... args) throws ProcessorException {
				return "这是响应报文";
			}
		});
		
		Assert.assertEquals("这是响应报文", res);
	}
	
	
	@Test
	public void test_faulttolerant() {
		INeure<String, String> neure=new NeureFactory<>();
		try {
			neure.notify(new Moduler<String, String>());
			String res=neure.neure("这是请求报文", new INeureProcessor<String, String>() {
				public String faulttolerant(String req, Object... args) {
					return "这是响应报文";
				}
				public void callback(String res, Object... args) throws Throwable {
				}
				public long breath(long nowTimes, long nowExpend, long maxRetryNum, Object... args) throws Throwable {
					return 0;
				}
				public void alarm(AlarmType alarmType, String req, Throwable t, Object... args) throws Throwable {
				}
				@Override
				public String processor(String req, Object... args) throws ProcessorException {
					throw new RuntimeException();
				}
			});
			
			Assert.assertEquals("这是响应报文", res);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
