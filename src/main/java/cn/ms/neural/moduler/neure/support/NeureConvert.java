package cn.ms.neural.moduler.neure.support;

import cn.ms.neural.Conf;
import cn.ms.neural.common.URL;
import cn.ms.neural.moduler.neure.conf.NeureConf;
import cn.ms.neural.moduler.neure.entity.HystrixSetter;
import cn.ms.neural.moduler.neure.entity.NeureEntity;

public class NeureConvert {

	public static NeureEntity convert(URL url) {
		NeureEntity entity=new NeureEntity();
		//神经内核开关
		entity.setNeureSwitch(url.getModulerParameter(Conf.NEURE, NeureConf.SWITCH_KEY, NeureConf.SWITCH_DEF_VAL));
		//容错开关
		entity.setFallback(url.getModulerParameter(Conf.NEURE, NeureConf.FALLBACK_SWITCH_KEY, NeureConf.FALLBACK_SWITCH_DEF_VAL));
		//线程之间参数传递
		entity.setThreadContext(url.getModulerParameter(Conf.NEURE, NeureConf.THREADCONTEXT_SWITCH_KEY, NeureConf.THREADCONTEXT_SWITCH_DEF_VAL));
		//最大重试次数
		entity.setMaxRetryTimes(url.getModulerParameter(Conf.NEURE, NeureConf.MAX_RETRYTIMES_KEY, NeureConf.MAX_RETRYTIMES_DEF_VAL));
		
		HystrixSetter hystrixSetter=new HystrixSetter();
		//执行隔离线程超时毫秒,默认为1000ms
		hystrixSetter.setEitTimeout(url.getModulerParameter(Conf.NEURE, NeureConf.EITTIMEOUT_KEY, NeureConf.EITTIMEOUT_DEF_VAL));
		//执行超时时间,默认为1000ms
		hystrixSetter.setEtimeout(url.getModulerParameter(Conf.NEURE, NeureConf.ETIMEOUT_KEY, NeureConf.ETIMEOUT_DEF_VAL));
		//
		hystrixSetter.setFismRequests(url.getModulerParameter(Conf.NEURE, NeureConf.FISMREQUESTS_KEY, NeureConf.FISMREQUESTS_DEF_VAL));
		
		//
		hystrixSetter.setCbErrorRate(url.getModulerParameter(Conf.NEURE, NeureConf.CBERRORRATE_KEY, NeureConf.CBERRORRATE_DEF_VAL));
		//
		hystrixSetter.setCbRequests(url.getModulerParameter(Conf.NEURE, NeureConf.CBREQUESTS_KEY, NeureConf.CBREQUESTS_DEF_VAL));
		//
		hystrixSetter.setCbSleepWindow(url.getModulerParameter(Conf.NEURE, NeureConf.CBSLEEPWINDOW_KEY, NeureConf.CBSLEEPWINDOW_DEF_VAL));
		
		//
		hystrixSetter.setThreadPoolCoreSize(url.getModulerParameter(Conf.NEURE, NeureConf.THREADPOOLCORESIZE_KEY, NeureConf.THREADPOOLCORESIZE_DEF_VAL));
		//
		hystrixSetter.setThreadPoolQueueSize(url.getModulerParameter(Conf.NEURE, NeureConf.THREADPOOLQUEUESIZE_KEY, NeureConf.THREADPOOLQUEUESIZE_DEF_VAL));
		
		
		entity.setHystrixSetter(hystrixSetter);
		
		return entity;
	}
	
}
