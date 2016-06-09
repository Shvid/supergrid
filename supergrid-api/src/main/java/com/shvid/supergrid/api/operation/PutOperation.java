package com.shvid.supergrid.api.operation;

import com.shvid.supergrid.api.schema.SupergridField;

public class PutOperation extends FieldOperation {

	public PutOperation(SupergridField field) {
		super(field);
	}
	
	@Override
	public boolean isReadonly() {
		return false;
	}

	
}
