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
package com.shvid.supergrid.api;

/**
 * GridProvider interface
 * 
 * @author Alex Shvid
 *
 */

public interface GridProvider {

	/**
	 * Executes operation
	 * 
	 * @param operation - single operation
	 * @param timeoutMillis - timeout milliseconds
	 * @return not null future of single result
	 */
	
	<O extends SingleOperation<O>> SingleFuture<O> execute(O operation, int timeoutMillis);
	
	/**
	 * Executes batch
	 * 
	 * @param timeoutMillis - timeout milliseconds
	 * @return not null batch future
	 */
	
	BatchFuture execute(BatchOperation batch, int timeoutMillis);
	
}
