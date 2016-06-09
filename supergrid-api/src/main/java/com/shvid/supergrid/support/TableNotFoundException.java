package com.shvid.supergrid.support;

public class TableNotFoundException extends SupergridException {

	private static final long serialVersionUID = 4824538129120664380L;

	public TableNotFoundException(String tableName) {
		super(tableName);
	}

	public TableNotFoundException(String tableName, Throwable t) {
		super(tableName, t);
	}

}
