package cn.ms.neural.moduler.engine.faulttolerant.entity;

import java.util.concurrent.atomic.AtomicLong;

import cn.ms.neural.common.SystemClock;
import cn.ms.neural.moduler.engine.faulttolerant.type.ExecuteType;
import cn.ms.neural.moduler.engine.faulttolerant.type.RouteMethod;

/**
 * 调用链
 * 
 * @author lry
 */
public class CallChain {
	
	/**
	 * The IRoute method
	 */
	RouteMethod routeMethod;
	/**
	 * Execute type.
	 */
	ExecuteType executeType;
	/**
	 * Expend.
	 */
	AtomicLong expend;

	CallChain() {
	}

	CallChain(RouteMethod routeMethod, ExecuteType executeType, AtomicLong expend) {
		this.routeMethod = routeMethod;
		this.executeType = executeType;
		this.expend = expend;
	}

	public RouteMethod getRouteMethod() {
		return routeMethod;
	}

	public void setRouteMethod(RouteMethod routeMethod) {
		this.routeMethod = routeMethod;
	}

	public ExecuteType getExecuteType() {
		return executeType;
	}

	public void setExecuteType(ExecuteType executeType) {
		this.executeType = executeType;
	}

	public AtomicLong getExpend() {
		return expend;
	}

	public void setExpend(AtomicLong expend) {
		this.expend = expend;
	}
	
	//$NON-NLS-support$
	public static CallChain build(RouteMethod routeMethod,ExecuteType executeType, Long startTime) {
		return new CallChain(routeMethod, executeType, new AtomicLong(SystemClock.now()-startTime));
	}

	public String toString() {
		return "CallChain [executeType=" + executeType + ", expend=" + expend + "]";
	}

}
