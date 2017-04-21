package cn.ms.neural.bypass;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;

public class BypassServiceTest {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		BypassBuilder<BypassEvent> builder = new BypassBuilder<BypassEvent>();
		builder.setBypassWrapper(new BypassWrapper<BypassEvent>() {
			@Override
			public void wrapper(BypassEvent input, BypassEvent data) {
				data.set(input.getValue());
			}
		});
		builder.setEventFactory(new EventFactory<BypassEvent>() {
			@Override
			public BypassEvent newInstance() {
				return new BypassEvent();
			}
		});
		
		builder.setEventHandlers(new EventHandler<BypassEvent>() {
			@Override
			public void onEvent(BypassEvent event, long sequence, boolean endOfBatch) throws Exception {
				System.out.println("1-BypassEvent:"+event+", sequence:"+sequence);
			}
		}, new EventHandler<BypassEvent>() {
			@Override
			public void onEvent(BypassEvent event, long sequence, boolean endOfBatch) throws Exception {
				System.out.println("2-BypassEvent:"+event+", sequence:"+sequence);
			}
		});
		

		BypassService<BypassEvent> bypassService = new BypassService<BypassEvent>();
		bypassService.start(builder);
		for (int i = 0; i < 10; i++) {
			bypassService.publish(new BypassEvent());
		}
	}

}
