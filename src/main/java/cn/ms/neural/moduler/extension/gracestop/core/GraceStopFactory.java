package cn.ms.neural.moduler.extension.gracestop.core;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import cn.ms.neural.common.NamedThreadFactory;
import cn.ms.neural.common.concurrent.LongAdder;
import cn.ms.neural.common.exception.gracestop.GraceStopedException;
import cn.ms.neural.common.exception.gracestop.GraceWaitStopException;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.conf.Conf;
import cn.ms.neural.moduler.extension.gracestop.IGraceStop;
import cn.ms.neural.moduler.extension.gracestop.conf.GraceStopConf;
import cn.ms.neural.moduler.extension.gracestop.processor.IGraceStopProcessor;
import cn.ms.neural.moduler.extension.gracestop.type.GraceStopBootType;
import cn.ms.neural.moduler.extension.gracestop.type.GraceStopStatusType;

public class GraceStopFactory<REQ, RES> implements IGraceStop<REQ, RES> {

	/**
	 * 模块中心
	 */
	private Moduler<REQ, RES> moduler;
	
	/**
	 * 实时流量计数器
	 */
	private final LongAdder longAdder = new LongAdder();
	
	@Override
	public void setModuler(Moduler<REQ, RES> moduler) {
		this.moduler=moduler;
		
		//每次变更通知后进行检查
		this.schedule(moduler.getUrl().getModulerParameter(Conf.GRACESTOP, 
				GraceStopConf.SHUTDOWN_TIMEOUT_KEY, GraceStopConf.SHUTDOWN_TIMEOUT_DEFAULT_VALUE));
	}
	
	@Override
	public void init() throws Throwable {
		try {
    		moduler.setUrl(moduler.getUrl().addParameter(GraceStopConf.STARTUP_TIME_KEY, System.currentTimeMillis()));//启动时间
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	@Override
	public RES gracestop(REQ req, IGraceStopProcessor<REQ, RES> processor, Object... args) {
		//开机状态校验
		String status=moduler.getUrl().getModulerParameter(Conf.GRACESTOP, 
				GraceStopConf.STATUS_KEY, GraceStopConf.STATUS_DEFAULT_VALUE.getVal());
		if (GraceStopStatusType.OFFLINE.getVal().equals(status)) {//已离线
			throw new GraceStopedException();
		}
		
		//开/关机开关校验
		String bootOrStop=moduler.getUrl().getModulerParameter(Conf.GRACESTOP, 
				GraceStopConf.SWITCH_KEY, GraceStopConf.SWITCH_DEFAULT_VALUE.getVal());
		if (GraceStopBootType.BOOT.getVal().equals(bootOrStop)) {//正常开机
			try {
				longAdder.increment();//加流量
				return processor.processor(req, args);
			} finally {
				longAdder.decrement();//减流量
			}
		}else{//等待关机,拒绝接入流量
			throw new GraceWaitStopException();
		}
	}

	@Override
	public void destory() throws Throwable {
		
	}
	
	private Boolean schedule(long delay) {
		String bootOrStop=moduler.getUrl().getModulerParameter(Conf.GRACESTOP, 
    			GraceStopConf.SWITCH_KEY, GraceStopConf.SWITCH_DEFAULT_VALUE.getVal());
		if (GraceStopBootType.BOOT.getVal().equals(bootOrStop)) {//正常开机
			return false;
		}
		
		moduler=new Moduler<>();
		ScheduledExecutorService scheduledExecutorService=null;
		ScheduledFuture<Boolean> scheduledFuture=null;
		try {
			scheduledExecutorService = Executors.newScheduledThreadPool(1, new NamedThreadFactory("GracestopFactoryRetryTimer", true));
			scheduledFuture=scheduledExecutorService.schedule(new Callable<Boolean>() {
				public Boolean call() throws Exception {
					//开/关机开关校验
                	String bootOrStop=moduler.getUrl().getModulerParameter(Conf.GRACESTOP, 
                			GraceStopConf.SWITCH_KEY, GraceStopConf.SWITCH_DEFAULT_VALUE.getVal());
            		if (GraceStopBootType.BOOT.getVal().equals(bootOrStop)) {//正常开机
            			
            			String status=moduler.getUrl().getModulerParameter(Conf.GRACESTOP, 
            					GraceStopConf.STATUS_KEY, GraceStopConf.STATUS_DEFAULT_VALUE.getVal());
            			if (GraceStopStatusType.ONLINE.getVal().equals(status)) {//已停机
            				moduler.setUrl(moduler.getUrl().addModulerParameter(Conf.GRACESTOP, 
            						GraceStopConf.SHUTDOWN_TIME_KEY, System.currentTimeMillis()));//停机时间
            				
            				return true;
            			}
            		}
					return false;
				}
		    },delay,TimeUnit.MILLISECONDS);
			
			return scheduledFuture.get();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if(scheduledFuture!=null){
				scheduledFuture.cancel(true);
			}
			if(scheduledExecutorService!=null){
				scheduledExecutorService.shutdown();
			}
		}
	}
	
	public static void main(String[] args) throws Throwable{
		System.out.println(new GraceStopFactory<>().schedule(1000));;
	}
}
