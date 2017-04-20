package cn.ms.neural.status;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 垃圾回收指标集合
 * 
 * @author: lry
 */
public class GarbageCollectorMetricSet {
	
	private static final Pattern WHITESPACE = Pattern.compile("[\\s]+");
	private final List<GarbageCollectorMXBean> garbageCollectors;

	/**
	 * Creates a new set of gauges for all discoverable garbage collectors.
	 */
	public GarbageCollectorMetricSet() {
		this(ManagementFactory.getGarbageCollectorMXBeans());
	}

	/**
	 * Creates a new set of gauges for the given collection of garbage collectors.
	 *
	 * @param garbageCollectors the garbage collectors
	 */
	public GarbageCollectorMetricSet(Collection<GarbageCollectorMXBean> garbageCollectors) {
		this.garbageCollectors = new ArrayList<GarbageCollectorMXBean>(garbageCollectors);
	}

	public Map<String, String> getMetrics() {
		final Map<String, String> gauges = new LinkedHashMap<String, String>();
		for (final GarbageCollectorMXBean gc : garbageCollectors) {
			final String name = WHITESPACE.matcher(gc.getName()).replaceAll("-");
			gauges.put(name + ".count", Long.toString(gc.getCollectionCount()));
			// 单位：从毫秒转换为秒
			gauges.put(name + ".time", Long.toString(gc.getCollectionTime() / 1000));
		}

		return gauges;
	}
	
}