package cn.ms.neural.example.processor;

import cn.ms.neural.common.exception.AlarmException;
import cn.ms.neural.common.exception.ProcessorException;
import cn.ms.neural.common.exception.degrade.DegradeException;
import cn.ms.neural.common.exception.echosound.EchoSoundException;
import cn.ms.neural.common.exception.echosound.EchoSoundReboundException;
import cn.ms.neural.common.exception.idempotent.IdempotentException;
import cn.ms.neural.common.exception.neure.NeureBreathException;
import cn.ms.neural.common.exception.neure.NeureCallbackException;
import cn.ms.neural.common.exception.neure.NeureFaultTolerantException;
import cn.ms.neural.example.entity.GatewayREQ;
import cn.ms.neural.example.entity.GatewayRES;
import cn.ms.neural.moduler.ModulerType;
import cn.ms.neural.moduler.senior.alarm.IAlarmType;
import cn.ms.neural.processor.INeuralProcessor;

/**
 * 网关处理中心
 * <br>
 * 主要用于定义各类处理的逻辑。
 * @author lry
 * @version v1.0
 */
public class APIGatewayNeuralProcessor implements INeuralProcessor<GatewayREQ, GatewayRES>{

	@Override
	public GatewayRES processor(GatewayREQ req, Object... args) throws ProcessorException {
		//模拟业务处理并组装响应报文
		GatewayRES res=new GatewayRES();
		res.setContent("业务响应{"+req.getBizNo()+","+req.getContent()+"}");
		
		return res;
	}

	@Override
	public void alarm(ModulerType modulerType, IAlarmType alarmType, GatewayREQ req, GatewayRES res, Throwable t,
			Object... args) throws AlarmException {
		// 此处为告警通知处理逻辑
		
	}

	@Override
	public GatewayRES faulttolerant(GatewayREQ req, Object... args) throws NeureFaultTolerantException {
		// 此处为容错处理逻辑
		
		return null;
	}

	@Override
	public long breath(long nowTimes, long nowExpend, long maxRetryNum, Object... args) throws NeureBreathException {
		// 此处为慢性呼吸时间控制处理逻辑
		return 0;
	}

	@Override
	public void callback(GatewayRES res, Object... args) throws NeureCallbackException {
		// 此处为调度完成后回调处理逻辑
		
	}

	@Override
	public GatewayREQ $echo(GatewayREQ req, Object... args) throws EchoSoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GatewayRES $rebound(GatewayREQ req, Object... args) throws EchoSoundReboundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean check(String neuralId, Object... args) throws IdempotentException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public GatewayRES get(String neuralId, Object... args) throws IdempotentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void storage(GatewayREQ req, GatewayRES res, Object... args) throws IdempotentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GatewayRES mock(GatewayREQ req, Object... args) throws DegradeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GatewayRES bizDegrade(GatewayREQ req, Object... args) throws DegradeException {
		// 此处为业务降级处理处理逻辑
		return null;
	}

}
