package cn.ms.neural.moduler.neure.support;

import cn.ms.neural.common.URL;
import cn.ms.neural.moduler.Conf;
import cn.ms.neural.moduler.neure.conf.NeureConf;
import cn.ms.neural.moduler.neure.entity.HystrixSetter;
import cn.ms.neural.moduler.neure.entity.NeureEntity;

public class NeureSupport {

	public static NeureEntity convert(URL url) {
		NeureEntity entity=new NeureEntity();
		entity.setNeureSwitch(url.getModulerParameter(Conf.NEURE, NeureConf.SWITCH_KEY, NeureConf.SWITCH_DEF_VAL));
		entity.setFallbackSwitch(url.getModulerParameter(Conf.NEURE, NeureConf.FALLBACK_SWITCH_KEY, NeureConf.FALLBACK_SWITCH_DEF_VAL));
		entity.setThreadContextSwitch(url.getModulerParameter(Conf.NEURE, NeureConf.THREADCONTEXT_SWITCH_KEY, NeureConf.THREADCONTEXT_SWITCH_DEF_VAL));
		entity.setRetryTimes(url.getModulerParameter(Conf.NEURE, NeureConf.RETRYTIMES_KEY, NeureConf.RETRYTIMES_DEF_VAL));
		
		HystrixSetter hystrixSetter=new HystrixSetter();
		hystrixSetter.setEitTimeout(url.getModulerParameter(Conf.NEURE, NeureConf.EITTIMEOUT_KEY, NeureConf.EITTIMEOUT_DEF_VAL));
		hystrixSetter.seteTimeout(url.getModulerParameter(Conf.NEURE, NeureConf.ETIMEOUT_KEY, NeureConf.ETIMEOUT_DEF_VAL));
		hystrixSetter.setFismRequests(url.getModulerParameter(Conf.NEURE, NeureConf.FISMREQUESTS_KEY, NeureConf.FISMREQUESTS_DEF_VAL));
		
		hystrixSetter.setCbErrorRate(url.getModulerParameter(Conf.NEURE, NeureConf.CBERRORRATE_KEY, NeureConf.CBERRORRATE_DEF_VAL));
		hystrixSetter.setCbRequests(url.getModulerParameter(Conf.NEURE, NeureConf.CBREQUESTS_KEY, NeureConf.CBREQUESTS_DEF_VAL));
		hystrixSetter.setCbSleepWindow(url.getModulerParameter(Conf.NEURE, NeureConf.CBSLEEPWINDOW_KEY, NeureConf.CBSLEEPWINDOW_DEF_VAL));
		
		hystrixSetter.setThreadPoolCoreSize(url.getModulerParameter(Conf.NEURE, NeureConf.THREADPOOLCORESIZE_KEY, NeureConf.THREADPOOLCORESIZE_DEF_VAL));
		hystrixSetter.setThreadPoolQueueSize(url.getModulerParameter(Conf.NEURE, NeureConf.THREADPOOLQUEUESIZE_KEY, NeureConf.THREADPOOLQUEUESIZE_DEF_VAL));
		
		entity.setHystrixSetter(hystrixSetter);
		
		return entity;
	}
	
}
