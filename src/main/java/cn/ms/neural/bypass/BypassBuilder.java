package cn.ms.neural.bypass;

import java.util.concurrent.ThreadFactory;

import cn.ms.micro.threadpool.NamedThreadFactory;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;

public class BypassBuilder<T> {

	private EventFactory<T> eventFactory;
	private int ringBufferSize = 1 * 1024;
	private BypassWrapper<T> bypassWrapper;
	private EventHandler<T>[] eventHandlers;
	private ProducerType producerType = ProducerType.SINGLE;
	private WaitStrategy waitStrategy = new YieldingWaitStrategy();
	private ThreadFactory threadFactory = new NamedThreadFactory();

	private long shutdownTimeout = 60 * 1000;

	public EventFactory<T> getEventFactory() {
		return eventFactory;
	}

	public BypassBuilder<T> setEventFactory(EventFactory<T> eventFactory) {
		this.eventFactory = eventFactory;
		return this;
	}

	public int getRingBufferSize() {
		return ringBufferSize;
	}

	public BypassBuilder<T> setRingBufferSize(int ringBufferSize) {
		this.ringBufferSize = ringBufferSize;
		return this;
	}

	public BypassWrapper<T> getBypassWrapper() {
		return bypassWrapper;
	}

	public BypassBuilder<T> setBypassWrapper(BypassWrapper<T> bypassWrapper) {
		this.bypassWrapper = bypassWrapper;
		return this;
	}

	public EventHandler<T>[] getEventHandlers() {
		return eventHandlers;
	}

	public BypassBuilder<T> setEventHandlers(
			@SuppressWarnings("unchecked") EventHandler<T>... eventHandlers) {
		this.eventHandlers = eventHandlers;
		return this;
	}

	public ProducerType getProducerType() {
		return producerType;
	}

	public BypassBuilder<T> setProducerType(ProducerType producerType) {
		this.producerType = producerType;
		return this;
	}

	public WaitStrategy getWaitStrategy() {
		return waitStrategy;
	}

	public BypassBuilder<T> setWaitStrategy(WaitStrategy waitStrategy) {
		this.waitStrategy = waitStrategy;
		return this;
	}

	public ThreadFactory getThreadFactory() {
		return threadFactory;
	}

	public BypassBuilder<T> setThreadFactory(ThreadFactory threadFactory) {
		this.threadFactory = threadFactory;
		return this;
	}

	public long getShutdownTimeout() {
		return shutdownTimeout;
	}

	public BypassBuilder<T> setShutdownTimeout(long shutdownTimeout) {
		this.shutdownTimeout = shutdownTimeout;
		return this;
	}

}
