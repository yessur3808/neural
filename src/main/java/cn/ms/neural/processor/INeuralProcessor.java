package cn.ms.neural.processor;

import cn.ms.neural.moduler.neure.processor.INeureProcessor;

/**
 * 微服务神经元处理中心
 * 
 * @author lry
 * @version v1.0
 */
public interface INeuralProcessor<REQ, RES> extends INeureProcessor<REQ, RES> {

	
	//$NON-NLS-回声探测$
	/**
	 * 发起探测
	 * 
	 * @param req
	 * @param args
	 * @return
	 */
	REQ $echo(REQ req, Object...args);
	/**
	 * 反弹探测
	 * 
	 * @param req
	 * @param args
	 * @return
	 */
	RES $rebound(REQ req, Object...args);

	
	//$NON-NLS-幂等$
	/**
	 * 幂等请求校验(判断是否是幂等请求)
	 * 
	 * @param neuralId
	 * @param args
	 * @return
	 */
	boolean check(String neuralId, Object...args);
	/**
	 * 获取幂等结果
	 * 
	 * @param neuralId
	 * @param args
	 * @return
	 */
	RES get(String neuralId, Object...args);
	/**
	 * 幂等结果持久化
	 * 
	 * @param req
	 * @param res
	 * @param args
	 */
	void storage(REQ req, RES res, Object...args);

	
	//$NON-NLS-服务降级$
	/**
	 * Mock服务降级
	 * 
	 * @param req
	 * @param args
	 * @return
	 */
	RES mock(REQ req, Object...args);
	/**
	 * 业务降级
	 * 
	 * @param req
	 * @param args
	 * @return
	 */
	RES bizDegrade(REQ req, Object...args);

	
}
