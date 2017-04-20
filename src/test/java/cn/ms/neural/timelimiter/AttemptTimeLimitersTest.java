package cn.ms.neural.timelimiter;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import cn.ms.neural.timelimiter.AttemptTimeLimiters;

public class AttemptTimeLimitersTest {

	@Test
	public void fixedTimeLimitTest() {
		try {
			AttemptTimeLimiters.fixedTimeLimit(3000, TimeUnit.MILLISECONDS).call(new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					Thread.sleep(2000);
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
