package com.shvid.supergrid.api.batch;

import com.shvid.supergrid.api.operation.FieldOperation;

public class EntryOperation {

	protected final String minorKeyOrNull;
	
	public EntryOperation(String minorKeyOrNull) {
		this.minorKeyOrNull = minorKeyOrNull;
	}

	public EntryOperation add(FieldOperation op) {
		return this;
	}
	
}
