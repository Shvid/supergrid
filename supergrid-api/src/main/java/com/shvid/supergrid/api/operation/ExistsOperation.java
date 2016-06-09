package com.shvid.supergrid.api.operation;

import com.shvid.supergrid.api.schema.SupergridField;

public class ExistsOperation extends FieldOperation {

	public ExistsOperation(SupergridField field) {
		super(field);
	}
	
	@Override
	public boolean isReadonly() {
		return true;
	}

}
