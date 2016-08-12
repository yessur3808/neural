package cn.ms.neural.moduler.flowrate;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.ms.neural.common.concurrent.LongAdder;

/**
 * 流量控制
 * 
 * @author lry
 * @version v1.0
 */
public class FlowRate {
	LongAdder longAdder = new LongAdder();

	public FlowRate() {
		int count = 2000;
		CyclicBarrier cyclicBarrier = new CyclicBarrier(count);
		ExecutorService executorService = Executors.newFixedThreadPool(count);
		for (int i = 0; i < count; i++) {
			executorService.execute(new Task(cyclicBarrier));
		}

		executorService.shutdown();
		while (!executorService.isTerminated()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println(longAdder.doubleValue());
	}

	public static void main(String[] args) {
		new FlowRate();
	}

	public class Task implements Runnable {
		private CyclicBarrier cyclicBarrier;

		public Task(CyclicBarrier cyclicBarrier) {
			this.cyclicBarrier = cyclicBarrier;
		}

		@Override
		public void run() {
			try {
				// 等待所有任务准备就绪
				cyclicBarrier.await();
				// 测试内容
				longAdder.increment();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
