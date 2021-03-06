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

import com.shvid.supergrid.api.operation.CompareAndSetOperation;
import com.shvid.supergrid.api.operation.ExistOperation;
import com.shvid.supergrid.api.operation.GetOperation;
import com.shvid.supergrid.api.operation.SetOperation;


/**
 * Client operations
 * 
 * @author Alex Shvid
 *
 */

public interface ClientOperations {

	/**
	 * Creates batch
	 * 
	 * @return not null batch operation
	 */
	
	BatchOperation newBatch();

	/**
	 * Creates exist operation
	 * 
	 * @param cacheName - cache name
	 * @param majorKey - major key
	 * @return not null exist operation
	 */
	
	ExistOperation newExist(String cacheName, String majorKey);
	
	/**
	 * Creates get operation
	 * 
	 * @param cacheName - cache name
	 * @param majorKey - major key
	 * @return not null get operation
	 */
	
	GetOperation newGet(String cacheName, String majorKey);

	/**
	 * Creates set operation
	 * 
	 * @param cacheName - cache name
	 * @param majorKey - major key
	 * @return not null set operation
	 */
	
	SetOperation newSet(String cacheName, String majorKey);
	
	/**
	 * Creates compare and set operation
	 * 
	 * @param cacheName - cache name
	 * @param majorKey - major key
	 * @return not null CAS operation
	 */
	
	CompareAndSetOperation newCas(String cacheName, String majorKey);
	
}
