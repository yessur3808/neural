package cn.ms.neural.common.perf;

import java.util.UUID;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;

public class ContiPerfTest {
	
	@Rule
	public ContiPerfRule i = new ContiPerfRule();

	@Test
	@PerfTest(invocations = 1000000, threads = 16)
	@Required(max = 1200, average = 250, totalTime = 60000)
	public void test() throws Exception {
		System.out.println(UUID.randomUUID().toString());
	}

}