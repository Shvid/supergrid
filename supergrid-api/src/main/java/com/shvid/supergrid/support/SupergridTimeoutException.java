package com.shvid.supergrid.support;

/**
 * SupergridTimeoutException
 * 
 * @author Alex Shvid
 *
 */

public class SupergridTimeoutException extends SupergridException {

	private static final long serialVersionUID = 942050858050281111L;

	public SupergridTimeoutException(String msg) {
		super(msg);
	}
	
	public SupergridTimeoutException(String msg, Throwable t) {
		super(msg, t);
	}

	public SupergridTimeoutException(Throwable t) {
		super(t);
	}
	
}
