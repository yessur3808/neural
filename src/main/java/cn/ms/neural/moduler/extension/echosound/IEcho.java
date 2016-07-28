package cn.ms.neural.moduler.extension.echosound;

/**
 * 回声探测
 * <br>
 * 1.回声测试用于检测服务是否可用，回声测试按照正常请求流程执行，能够测试整个调用是否通畅，可用于监控。
 * 2.所有服务自动实现EchoService接口，只需将任意服务引用强制转型为EchoService，即可使用。
 * @author lry
 */
public interface IEcho {

	/**
     * echo test.
     * 
     * @param message message.
     * @return message.
     */
    Object $echo(Object message);
	
}
