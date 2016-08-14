package cn.ms.neural.moduler.neure;

import org.junit.Assert;
import org.junit.Test;

import cn.ms.neural.common.exception.AlarmException;
import cn.ms.neural.common.exception.ProcessorException;
import cn.ms.neural.common.exception.neure.NeureBreathException;
import cn.ms.neural.common.exception.neure.NeureCallbackException;
import cn.ms.neural.common.exception.neure.NeureFaultTolerantException;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.neure.core.NeureFactory;
import cn.ms.neural.moduler.neure.processor.INeureProcessor;
import cn.ms.neural.moduler.senior.alarm.IAlarmType;

public class NeureTest {

	@Test
	public void test_route() {
		INeure<String, String> neure=new NeureFactory<>();
		neure.notify(new Moduler<String, String>());
		String res=neure.neure("这是请求报文", new INeureProcessor<String, String>() {
			@Override
			public String processor(String req, Object... args) throws ProcessorException {
				return "这是响应报文";
			}
			@Override
			public String faulttolerant(String req, Object... args) throws NeureFaultTolerantException {
				return null;
			}
			@Override
			public long breath(long nowTimes, long nowExpend, long maxRetryNum, Object... args) throws NeureBreathException {
				return 0;
			}
			@Override
			public void callback(String res, Object... args) throws NeureCallbackException {
			}
			@Override
			public void alarm(IAlarmType alarmType, String req, String res, Throwable t, Object... args)
					throws AlarmException {
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
				@Override
				public String processor(String req, Object... args) throws ProcessorException {
					throw new RuntimeException();
				}
				@Override
				public String faulttolerant(String req, Object... args) {
					return "这是响应报文";
				}
				@Override
				public long breath(long nowTimes, long nowExpend, long maxRetryNum, Object... args) throws NeureBreathException {
					return 0;
				}
				@Override
				public void callback(String res, Object... args) throws NeureCallbackException {
				}
				@Override
				public void alarm(IAlarmType alarmType, String req, String res, Throwable t, Object... args)
						throws AlarmException {
				}
			});
			
			Assert.assertEquals("这是响应报文", res);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
