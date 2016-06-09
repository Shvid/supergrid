package com.shvid.supergrid.api.batch;


public class RecordOperation {

	protected final String majorKey;
	
	public RecordOperation(String majorKey) {
		this.majorKey = majorKey;
	}
	
	public String getMajorKey() {
		return majorKey;
	}

	public RecordOperation add(EntryOperation op) {
		return this;
	}
	
}
