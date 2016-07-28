package cn.ms.neural.moduler.extension.grayroute;

import java.util.Map;

/**
 * 灰度发布
 * <br>
 * 1.现有服务如何实现灰度升级?<br>
 * 2.新服务如何实现灰度体验?<br>
 * <br>
 * 终端--->灰度路由--->后端服务
 * 
 * @author lry
 *
 * @param <REQ>
 * @param <RES>
 */
public interface IGray<REQ, RES> {

	RES gray(Map<String, Object> grays, REQ req);
	
}
