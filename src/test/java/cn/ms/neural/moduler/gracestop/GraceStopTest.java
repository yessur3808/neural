package cn.ms.neural.moduler.gracestop;

import org.junit.Assert;
import org.junit.Test;

import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.extension.gracestop.IGraceStop;
import cn.ms.neural.moduler.extension.gracestop.core.GraceStopFactory;
import cn.ms.neural.moduler.extension.gracestop.processor.IGraceStopProcessor;

public class GraceStopTest {

	@Test
	public void test() {
		String reqData="请求报文";
		IGraceStop<String, String> graceStop=new GraceStopFactory<>();
		graceStop.setModuler(new Moduler<String, String>());//注入模块
		
		String resData=graceStop.gracestop(reqData, new IGraceStopProcessor<String, String>() {
			@Override
			public String processor(String req, Object... args) {
				return req;
			}
		});
		
		Assert.assertEquals(reqData, resData);
	}
	
}
