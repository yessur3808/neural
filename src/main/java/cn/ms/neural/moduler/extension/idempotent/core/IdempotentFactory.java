package cn.ms.neural.moduler.extension.idempotent.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.ms.neural.common.NamedThreadFactory;
import cn.ms.neural.common.SystemClock;
import cn.ms.neural.moduler.extension.idempotent.Idempotent;
import cn.ms.neural.moduler.extension.idempotent.conf.IdempotentConf;
import cn.ms.neural.moduler.extension.idempotent.entity.IdempotentStorage;
import cn.ms.neural.moduler.extension.idempotent.processor.IdempotentProcessor;

/**
 * 幂等处理中心
 * 
 * @author lry
 *
 * @param <REQ>
 * @param <RES>
 */
public class IdempotentFactory<REQ, RES> implements Idempotent<REQ, RES>{

	private static final Logger logger=LogManager.getLogger(IdempotentFactory.class);
	
	/**
	 * 检查周期
	 */
	private long retryPeriod=1000*30;
	/**
	 * 过期周期
	 */
	private long expireCycle=1000*60*2;
	/**
	 * 容量大小
	 */
	private int idempStorCapacity=10000;
	/**
	 * 持久化仓库
	 */
	private ConcurrentHashMap<String, IdempotentStorage<RES>> idempotentStorage;
	/**
	 * 失败重试定时器，定时检查是否有请求失败，如有，无限次重试
	 */
    private ScheduledFuture<?> retryFuture;
    /**
     * 定时任务执行器
     */
    private final ScheduledExecutorService retryExecutor = Executors.newScheduledThreadPool(1, new NamedThreadFactory("IdempotentCleanFailedRetryTimer", true));
    
    /**
     * @param expireCycle 过期周期
     * @param idempStorCapacity 容量大小
     */
	public IdempotentFactory() {
		try {
			init();//初始化
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * 清理
	 * 
	 * @throws Throwable
	 */
	protected void cleanup() throws Throwable {
		if(idempotentStorage.isEmpty()){
			return;
		}
		
		for (Map.Entry<String, IdempotentStorage<RES>> entry:idempotentStorage.entrySet()) {
			IdempotentStorage<RES> isStorage=entry.getValue();
			if(isStorage!=null){
				if(SystemClock.now()-isStorage.getTime()>expireCycle){
					idempotentStorage.remove(isStorage.getId());
				}
			}
		}
	}

	@Override
	public void init() throws Throwable {
		idempotentStorage=new ConcurrentHashMap<String, IdempotentStorage<RES>>(idempStorCapacity);
		try {
	        this.retryFuture = retryExecutor.scheduleWithFixedDelay(new Runnable() {
	            public void run() {
	                try {
	                    cleanup();
	                } catch (Throwable t) { // 防御性容错
	                    logger.error("Unexpected error occur at failed retry, cause: " + t.getMessage(), t);
	                }
	            }
	        }, retryPeriod, retryPeriod, TimeUnit.MILLISECONDS);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public RES idempotent(IdempotentConf idempotentConf, REQ idempotentREQ, IdempotentProcessor<REQ, RES> idempotentHandler) throws Throwable {
		if(!idempotentConf.isIdempotentEnable()){//未开启开关
			return idempotentHandler.handler(idempotentConf, idempotentREQ);
		}
		
		IdempotentStorage<RES> storage= idempotentStorage.get(idempotentConf.getIdempotentId());
		if(storage==null){//没有执行过,则执行
			RES res = idempotentHandler.handler(idempotentConf, idempotentREQ);
			if(res!=null){//幂等记录结果
				this.storage(idempotentConf, idempotentREQ, res);
			}
			
			return res;
		}else{//有幂等结果,直接返回
			
			//TODO 待统计命中率
			
			return storage.getRes();			
		}
	}

	@Override
	public void storage(IdempotentConf idempotentConf, REQ idempotentREQ, RES idempotentRES) throws Throwable {
		idempotentStorage.put(idempotentConf.getIdempotentId(), new IdempotentStorage<RES>(idempotentConf.getIdempotentId(), SystemClock.now(), idempotentRES));
	}
	
	@Override
	public void destory() throws Throwable{
		try {
			if(idempotentStorage!=null){
				idempotentStorage.clear();	
			}
			if(retryFuture!=null){
				retryFuture.cancel(true);	
			}
        } catch (Throwable t) {
            logger.warn(t.getMessage(), t);
        }
	}

}
