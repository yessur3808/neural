package cn.ms.neural;

/**
 * 通知
 * 
 * @author lry
 */
public interface INotify<MSG> {

	void notify(MSG msg);
	
}
