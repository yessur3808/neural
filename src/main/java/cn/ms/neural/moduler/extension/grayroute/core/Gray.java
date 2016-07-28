package cn.ms.neural.moduler.extension.grayroute.core;

import java.util.Map;

import cn.ms.neural.moduler.extension.grayroute.IGray;

/**
 * 灰度路由
 * <br>
 * 1.channelId:渠道ID<br>
 * 2.terminalType:终端类型<br>
 * 3.areaName:区域名称<br>
 * 4.:<br>
 * 5.:<br>
 * 6.:<br>
 * 7.:<br>
 * 8.:<br>
 * 9.:<br>
 * @author lry
 *
 * @param <REQ>
 * @param <RES>
 */
public class Gray<REQ, RES> implements IGray<REQ, RES>{

	@Override
	public RES gray(Map<String, Object> grays, REQ req) {
		for (Map.Entry<String, Object> gray:grays.entrySet()) {//遍历当前请求的所有的灰度数据
			System.out.println(gray);
		}
		return null;
	}

}
