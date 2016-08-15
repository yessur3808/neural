package cn.ms.neural.moduler.flowrate;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import cn.ms.neural.moduler.flowrate.RateLimiter.SleepingStopwatch;

public class ListenableFutureDemo {
	
	ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
	final ListenableFuture<Integer> listenableFuture = executorService.submit(new Callable<Integer>() {
		@Override
		public Integer call() throws Exception {
			System.out.println("call execute..");
			TimeUnit.SECONDS.sleep(3);
			return 7;
		}
	});
	
	/**
	 * RateLimiter类似于JDK的信号量Semphore，他用来限制对资源并发访问的线程数
	 */
	public static void testRateLimiter() {
		try {
			RateLimiter limiter = RateLimiter.create(5);
			limiter.setRate(6);
			for (int i = 0; i < 10; i++) {
				System.out.println(limiter.tryAcquire());
				Thread.sleep(150);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			testRateLimiter();
			
//			ListenableFutureDemo demo=new ListenableFutureDemo();
//			for (int i = 0; i < 10; i++) {
//				demo.testListenableFuture();
//				Thread.sleep(0);
//			}
//			System.out.println("----");
//			demo.executorService.shutdown();
//			System.out.println("++++");
		} catch (Exception e) {
			e.printStackTrace();
		}
		SleepingStopwatch.createFromSystemTimer();
	}

	public void testListenableFuture() {
		Futures.addCallback(listenableFuture, new FutureCallback<Integer>() {
			@Override
			public void onSuccess(Integer result) {
				System.out.println("get listenable future's result with callback " + result);
			}

			@Override
			public void onFailure(Throwable t) {
				t.printStackTrace();
			}
		});
	}
}
