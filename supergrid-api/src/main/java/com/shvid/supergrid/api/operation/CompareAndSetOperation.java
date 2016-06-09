package com.shvid.supergrid.api.operation;

import com.shvid.supergrid.api.schema.SupergridField;

public class CompareAndSetOperation extends FieldOperation {

	public CompareAndSetOperation(SupergridField field) {
		super(field);
	}

	@Override
	public boolean isReadonly() {
		return false;
	}
	
	
	
}
