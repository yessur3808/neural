package cn.ms.neural.throttle.support;

import org.junit.Assert;
import org.junit.Test;

public class FlowUnitTest {

	@Test
	public void unitTest() {
		Assert.assertEquals(1125899906842624l, FlowUnit.PB.toByte(1));
		Assert.assertEquals(1099511627776l, FlowUnit.TB.toByte(1));
		// 测试TPS流量统计能够支持最大流量T为
		Assert.assertEquals(8388607l, Long.MAX_VALUE / FlowUnit.TB.toByte(1));
	}
	
}