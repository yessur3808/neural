package cn.ms.neural;

import cn.ms.neural.common.URL;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.extension.echosound.type.EchoSoundType;
import cn.ms.neural.moduler.extension.gracestop.type.GraceStopStatusType;

/**
 * Demo 案例
 * 
 * @author lry
 * @version v1.0
 */
public class HelloWord {

	Moduler<String, String> moduler=new Moduler<>();
	URL url=URL.valueOf("http://127.0.0.1:8080/neural/?"
			+ "gracestop.status="+GraceStopStatusType.ONLINE
			+ "&gracestop.switch=boot");
	Neural<String, String> neural=new Neural<String, String>();
	
	public HelloWord() {
		try {
			moduler.setUrl(url);
			neural.notify(moduler);
			neural.init();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void input() {
		try {
			try {
				neural.neural("请求报文", "key1", EchoSoundType.NON, null, new NeuralProcessorDemo());
			} catch (Throwable t) {
				t.printStackTrace();
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new HelloWord().input();
	}
	
}
