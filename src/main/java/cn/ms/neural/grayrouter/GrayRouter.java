package cn.ms.neural.grayrouter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * The Gray Router.<br>
 * <br>
 * 原理: 路由参数的Key/Value和Service => Version <br>
 * <br>
 * 
 * @author lry
 */
public class GrayRouter {

	/**
	 * 灰度路由粒度
	 */
	String[] granularities;
	/**
	 * 灰度路由规则,数据结构:Map<rule, version>
	 */
	Map<String, String> routerRuleMap = new ConcurrentHashMap<String, String>();

	public void initialize(String[] granularities) {
		this.granularities = granularities;
	}

	public void notifys(Map<String, String> routerRuleMap) {
		this.routerRuleMap = routerRuleMap;
	}

	public RouterResult doGrayRouter(Map<String, Object> routers, String service) {
		if (granularities == null || granularities.length == 0) {
			return new RouterResult(ResultType.NONINITIALIZE);
		}

		StringBuffer routerBuffer = new StringBuffer();
		for (int i = 0; i < granularities.length; i++) {
			String granularity = granularities[i];
			routerBuffer.append(routers.get(granularity));
			if (i < granularities.length - 1) {
				routerBuffer.append(",");
			}
		}
		String routeRuleRegex = routerBuffer.toString();

		for (Map.Entry<String, String> entry : routerRuleMap.entrySet()) {
			if (matches(entry.getKey(), routeRuleRegex)) {// 进行路由KEY的匹配
				return new RouterResult(ResultType.SUCCESS, entry.getValue());
			}
		}

		return new RouterResult(ResultType.NOTFOUND);
	}

	/**
	 * 匹配校验规则<br>
	 * <br>
	 * 常用匹配规则:<br>
	 * .-->匹配任意一个字符串<br>
	 * a.*-->匹配以a为开头的任意字符串<br>
	 * m$-->匹配以m为结尾的字符串<br>
	 * (a|b|c)-->匹配a、b、c中的任意一个字符串,即枚举匹配<br>
	 * [1-9]-->匹配1到9中的任意一个数<br>
	 * [a-z]-->匹配a到z中的人一个小写字母<br>
	 * 
	 * @param regex
	 * @param data
	 * @return
	 */
	private boolean matches(String regex, String data) {
		if (regex.equals(data)) {
			return true;
		}

		if (Pattern.matches(regex, data)) {
			return true;
		}

		return false;
	}

}