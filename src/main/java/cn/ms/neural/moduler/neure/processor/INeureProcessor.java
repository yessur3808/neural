package cn.ms.neural.moduler.neure.processor;

import cn.ms.neural.common.exception.neure.NeureBreathException;
import cn.ms.neural.common.exception.neure.NeureCallbackException;
import cn.ms.neural.common.exception.neure.NeureFaultTolerantException;
import cn.ms.neural.moduler.senior.alarm.IAlarmType;
import cn.ms.neural.processor.IProcessor;

/**
 * 处理器
 * 
 * @author lry
 * @version v1.0
 */
public interface INeureProcessor<REQ, RES> extends IProcessor<REQ, RES>, IAlarmType<REQ, RES> {

	/**
	 * 失败容错
	 * 
	 * @param req
	 * @param args
	 * @return
	 */
	RES faulttolerant(REQ req, Object...args) throws NeureFaultTolerantException;
	
	/**
	 * 慢性尝试周期计算
	 * <br>
	 * 1.用于释放句柄资源<br>
	 * 2.用于节约资源开销<br>
	 * @param nowTimes
	 * @param nowExpend
	 * @param maxRetryNum
	 * @param args
	 * @return
	 * @throws Throwable
	 */
	long breath(long nowTimes, long nowExpend, long maxRetryNum, Object...args) throws NeureBreathException;
	
	/**
	 * 回调服务
	 * 
	 * @param res
	 * @param args
	 * @throws Throwable
	 */
	void callback(RES res, Object...args) throws NeureCallbackException;
	
}
