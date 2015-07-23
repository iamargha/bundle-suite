
package com.mhe.bundle.common;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.LoggerFactory;

public class Logger {

	private org.slf4j.Logger _log = LoggerFactory.getLogger(Logger.class);

	//Let the debug be always enabled. The actual value is determined by the logback and not here. 
	private static int DEBUG = 1;
	
	public Logger() {

	}

	private Logger(Class cl) {
		_log = LoggerFactory.getLogger(cl);
	}

	private Logger(String str) {
		_log = LoggerFactory.getLogger(str);
	}

	public synchronized static void init() {
        Properties props = new Properties();
	
				//String debug = Configuration.getSystemValue("DEBUG");
        		//hard coded
        		String debug = null;
		        if(debug!=null && debug.equals("ON")){
		             DEBUG=1;
		        } else {
		            DEBUG=0;
		        }
			 
    }

	public synchronized static Logger getInstance(Class cl) {
		Logger logger = new Logger(cl);
		logger._log = LoggerFactory.getLogger(cl.getName());
		/** Capture the class name */
		if (DEBUG == 5) {
			init();
		}

		return logger;
	}

	public synchronized static Logger getInstance(String str) {
		Logger logger = new Logger(str);
		logger._log = LoggerFactory.getLogger(str);
		if (DEBUG == 5) {
			init();
		}
		return logger;
	}

	public boolean isDebugEnabled() {
		return _log.isDebugEnabled();
	}

	public boolean isErrorEnabled() {
		return _log.isErrorEnabled();
	}

	public boolean isFatalEnabled() {
		return true;
	}

	public boolean isInfoEnabled() {
		return _log.isInfoEnabled();
	}

	public boolean isTraceEnabled() {
		return _log.isTraceEnabled();
	}

	public boolean isWarnEnabled() {
		return _log.isWarnEnabled();
	}

	public void trace(Object o) {
		_log.trace(o.toString());
	}

	public void trace(String message, Throwable throwable) {
		_log.trace(message, throwable);
	}

	public void trace(Throwable ex) {
		_log.trace("Exception", ex);
	}

	public void debug(Object o) {
		if (o != null)
			_log.debug(o.toString());
	}

	public void debug(Throwable ex) {
		_log.debug("Exception", ex);
	}

	public void debug(String message, Throwable throwable) {
		_log.debug(message, throwable);
	}
	
	public void debug(String s ,Object[] o){
		if(s!=null && o!= null){
			_log.debug(s, o);
		}
	}

	public void info(Object o) {
		if (o != null)
			_log.info(o.toString());
	}
	
	public void info(String s ,Object[] o){
		if(s!=null && o!= null){
			_log.info(s, o);
		}
	}

	public void info(String message, Throwable throwable) {
		_log.info(message, throwable);
	}

	public void info(Throwable ex) {
		_log.info("Exception", ex);
	}

	public void warn(Object o) {
		if (o != null)
			_log.warn(o.toString());
	}

	public void warn(String message, Throwable throwable) {
		_log.warn(message, throwable);
	}

	public void warn(Throwable ex) {
		_log.warn("Exception", ex);
	}

	public void error(Object o) {
		if (o != null)
			_log.error(o.toString());
	}

	public void error(String message, Throwable throwable) {
		_log.error(message, throwable);
	}
	
	public void error(String s ,Object[] o){
		if(s!=null && o!= null){
			_log.error(s, o);
		}
	}

	public void error(Throwable ex) {
		_log.error("Exception", ex);
	}

	public void fatal(Object o) {
		if (o != null)
			_log.error(o.toString());
	}

	public void fatal(String o, Throwable throwable) {
		if (o != null)
			_log.error(o.toString(), throwable);
	}

	public void fatal(Throwable ex) {
		_log.error("Exception", ex);
	}

	public static void turnDebugOn() {
		DEBUG = 1;
	}

	public static void turnDebugOff() {
		DEBUG = 0;
	}

	/**
	 * Log a message at the DEBUG level according to the specified format
	 * and argument.
	 *
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the DEBUG level. </p>
	 *
	 * @param format the format string
	 * @param arg  the argument
	 */
	public void debug(String format, Object arg) {
		_log.debug(format, arg);
	}

	/**
	 * Log a message at the DEBUG level according to the specified format
	 * and arguments.
	 *
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the DEBUG level. </p>
	 *
	 * @param format the format string
	 * @param arg1  the first argument
	 * @param arg2  the second argument
	 */
	public void debug(String format, Object arg1, Object arg2) {
		_log.debug(format, arg1, arg2);
	}

	/**
	 * Log a message at the INFO level according to the specified format
	 * and argument.
	 *
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the INFO level. </p>
	 *
	 * @param format the format string
	 * @param arg  the argument
	 */
	public void info(String format, Object arg) {
		_log.info(format, arg);
	}

	/**
	 * Log a message at the INFO level according to the specified format
	 * and arguments.
	 *
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the INFO level. </p>
	 *
	 * @param format the format string
	 * @param arg1  the first argument
	 * @param arg2  the second argument
	 */
	public void info(String format, Object arg1, Object arg2) {
		_log.info(format, arg1, arg2);
	}

	/**
	 * Log a message at the WARN level according to the specified format
	 * and argument.
	 *
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the WARN level. </p>
	 *
	 * @param format the format string
	 * @param arg  the argument
	 */
	public void warn(String format, Object arg) {
		_log.warn(format, arg);
	}

	/**
	 * Log a message at the WARN level according to the specified format
	 * and arguments.
	 *
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the WARN level. </p>
	 *
	 * @param format the format string
	 * @param arg1  the first argument
	 * @param arg2  the second argument
	 */
	public void warn(String format, Object arg1, Object arg2) {
		_log.warn(format, arg1, arg2);
	}

	/**
	 * Log a message at the ERROR level according to the specified format
	 * and argument.
	 *
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the ERROR level. </p>
	 *
	 * @param format the format string
	 * @param arg  the argument
	 */
	public void error(String format, Object arg) {
		_log.error(format, arg);
	}

	/**
	 * Log a message at the ERROR level according to the specified format
	 * and arguments.
	 *
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the ERROR level. </p>
	 *
	 * @param format the format string
	 * @param arg1  the first argument
	 * @param arg2  the second argument
	 */
	public void error(String format, Object arg1, Object arg2) {
		_log.error(format, arg1, arg2);
	}



}