package com.shvid.supergrid.support;

public class NamespaceNotFoundException extends SupergridException {

	private static final long serialVersionUID = -3537542670090400719L;

	public NamespaceNotFoundException(String namespace) {
		super(namespace);
	}

	public NamespaceNotFoundException(String namespace, Throwable t) {
		super(namespace, t);
	}

}
