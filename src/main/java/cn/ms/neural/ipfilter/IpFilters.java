package cn.ms.neural.ipfilter;

/**
 * IP pattern<br>
 * <br>
 * Available string for parameter value of allow/deny method of Config class<br>
 * simple ip: 1.2.3.4 (exact matching)<br>
 * network range: 1.1.1.64/26 (1.1.1.64 ~ 1.1.1.127) ** 64/26: 26 means a length
 * of network address<br>
 * all: 1.2.3.*, 1.2.*, 1.*, *<br>
 * 
 * @author lry
 */
public class IpFilters {
	
	public static IpFilter create(IpFilterConf ipFilterConf) {
		return new ConfIpFilter(ipFilterConf);
	}

	public static IpFilter createCached(IpFilterConf ipFilterConf) {
		return new CachedIpFilter(create(ipFilterConf));
	}
	
}
