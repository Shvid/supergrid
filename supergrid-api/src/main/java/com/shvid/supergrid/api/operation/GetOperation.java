package com.shvid.supergrid.api.operation;

import com.shvid.supergrid.api.schema.SupergridField;

public class GetOperation extends FieldOperation {

	public GetOperation(SupergridField field) {
		super(field);
	}
	
	@Override
	public boolean isReadonly() {
		return true;
	}

	
}
