package cn.ms.neural.common.logger;

/**
 * 日志中心
 * 
 * @author lry
 */
public abstract class ILogger {  
	
	//$NON-NLS-DEBUG$
	public abstract boolean isDebugEnabled();
	public abstract void debug(String message);
	public abstract void debug(String message, Throwable t); 
	public void debug(String format, Object... args){
		debug(String.format(format, args));
	}
	public void debug(Throwable t, String format, Object... args){
		debug(String.format(format, args), t);
	}
	
	//$NON-NLS-INFO$
	public abstract boolean isInfoEnabled();
	public abstract void info(String message);
	public abstract void info(String message, Throwable t);
	public void info(String format, Object... args){
		info(String.format(format, args));
	}
	public void info(Throwable t, String format, Object... args){
		info(String.format(format, args), t);
	}
	
	//$NON-NLS-WARN$
	public abstract boolean isWarnEnabled();
	public abstract void warn(String message);
	public abstract void warn(String message, Throwable t);
	public void warn(String format, Object... args){
		warn(String.format(format, args));
	}
	public void warn(Throwable t, String format, Object... args){
		warn(String.format(format, args), t);
	}
	
	//$NON-NLS-ERROR$
	public abstract boolean isErrorEnabled();
	public abstract void error(String message);
	public abstract void error(String message, Throwable t);
	public void error(String format, Object... args){
		error(String.format(format, args));
	} 
	public void error(Throwable t, String format, Object... args){
		error(String.format(format, args), t);
	}
	
	//$NON-NLS-TRACE$
	public abstract boolean isTraceEnabled();
	public abstract void trace(String message);
	public abstract void trace(String message, Throwable t);
	public void trace(String format, Object... args){
		trace(String.format(format, args));
	}
	public void trace(Throwable t, String format, Object... args){
		trace(String.format(format, args), t);
	}

	//$NON-NLS-FATAL$
	public abstract boolean isFatalEnabled();
	public abstract void fatal(String message);
	public abstract void fatal(String message, Throwable t);
	public void fatal(String format, Object... args){
		fatal(String.format(format, args));
	}
	public void fatal(Throwable t, String format, Object... args){
		fatal(String.format(format, args), t);
	}
	
}