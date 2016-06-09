package com.shvid.supergrid.support;

import com.shvid.supergrid.api.SupergridOperation;

/**
 * NotCompletedOperationException
 * 
 * @author Alex Shvid
 *
 */

public class NotCompletedOperationException extends SupergridException {

	private static final long serialVersionUID = -3703307500050056346L;
	
	private final SupergridOperation operation;
	
	public NotCompletedOperationException(SupergridOperation operation) {
		super("execute operation before get result: " + operation);
		this.operation = operation;
	}

	public SupergridOperation getOperation() {
		return operation;
	}

	@Override
	public String toString() {
		return "NotCompletedOperationException [operation=" + operation + "]";
	}
}
