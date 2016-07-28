package cn.ms.neural;

import cn.ms.neural.common.URL;
import cn.ms.neural.moduler.Moduler;

public class NeuralTest {

	public static final String REQ_DATA = "这是请求报文";
	
	public static void main(String[] args) throws Throwable{
		Moduler<String, String> moduler=new Moduler<>();
		moduler.setUrl(URL.valueOf("http://127.0.0.1:8080/test/?a=1&b=2"));
		
		Neural<String, String> neural=new Neural<String, String>();
		neural.setModuler(moduler);
		neural.init();
		System.out.println("开始前:"+moduler.getUrl().toString());
		for (int i = 0; i < 10; i++) {
			String resData=neural.gracestop(REQ_DATA);
			System.out.println("响应报文:"+resData);
			System.out.println("URL:"+moduler.getUrl().toString());
			Thread.sleep(1000);
		}
	}
	
}
