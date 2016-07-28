package cn.ms.neural.common.perf;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import cn.ms.neural.common.NamedThreadFactory;
import cn.ms.neural.common.logger.ILogger;
import cn.ms.neural.common.logger.LoggerManager;

/**
 * 性能日志
 * 
 * @author lry
 */
public enum PerfLog {

	INSTANCE;
	private static final ILogger sysDefaultLog=LoggerManager.getSysDefaultLog();
	private static final ILogger bizPerfLog=LoggerManager.getBizPerfLog();

	private static final ConcurrentHashMap<String, Long[]> perfMap=new ConcurrentHashMap<String, Long[]>();
	
	//检查周期
	private long retryPeriod=1000*60;
	//失败重试定时器，定时检查是否有请求失败，如有，无限次重试
    private ScheduledFuture<?> retryFuture;
	private final ScheduledExecutorService retryExecutor = Executors.newScheduledThreadPool(1, new NamedThreadFactory("Neural-PerfLog-Timer", true));
	
	PerfLog() {
		try {
	        this.retryFuture = retryExecutor.scheduleWithFixedDelay(new Runnable() {
	            public void run() {
	                try {
	                	for (Map.Entry<String, Long[]> entry:perfMap.entrySet()) {
	                		Long[] array=entry.getValue();
	                		if(bizPerfLog.isInfoEnabled()){
	                			bizPerfLog.info(String.format("The avg perf [%s, %s ms]", entry.getKey(), (double)(array[1]/array[0])));
	                		}
						}
	                } catch (Throwable t) { // 防御性容错
	                	sysDefaultLog.error("The PerfLog unexpected error occur at failed retry, cause: " + t.getMessage(), t);
	                } finally {
	                	perfMap.clear();
					}
	            }
	        }, retryPeriod, retryPeriod, TimeUnit.MILLISECONDS);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void collect(String key, long expend) {
		Long[] avgExpend=perfMap.get(key);
		if(avgExpend==null){
			avgExpend=new Long[2];
			avgExpend[0]=0l;
			avgExpend[1]=0l;
		}
		avgExpend[0]=avgExpend[0]+1;
		avgExpend[1]=avgExpend[1]+expend;
		
		perfMap.put(key, avgExpend);
	}
	
	public void destory() {
		try {
			if(retryFuture!=null){
				retryFuture.cancel(true);	
			}
        } catch (Throwable t) {
            t.printStackTrace();
        }
	}
	
}
