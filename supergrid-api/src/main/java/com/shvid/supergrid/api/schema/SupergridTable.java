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
package com.shvid.supergrid.api.schema;

import java.util.List;

import com.shvid.supergrid.support.FieldNotFoundException;


/**
 * SupergridCache
 * 
 * @author Alex Shvid
 *
 */

public interface SupergridTable {

	/**
	 * Gets namespace
	 * 
	 * @return namespace
	 */
	
	SupergridNamespace getNamespace();
	
	/**
	 * Returns cache id
	 * 
	 * @return unique number of the cache
	 */
	
	int getTableId();
	
	/**
	 * Return cache name
	 * 
	 * @return not null cache name
	 */
	
	String getTableName();
	
	/**
	 * Return list of fields in the table, can be empty
	 * 
	 * @return not null list
	 */
	
	List<SupergridField> getFields();
	
	/**
	 * Gets field by name
	 * 
	 * @param fieldName - field name
	 * @return not null field
	 * @throws FieldNotFoundException
	 */
	
	SupergridField getFieldByName(String fieldName) throws FieldNotFoundException;
	
	/**
	 * Returns time to live in seconds
	 * 
	 * @return ttl in seconds or 0
	 */
	
	int getTimeToLive();
	
	/**
	 * Returns timeout in milliseconds
	 * 
	 * @return timeoutMillis or 0
	 */
	
	int timeoutMillis();
	
}
