package cn.ms.neural.moduler.extension.flowrate.core.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.ms.neural.common.exception.flowrate.support.CCTRejectREQException;
import cn.ms.neural.moduler.extension.flowrate.conf.FlowRateConf;
import cn.ms.neural.moduler.extension.flowrate.entity.FlowrateRule;
import cn.ms.neural.moduler.extension.flowrate.processor.IFlowRateProcessor;

/**
 * 非阻塞式并发控制
 * <br>
 * int availablePermits() ：返回此信号量中当前可用的许可证数。<br>
 * int getQueueLength()：返回正在等待获取许可证的线程数。<br>
 * boolean hasQueuedThreads() ：是否有线程正在等待获取许可证。<br>
 * void reducePermits(int reduction) ：减少reduction个许可证。是个protected方法。<br>
 * Collection getQueuedThreads() ：返回所有等待获取许可证的线程集合。是个protected方法。<br>
 * <br>
 * <br>
 * tryAcquire():尝试获取一个许可，若获取成功，则立即返回true，若获取失败，则立即返回false<br>
 * tryAcquire(long timeout, TimeUnit unit):尝试获取一个许可，若在指定的时间内获取成功，则立即返回true，否则则立即返回false<br>
 * tryAcquire(int permits):尝试获取permits个许可，若获取成功，则立即返回true，若获取失败，则立即返回false<br>
 * tryAcquire(int permits, long timeout, TimeUnit unit):尝试获取permits个许可，若在指定的时间内获取成功，则立即返回true，否则则立即返回false<br>
 * 
 * cct:switch=true/false&fastacquire=true/false&timeout=5000
 * flowrate.list=cct:true:flase:5000:key1,cct:true:true:5000:key2
 * 
 * @author lry
 */
public class NIOSemaphoreFlowrate<REQ, RES> {
	
	private static final Logger logger=LogManager.getLogger(NIOSemaphoreFlowrate.class);
	
	private Semaphore semaphore;
	private FlowrateRule flowrateRule;
	
	public NIOSemaphoreFlowrate(FlowrateRule flowrateRule) {
		this.flowrateRule=flowrateRule;
		
		semaphore = new Semaphore(this.flowrateRule.getPermits(),this.flowrateRule.isFair());
	}
	
	public RES semaphore(FlowRateConf flowrateConf, REQ flowrateREQ, IFlowRateProcessor<REQ, RES> processor) throws Throwable {
		try {
			boolean check=semaphore.tryAcquire(flowrateRule.getTimeout(),TimeUnit.valueOf(flowrateRule.getTimeUnit())); // 获取许可
			if(logger.isFatalEnabled()){
				logger.fatal("线程" + Thread.currentThread().getName() + "进入,当前已有" + (flowrateRule.getPermits()-semaphore.availablePermits()) + "个并发");
			}
			if(check){
				if(semaphore.availablePermits()>=0){
					return processor.processor(flowrateREQ, flowrateREQ);
				}else{
					throw new CCTRejectREQException("并发超额,拒绝请求");
				}
			}else{
				throw new CCTRejectREQException("并发拒绝请求");
			}
		} finally {
			semaphore.release();// 访问完后，释放
			if(logger.isFatalEnabled()){
				logger.fatal("线程" + Thread.currentThread().getName() + "已离开，当前有" + (flowrateRule.getPermits()-semaphore.availablePermits()) + "个并发");    
			}
		}
	}
	
	public int availablePermits() {
		return semaphore.availablePermits();
	}
}
