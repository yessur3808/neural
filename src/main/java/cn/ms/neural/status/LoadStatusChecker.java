package cn.ms.neural.status;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;

import cn.ms.neural.extension.SpiMeta;

/**
 * Load Status
 * 
 * @author lry
 */
@SpiMeta(name = "load")
public class LoadStatusChecker implements StatusChecker {

    public Status check() {
    	OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
    	double load;
    	try {
    	    Method method = OperatingSystemMXBean.class.getMethod("getSystemLoadAverage", new Class<?>[0]);
    	    load = (Double)method.invoke(operatingSystemMXBean, new Object[0]);
    	} catch (Throwable e) {
    	    load = -1;
    	}
    	int cpu = operatingSystemMXBean.getAvailableProcessors();
    	
        return new Status(load < 0 ? Status.Level.UNKNOWN : (load < cpu ? Status.Level.OK : Status.Level.WARN), (load < 0 ? "" : "load:" + load + ",") + "cpu:" + cpu);
    }

}