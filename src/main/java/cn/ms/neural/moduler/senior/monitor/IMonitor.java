package cn.ms.neural.moduler.senior.monitor;

import java.util.Map;

import cn.ms.neural.common.exception.monitor.MonitorCollectException;
import cn.ms.neural.common.exception.monitor.MonitorLookupException;
import cn.ms.neural.moduler.IModuler;

/**
 * 监控中心
 * <br>
 * 1.实时交易数据监控<br>
 * 2.实时状态监控数据<br>
 * <br>
 * @author lry
 * @version v1.0
 */
public interface IMonitor<REQ, RES> extends IModuler<REQ, RES>{

	/**
	 * 收集数据
	 * 
	 * @param data
	 * @param args
	 * @throws MonitorCollectException
	 */
	void collect(Map<String,Object> data, Object...args) throws MonitorCollectException;
	
	/**
	 * 查询数据
	 * @param data
	 * @param args
	 * @return
	 * @throws MonitorLookupException
	 */
	Map<String,Object> lookup(Map<String,Object> data, Object...args) throws MonitorLookupException;
	
}
