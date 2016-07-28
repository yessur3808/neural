package cn.ms.neural.common.logger.impl;

import org.apache.logging.log4j.LogManager;

import cn.ms.neural.common.logger.ILogger;
import cn.ms.neural.common.logger.LoggerFactory.InternalLoggerFactory;

public class Log4j2LoggerFactory implements InternalLoggerFactory {
	
	public ILogger getLogger(Class<?> clazz) {
		return new Log4jLogger(clazz);
	}
	
	public ILogger getLogger(String name) {
		return new Log4jLogger(name);
	}
	
}

class Log4jLogger extends ILogger {
	
	private org.apache.logging.log4j.Logger log;
	
	Log4jLogger(Class<?> clazz) {
		log = LogManager.getLogger(clazz);
	}
	Log4jLogger(String name) {
		log = LogManager.getLogger(name);
	}
	
	
	//$NON-NLS-INFO$
	@Override
	public boolean isInfoEnabled() {
		return log.isInfoEnabled();
	}
	@Override
	public void info(String message) {
		log.info(message);
	}
	@Override
	public void info(String message, Throwable t) {
		log.info(message, t);
	}
	@Override
	public void info(String format, Object... args){
		log.info(format, args);
	}

	
	//$NON-NLS-DEBUG$
	@Override
	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}
	@Override
	public void debug(String message) {
		log.debug(message);
	}
	@Override
	public void debug(String message, Throwable t) {
		log.debug(message, t);
	}
	@Override
	public void debug(String format, Object... args){
		log.debug(format, args);
	} 
	
	
	//$NON-NLS-WARN$
	@Override
	public boolean isWarnEnabled() {
		return log.isWarnEnabled();
	}
	@Override
	public void warn(String message) {
		log.warn(message);
	}
	@Override
	public void warn(String message, Throwable t) {
		log.warn(message, t);
	}
	@Override
	public void warn(String format, Object... args){
		log.warn(format, args);
	}
	
	
	//$NON-NLS-ERROR$
	@Override
	public boolean isErrorEnabled() {
		return log.isErrorEnabled();
	}
	@Override
	public void error(String message) {
		log.error(message);
	}
	@Override
	public void error(String message, Throwable t) {
		log.error(message, t);
	}
	@Override
	public void error(String format, Object... args){
		log.error(format, args);
	}
	
	
	//$NON-NLS-FATAL$
	@Override
	public boolean isFatalEnabled() {
		return log.isFatalEnabled();
	}
	@Override
	public void fatal(String message) {
		log.fatal(message);
	}
	@Override
	public void fatal(String message, Throwable t) {
		log.fatal(message, t);
	}
	
	
	//$NON-NLS-TRACE$
	@Override
	public void trace(String message) {
		log.trace(message);
	}
	@Override
	public void trace(String message, Throwable t) {
		log.trace(message, t);
	}
	@Override
	public boolean isTraceEnabled() { 
		return log.isTraceEnabled();
	}
	
}