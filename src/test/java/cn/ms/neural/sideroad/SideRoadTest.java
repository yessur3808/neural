package cn.ms.neural.sideroad;

import cn.ms.neural.sideroad.SideRoad;
import cn.ms.neural.sideroad.SideRoadBuilder;
import cn.ms.neural.sideroad.SideRoadWrapper;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;

public class SideRoadTest {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		SideRoadBuilder<SideRoadEvent> builder = new SideRoadBuilder<SideRoadEvent>();
		builder.setBypassWrapper(new SideRoadWrapper<SideRoadEvent>() {
			@Override
			public void wrapper(SideRoadEvent input, SideRoadEvent data) {
				data.set(input.getValue());
			}
		});
		builder.setEventFactory(new EventFactory<SideRoadEvent>() {
			@Override
			public SideRoadEvent newInstance() {
				return new SideRoadEvent();
			}
		});
		
		builder.setEventHandlers(new EventHandler<SideRoadEvent>() {
			@Override
			public void onEvent(SideRoadEvent event, long sequence, boolean endOfBatch) throws Exception {
				System.out.println("1-BypassEvent:"+event+", sequence:"+sequence);
			}
		}, new EventHandler<SideRoadEvent>() {
			@Override
			public void onEvent(SideRoadEvent event, long sequence, boolean endOfBatch) throws Exception {
				System.out.println("2-BypassEvent:"+event+", sequence:"+sequence);
			}
		});
		

		SideRoad<SideRoadEvent> bypassService = new SideRoad<SideRoadEvent>();
		bypassService.start(builder);
		for (int i = 0; i < 10; i++) {
			bypassService.publish(new SideRoadEvent());
		}
	}

}
