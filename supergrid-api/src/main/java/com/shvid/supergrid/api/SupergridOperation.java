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
 * All operations in Supergrid are asynronous
 * 
 * Reactive programming
 * 
 * All operations are thread-safe
 * 
 * @author Alex Shvid
 *
 */

public interface SupergridOperation {

	/**
	 * Checks if operation was completed
	 * 
	 * @return true if completed
	 */
	
	boolean isCompleted();
	
	/**
	 * Returns result of operation
	 * 
	 * @return not null result or throws NotCompletedOperationException if not completed
	 */
	
	SupergridResult getResult();
	
	/**
	 * This method gives ability to check if result was successful or not 
	 * 
	 * It will throw NotCompletedOperationException or any error exception on error
	 */
	
	void assertNoError();
	
	/**
	 * Resets operation, remove existing result
	 */
	
	void reset();
	
	/**
	 * Sets result of operation 
	 * 
	 * @param result of the operation
	 */
	
	void complete(SupergridResult result);
	
}
