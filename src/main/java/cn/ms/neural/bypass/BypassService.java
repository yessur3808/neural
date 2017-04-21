package cn.ms.neural.bypass;

import java.util.concurrent.TimeUnit;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.dsl.Disruptor;

public class BypassService<T> {

	private Disruptor<T> disruptor;
	private BypassBuilder<T> bypassBuilder;

	public void start(BypassBuilder<T> bypassBuilder) {
		this.bypassBuilder = bypassBuilder;
		disruptor = new Disruptor<T>(this.bypassBuilder.getEventFactory(),
				this.bypassBuilder.getRingBufferSize(),
				this.bypassBuilder.getThreadFactory(),
				this.bypassBuilder.getProducerType(),
				this.bypassBuilder.getWaitStrategy());
		disruptor.handleEventsWith(this.bypassBuilder.getEventHandlers());
		disruptor.start();
	}

	public void publish(T input) {
		RingBuffer<T> ringBuffer = disruptor.getRingBuffer();
		long sequence = ringBuffer.next();

		try {
			T data = ringBuffer.get(sequence);
			bypassBuilder.getBypassWrapper().wrapper(input, data);
		} finally {
			ringBuffer.publish(sequence);// 发布事件；
		}
	}

	public Disruptor<T> getDisruptor() {
		return disruptor;
	}

	public void setDisruptor(Disruptor<T> disruptor) {
		this.disruptor = disruptor;
	}

	public void shutdown() {
		if (disruptor != null) {
			try {
				disruptor.shutdown(bypassBuilder.getShutdownTimeout(), TimeUnit.MILLISECONDS);
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
		}
	}

}
