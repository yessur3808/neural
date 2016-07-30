package cn.ms.neural.moduler.neure.core;

import java.util.List;
import java.util.concurrent.TimeoutException;

import com.netflix.hystrix.HystrixEventType;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixRuntimeException;

import cn.ms.neural.common.exception.neure.NeureCallbackException;
import cn.ms.neural.common.exception.neure.NeureException;
import cn.ms.neural.common.exception.neure.NeureNonFaultTolerantException;
import cn.ms.neural.common.exception.neure.NeureTimeoutException;
import cn.ms.neural.common.exception.neure.NeureUnknownException;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.neure.INeure;
import cn.ms.neural.moduler.neure.entity.NeureEntity;
import cn.ms.neural.moduler.neure.handler.INeureHandler;
import cn.ms.neural.moduler.neure.handler.support.NeureHandler;

public class NeureFactory<REQ, RES> implements INeure<REQ, RES> {

	Moduler<REQ, RES> moduler;
	NeureEntity neureEntity;
	
	@Override
	public void setModuler(Moduler<REQ, RES> moduler) {
		this.moduler=moduler;
	}
	
	@Override
	public void init() throws Throwable {
		
	}

	@Override
	public RES neure(REQ req, INeureHandler<REQ, RES> handler, Object... args) throws NeureException {
		RES res=null;
		NeureHandler<REQ, RES> neureHandler=null;
		
		neureHandler=new NeureHandler<REQ, RES>(req, neureEntity, handler, args);
		
		try {
			res=neureHandler.execute();//执行或容错
		}catch(HystrixBadRequestException nonft){
			throw new NeureNonFaultTolerantException(nonft.getMessage(), nonft);//不执行容错异常
		}catch(HystrixRuntimeException hre){
			if(hre.getCause() instanceof TimeoutException){
				throw new NeureTimeoutException(hre.getMessage(), hre);//超时异常
			}else{
				throw new NeureException(hre.getMessage(), hre);//其他异常
			}
		} catch (Throwable unknown) {
			throw new NeureUnknownException(unknown.getMessage(), unknown);//未知异常
		} finally {
			try {
				handler.callback(res, args);//回调
			} catch (Throwable callback) {
				throw new NeureCallbackException(callback.getMessage(), callback);//回调异常
			}
			
			List<HystrixEventType> hystrixEventTypes=neureHandler.getExecutionEvents();
			HystrixEventType hystrixEventType=hystrixEventTypes.get(0);
			System.out.println(hystrixEventType.isTerminal());
			System.out.println(hystrixEventType.name());
			System.out.println(hystrixEventType.ordinal());
		}
		
		return res;
	}
	
	@Override
	public void destory() throws Throwable {
		
	}

}
