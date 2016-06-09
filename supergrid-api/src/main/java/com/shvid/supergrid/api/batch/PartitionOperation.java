package com.shvid.supergrid.api.batch;


public class PartitionOperation {

	protected final String superKey;
	
	public PartitionOperation(String superKey) {
		this.superKey = superKey;
	}
	
	public void add(RecordOperation op) {
		
	}
	
}
