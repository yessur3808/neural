package cn.ms.neural.moduler.degrade;

import org.junit.Assert;
import org.junit.Test;

import cn.ms.neural.common.exception.degrade.DegradeException;
import cn.ms.neural.moduler.extension.degrade.conf.DegradeConf;
import cn.ms.neural.moduler.extension.degrade.core.DegradeFactory;
import cn.ms.neural.moduler.extension.degrade.processor.IBizDegradeProcessor;
import cn.ms.neural.moduler.extension.degrade.processor.IDegradeProcessor;

/**
 * 服务降级测试
 * 
 * @author lry
 */
public class DegradeTest {

	String reqRoute="这是请求报文信息";
	final String resRoute="这是Route服务的响应报文信息";
	final String resMock="这是Mock服务的响应报文信息";
	final String resBiz="这是业务降级";
	
	/**
	 * 容错直接抛异常服务降级
	 * @throws Exception
	 */
	@Test
	public void degrade2Exception() throws Throwable {
		IBizDegradeProcessor<String, String> bizDegrade=new IBizDegradeProcessor<String, String>() {
			public String processor(DegradeConf conf, String req, IDegradeProcessor<String, String> runner) throws Throwable {
				return resBiz;
			}
		};
		try {
			new DegradeFactory<String, String>().degrade(new DegradeConf(), resBiz, bizDegrade, new IDegradeProcessor<String, String>() {
				public String processor(DegradeConf conf, String req) throws Throwable {
					return req;
				}
				public String mock(DegradeConf degradeConf, String degradeREQ) throws Throwable {
					return degradeREQ;
				}
			});
		} catch (Throwable t) {
			if(t instanceof DegradeException){
				Assert.assertEquals(true, true);
			}else{
				Assert.assertEquals(true, false);
			}
		}
	}
	
}
