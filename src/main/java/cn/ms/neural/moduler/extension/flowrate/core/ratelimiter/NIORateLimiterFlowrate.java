package cn.ms.neural.moduler.extension.flowrate.core.ratelimiter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.util.concurrent.RateLimiter;

import cn.ms.neural.moduler.extension.flowrate.entity.FlowrateRule;

/**
 * 非阻塞式的QPS流量控制器
 * 
 * @author lry
 */
public class NIORateLimiterFlowrate<REQ, RES> {

	private static final Logger logger=LogManager.getLogger(NIORateLimiterFlowrate.class);
	
	private FlowrateRule flowrateRule;
	private RateLimiter rateLimiter;
	private CountDownLatch latch; 
	 
	public NIORateLimiterFlowrate(FlowrateRule flowrateRule) {
		this.flowrateRule=flowrateRule;
		latch = new CountDownLatch((int)this.flowrateRule.getPermitsPerSecond()); 
		
		TimeUnit timeUnit = TimeUnit.valueOf(this.flowrateRule.getTimeUnit());
		rateLimiter = RateLimiter.create(this.flowrateRule.getPermitsPerSecond(), this.flowrateRule.getWarmupPeriod(), timeUnit);
	}
	
	/**
	 * QPS流量控制校验器
	 * 
	 * @return
	 * @throws Throwable
	 */
	public boolean rateLimiter() throws Throwable {
		boolean check=false;
		try {
			latch.countDown();
			return check=rateLimiter.tryAcquire();	
		} finally {
			if(logger.isInfoEnabled()){
				logger.info("QPS校验结果:"+check);
			}
		}
		
	}

	public double getRate() {
		return this.flowrateRule.getPermitsPerSecond()-latch.getCount();
	}
	
}
