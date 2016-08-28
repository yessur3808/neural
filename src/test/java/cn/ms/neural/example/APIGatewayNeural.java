package cn.ms.neural.example;

import java.util.UUID;

import cn.ms.neural.Neural;
import cn.ms.neural.common.URL;
import cn.ms.neural.common.utils.NetUtils;
import cn.ms.neural.example.entity.GatewayREQ;
import cn.ms.neural.example.entity.GatewayRES;
import cn.ms.neural.example.processor.APIGatewayNeuralProcessor;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.extension.echosound.type.EchoSoundType;
import cn.ms.neural.processor.INeuralProcessor;

/**
 * 服务网关Neural
 * 
 * @author lry
 * @version v1.0
 */
public class APIGatewayNeural {

	Neural<GatewayREQ, GatewayRES> neural = null;
	INeuralProcessor<GatewayREQ, GatewayRES> processor=new APIGatewayNeuralProcessor();
	
	public APIGatewayNeural() {
		try {
			//$NON-NLS-初始化$
			Moduler<GatewayREQ, GatewayRES> moduler=new Moduler<GatewayREQ, GatewayRES>();
			//记录当前节点的Neural配置信息
			URL url=URL.valueOf("http://"+NetUtils.getLocalIp()
					+":8080/cn.ms.neural.APIGatewayNeural?"
					+ "gracestop.status=ONLINE"
					+ "&gracestop.switch=boot");
			moduler.setUrl(url);
			
			neural=new Neural<GatewayREQ, GatewayRES>(moduler);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		APIGatewayNeural apiGatewayNeural=new APIGatewayNeural();
		
		//执行业务调度
		for (int i = 0; i < 5; i++) {//模拟5次请求
			//模拟请求对象
			GatewayREQ req=new GatewayREQ();
			req.setBizNo("BIZ_"+UUID.randomUUID().toString());
			req.setContent("这是请求报文"+i);
			
			GatewayRES res=apiGatewayNeural.handler(req);
			System.out.println("响应报文:"+res.getContent());	
		}
	}

	private GatewayRES handler(GatewayREQ req) {
		try {
			return neural.neural(req, req.getBizNo(), EchoSoundType.NON, null, processor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
