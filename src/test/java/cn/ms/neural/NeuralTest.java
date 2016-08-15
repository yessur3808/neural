package cn.ms.neural;

import org.junit.Test;

import cn.ms.neural.common.URL;
import cn.ms.neural.common.exception.gracestop.GraceStopedException;
import cn.ms.neural.demo.DemoProcessor;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.extension.echosound.type.EchoSoundType;
import cn.ms.neural.moduler.extension.gracestop.type.GraceStopStatusType;
import junit.framework.Assert;

public class NeuralTest {

	/**
	 * 优雅停机
	 * @throws Throwable
	 */
	@Test
	public void testGracestop() {
		try {
			Moduler<String, String> moduler=new Moduler<>();
			URL url=URL.valueOf("http://127.0.0.1:8080/neural/?"
					+ "gracestop.status="+GraceStopStatusType.OFFLINE
					+ "&gracestop.switch=boot");
			
			moduler.setUrl(url);
			Neural<String, String> neural=new Neural<String, String>();
			neural.init();
			neural.notify(moduler);

			try {
				neural.neural("请求报文", "key1", EchoSoundType.NON, null, new DemoProcessor());
				Assert.assertTrue(false);
			} catch (Throwable t) {
				if(t instanceof GraceStopedException){
					Assert.assertTrue(true);
				}else{
					Assert.assertTrue(false);
				}
			}
			
			neural.notify(url.addModulerParameter(Conf.GRACESTOP, "status", GraceStopStatusType.ONLINE.getVal()));
			try {
				String resData=neural.neural("请求报文", "key1", EchoSoundType.NON, null, new DemoProcessor());
				Assert.assertEquals("这是响应报文", resData);
			} catch (Throwable t) {
				Assert.assertTrue(false);
				t.printStackTrace();
			}
		} catch (Throwable t) {
			Assert.assertTrue(false);
			t.printStackTrace();
		}
	}
	
}
