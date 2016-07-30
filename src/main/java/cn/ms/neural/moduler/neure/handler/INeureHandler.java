package cn.ms.neural.moduler.neure.handler;

public interface INeureHandler<REQ, RES> {

	RES route(REQ req, Object...args) throws Throwable;

	RES faulttolerant(REQ req, Object...args);
	
	long breath(long nowTimes, long nowExpend, long maxRetryNum, Object...args) throws Throwable;
	
	void callback(RES res, Object...args) throws Throwable;
	
	void alarm(REQ req, Throwable t, Object...args) throws Throwable;
	
}
