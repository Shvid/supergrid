package com.shvid.supergrid.support;

/**
 * Root hierarchy exception for all supergrid runtime exceptions
 * 
 * @author Alex Shvid
 *
 */

public class SupergridException extends RuntimeException {

	private static final long serialVersionUID = -359861329437043934L;

	public SupergridException(String msg) {
		super(msg);
	}

	public SupergridException(String msg, Throwable t) {
		super(msg, t);
	}

	public SupergridException(Throwable t) {
		super(t);
	}
	
}
