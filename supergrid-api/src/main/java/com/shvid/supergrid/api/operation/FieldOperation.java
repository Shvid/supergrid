package com.shvid.supergrid.api.operation;

import com.shvid.supergrid.api.schema.SupergridField;

public abstract class FieldOperation {

	protected final SupergridField field;
	
	public FieldOperation(SupergridField field) {
		this.field = field;
	}
	
	/**
	 * Some operations are readonly, other are not
	 * 
	 * @return true if readonly
	 */
	
	public abstract boolean isReadonly();
	
}
