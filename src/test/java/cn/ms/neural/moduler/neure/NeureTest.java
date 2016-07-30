package cn.ms.neural.moduler.neure;

import org.junit.Test;

import cn.ms.neural.moduler.neure.core.NeureFactory;
import cn.ms.neural.moduler.neure.handler.INeureHandler;

public class NeureTest {

	@Test
	public void test() {
		INeure<String, String> neure=new NeureFactory<>();
		String res=neure.neure("这是请求报文", new INeureHandler<String, String>() {
			public String route(String req, Object... args) throws Throwable {
				return "这是响应报文";
			}
			public String faulttolerant(String req, Object... args) {
				return null;
			}
			public void callback(String res, Object... args) throws Throwable {
			}
			public long breath(long nowTimes, long nowExpend, long maxRetryNum, Object... args) throws Throwable {
				return 0;
			}
			public void alarm(String req, Throwable t, Object... args) throws Throwable {
			}
		});
		
		System.out.println("--->"+res);
	}
	
}
