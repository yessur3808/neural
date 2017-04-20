package cn.ms.neural.status;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.ms.micro.extension.SpiMeta;

/**
 * MemoryStatus
 * 
 * @author lry
 */
@SpiMeta(name = "memory")
public class MemoryStatusChecker implements StatusChecker {

	private final MemoryMXBean mxBean = ManagementFactory.getMemoryMXBean();

    /**
     * 将long型数值转换为字符串，单位从字节转换为MB
     */
    private static String toMBStr(Long value) {
    	return Long.toString(value / 1024 / 1024);
    }
    
    private static String toPercentStr(long used, long max) {
    	Double percent = (used * 100.0) / max;
    	return String.format("%.2f", percent);
    }
    
    /**
     * 获取Jvm信息<br><br>
     * 1.<font color="red">jvm_memory_init</font>：总初始内存容量（以字节为单位）<br>
     * 2.<font color="red">jvm_memory_used</font>：当前已经使用的总内存量（以字节为单位）<br>
     * 3.<font color="red">jvm_memory_max</font>：可以用于内存管理的最大内存总量（以字节为单位）<br>
     * 4.<font color="red">jvm_memory_committed</font>：保证可以由Java 虚拟机使用的内存总量（以字节为单位）<br>
     * 5.<font color="red">jvm_heap_init</font>：对象分配的堆的当前内存使用量的初始内存容量（以字节为单位）<br>
     * 6.<font color="red">jvm_heap_used</font>：对象分配的堆的当前内存使用量的已经使用的总内存量（以字节为单位）<br>
     * 7.<font color="red">jvm_heap_max</font>：对象分配的堆的当前内存使用量的最大内存总量（以字节为单位）<br>
     * 8.<font color="red">jvm_heap_committed</font>：对象分配的堆的当前内存使用量的保证可以由Java虚拟机使用的内存总量（以字节为单位）<br>
     * 9.<font color="red">jvm_heap_usage</font>：对象分配的堆的当前内存的使用率（单位%）<br>
     * 10.<font color="red">jvm_nonheap_init</font>：Java虚拟机使用的非堆内存的当前内存使用量的初始内存容量（以字节为单位）<br>
     * 11.<font color="red">jvm_nonheap_used</font>：Java虚拟机使用的非堆内存的当前内存使用量的已经使用的总内存量（以字节为单位）<br>
     * 12.<font color="red">jvm_nonheap_max</font>：Java虚拟机使用的非堆内存的当前内存使用量的最大内存总量（以字节为单位）<br>
     * 13.<font color="red">jvm_nonheap_committed</font>：Java虚拟机使用的非堆内存的当前内存使用量的保证可以由Java虚拟机使用的内存总量（以字节为单位）<br>
     * 14.<font color="red">jvm_nonheap_usage</font>：Java虚拟机使用的非堆内存的当前内存的使用率（单位%）<br>
     * @return
     */
    public Map<String, String> getMetrics() {
        final Map<String, String> gauges = new LinkedHashMap<String, String>();

        /**
         * getHeapMemoryUsage():对象分配的堆的当前内存使用量的初始内存容量
         * getNonHeapMemoryUsage():Java虚拟机使用的非堆内存的当前内存使用量的初始内存容量
         */
        //总初始内存容量（以字节为单位）
        gauges.put("jvm_memory_init", toMBStr(mxBean.getHeapMemoryUsage().getInit() +mxBean.getNonHeapMemoryUsage().getInit()));
        //当前已经使用的总内存量（以字节为单位）
        gauges.put("jvm_memory_used", toMBStr(mxBean.getHeapMemoryUsage().getUsed() +mxBean.getNonHeapMemoryUsage().getUsed()));
        //可以用于内存管理的最大内存总量（以字节为单位）
        gauges.put("jvm_memory_max", toMBStr(mxBean.getHeapMemoryUsage().getMax() +mxBean.getNonHeapMemoryUsage().getMax()));
        //保证可以由Java 虚拟机使用的内存总量（以字节为单位）
        gauges.put("jvm_memory_committed", toMBStr(mxBean.getHeapMemoryUsage().getCommitted() +mxBean.getNonHeapMemoryUsage().getCommitted()));
        
        //对象分配的堆的当前内存使用量的初始内存容量（以字节为单位）
        gauges.put("jvm_heap_init", toMBStr(mxBean.getHeapMemoryUsage().getInit()));
        //对象分配的堆的当前内存使用量的已经使用的总内存量（以字节为单位）
        gauges.put("jvm_heap_used", toMBStr(mxBean.getHeapMemoryUsage().getUsed()));
        //对象分配的堆的当前内存使用量的最大内存总量（以字节为单位）
        gauges.put("jvm_heap_max", toMBStr(mxBean.getHeapMemoryUsage().getMax()));
        //对象分配的堆的当前内存使用量的保证可以由Java虚拟机使用的内存总量（以字节为单位）
        gauges.put("jvm_heap_committed", toMBStr(mxBean.getHeapMemoryUsage().getCommitted()));

        //对象分配的堆的当前内存的使用率（单位%）
        final MemoryUsage usage = mxBean.getHeapMemoryUsage();
        gauges.put("jvm_heap_usage", toPercentStr(usage.getUsed(), usage.getMax()));
        
        //Java虚拟机使用的非堆内存的当前内存使用量的初始内存容量（以字节为单位）
        gauges.put("jvm_nonheap_init", toMBStr(mxBean.getNonHeapMemoryUsage().getInit()));
        //Java虚拟机使用的非堆内存的当前内存使用量的已经使用的总内存量（以字节为单位）
        gauges.put("jvm_nonheap_used", toMBStr(mxBean.getNonHeapMemoryUsage().getUsed()));
        //Java虚拟机使用的非堆内存的当前内存使用量的最大内存总量（以字节为单位）
        gauges.put("jvm_nonheap_max", toMBStr(mxBean.getNonHeapMemoryUsage().getMax()));
        //Java虚拟机使用的非堆内存的当前内存使用量的保证可以由Java虚拟机使用的内存总量（以字节为单位）
        gauges.put("jvm_nonheap_committed", toMBStr(mxBean.getNonHeapMemoryUsage().getCommitted()));
        
        //Java虚拟机使用的非堆内存的当前内存的使用率（单位%）
        final MemoryUsage noHeapUsage = mxBean.getNonHeapMemoryUsage();
        gauges.put("jvm_nonheap_usage", toPercentStr(noHeapUsage.getUsed(), noHeapUsage.getMax()));

        return gauges;
    }
    
    @Override
	public Status check() {
		Runtime runtime = Runtime.getRuntime();
		long freeMemory = runtime.freeMemory();
		long totalMemory = runtime.totalMemory();
		long maxMemory = runtime.maxMemory();
		boolean ok = (maxMemory - (totalMemory - freeMemory) > 2048); // 剩余空间小于2M报警
		String msg = "max:" + (maxMemory / 1024 / 1024) + "M,total:"
				+ (totalMemory / 1024 / 1024) + "M,used:"
				+ ((totalMemory / 1024 / 1024) - (freeMemory / 1024 / 1024))
				+ "M,free:" + (freeMemory / 1024 / 1024) + "M";

		return new Status(ok ? Status.Level.OK : Status.Level.WARN, msg);
	}

}