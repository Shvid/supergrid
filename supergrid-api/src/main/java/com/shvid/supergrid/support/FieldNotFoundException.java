package com.shvid.supergrid.support;

public class FieldNotFoundException extends SupergridException {

	private static final long serialVersionUID = -3479975328011894739L;

	public FieldNotFoundException(String fieldName) {
		super(fieldName);
	}

	public FieldNotFoundException(String fieldName, Throwable t) {
		super(fieldName, t);
	}

}
