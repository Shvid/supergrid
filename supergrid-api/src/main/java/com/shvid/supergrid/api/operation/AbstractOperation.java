/*
 * Copyright (C) 2016 The Supergrid Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.shvid.supergrid.api.operation;

import com.google.common.base.Preconditions;
import com.shvid.supergrid.api.BatchOperation;
import com.shvid.supergrid.api.ClientOperations;
import com.shvid.supergrid.api.SingleFuture;
import com.shvid.supergrid.api.SingleOperation;

/**
 * Abstract operation
 * 
 * @author Alex Shvid
 *
 */

public abstract class AbstractOperation<O extends SingleOperation<O>> implements SingleOperation<O> {

	protected final ClientOperations client;
	protected final String cacheName;
	private String superKey;
	private String majorKey;
	private String minorKey;
	private boolean executionPhase;
	
	public AbstractOperation(ClientOperations client, String cacheName) {
		this.client = client;
		this.cacheName = cacheName;
	}
	
	public String getCacheName() {
		return cacheName;
	}

	public O setSuperKey(String superKey) {
		ensureNotExecutionPhase();
		this.superKey = superKey;
		return castThis();
	}
	
	public String getSuperKey() {
		return superKey;
	}
	
	public O setMajorKey(String majorKey) {
		ensureNotExecutionPhase();
		this.majorKey = majorKey;
		return castThis();
	}
	
	public String getMajorKey() {
		return majorKey;
	}

	public O setMinorKey(String minorKey) {
		ensureNotExecutionPhase();
		this.minorKey = minorKey;
		return castThis();
	}

	public String getMinorKey() {
		return minorKey;
	}

	@Override
	public SingleFuture<O> addToBatch(BatchOperation batch) {
		Preconditions.checkNotNull(batch, "null batch");
		SingleFuture<O> future = batch.add(castThis());
		return future;
	}

	@Override
	public SingleFuture<O> execute(int timeoutMillis) {
		this.executionPhase = true;
		return null;
	}
	
	protected boolean isExecutionPhase() {
		return executionPhase;
	}
	
	protected void ensureNotExecutionPhase() {
		if (executionPhase) {
			throw new IllegalStateException("execution phase does not allow modificaitons in operation " + this);
		}
	}
	
	@SuppressWarnings("unchecked")
	private O castThis() {
		return (O) this;
	}
	
}
