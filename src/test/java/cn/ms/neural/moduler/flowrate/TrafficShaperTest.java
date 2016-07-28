package cn.ms.neural.moduler.flowrate;

import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;

public class TrafficShaperTest {

	// key-value(serverice,qps)
	private static final ConcurrentMap<String, Double> resourceMap = Maps.newConcurrentMap();
	// userkey-service limiter
	private static final ConcurrentMap<String, RateLimiter> userresourceLimiterMap = Maps.newConcurrentMap();
	static {
		// init
		resourceMap.put("aaa", 50.0);
	}

	public static void updateResourceQps(String resource, double qps) {
		resourceMap.put(resource, qps);
	}

	public static void removeResource(String resource) {
		resourceMap.remove(resource);
	}

	public static int enter(String resource, String userkey) {
		long t1 = System.currentTimeMillis();
		double qps = resourceMap.get(resource);
		// 服务不限流
		if (qps == 0.0) {
			return 0;
		}
		String keyser = resource + userkey;
		RateLimiter keyserlimiter = userresourceLimiterMap.get(keyser);
		// if null,new limiter
		if (keyserlimiter == null) {
			keyserlimiter = RateLimiter.create(qps);
			RateLimiter putByOtherThread = userresourceLimiterMap.putIfAbsent(keyser, keyserlimiter);
			if (putByOtherThread != null) {
				keyserlimiter = putByOtherThread;
			}
			keyserlimiter.setRate(qps);
		}

		// tryacquire
		if (!keyserlimiter.tryAcquire()) {
			System.out.println("use:" + (System.currentTimeMillis() - t1) + "ms;" + resource + "  visited  too frequently  by key:" + userkey);
			return 99;
		} else {
			System.out.println("use:" + (System.currentTimeMillis() - t1) + "ms;");
			return 0;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		int i = 0;
		while (true) {
			i++;
			long t2 = System.currentTimeMillis();
			System.out.println(t2 + ":qq:" + i);

			int res = TrafficShaperTest.enter("aaa", "qq");
			System.out.println((System.currentTimeMillis() - t2) + ":qq:" + i);
			if (res == 99) {
				i = 0;
				Thread.sleep(1000);
			}
		}
	}
}
