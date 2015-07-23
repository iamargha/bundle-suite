package com.mhe.bundle.exception;

public class BundleException extends RuntimeException{
	private String message;
	private Throwable throwable;
	
	public BundleException(Throwable t) {
		super(t);
		this.throwable = t;
	}
	public BundleException(String arg0, Throwable arg1)  {
		super(arg1);
		this.message = arg0;
		this.throwable = arg1;
	}
	
	public BundleException(String arg0)  {
		super(arg0);
		this.message = arg0;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the throwable
	 */
	public Throwable getThrowable() {
		return throwable;
	}
	/**
	 * @param throwable the throwable to set
	 */
	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}
	
	
}
