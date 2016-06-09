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

import com.shvid.supergrid.support.TableNotFoundException;


/**
 * SupergridNamespace
 * 
 * @author Alex Shvid
 *
 */

public interface SupergridNamespace {

	/**
	 * Gets unique number of namespace
	 * 
	 * @return namespace id
	 */
	
	int getNamespaceId();
	
	/**
	 * Return namespace
	 * 
	 * @return not null namespace name
	 */
	
	String getNamespace();
	
	/**
	 * Returns list of tables, can be empty
	 * 
	 * @return not null list
	 */
	
	List<SupergridTable> getTables();
	
	/**
	 * Gets table by name
	 * 
	 * @param tableName - table name
	 * @return not null table
	 * @throws TableNotFoundException
	 */
	
	SupergridTable getTableByName(String tableName) throws TableNotFoundException;

	
}
