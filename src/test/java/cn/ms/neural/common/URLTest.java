package cn.ms.neural.common;

import org.junit.Test;

import cn.ms.neural.common.utils.NetUtils;

/**
 * URL规则:
 * 
 * 
 * @author lry
 * @version v1.0
 */
public class URLTest {

	@Test
	public void test() {
		URL url=URL.valueOf("http://"+NetUtils.getLocalIp()+":9000/test/?param1=1&param2=asdsad&"
				+ "param3=name=>ds|1|sds|ew&param4=saddddd");
		System.out.println(url.getParameters());
	}
	
}
