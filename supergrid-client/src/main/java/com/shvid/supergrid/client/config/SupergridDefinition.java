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
package com.shvid.supergrid.client.config;

import java.util.List;
import java.util.Map;

/**
 * Base configuration interface 
 *  
 * @author Alex Shvid
 *
 */

public interface SupergridDefinition {

	/**
	 * Gets name of the configuration
	 * 
	 * @return not null name or unknown
	 */
	
	String getName();
	
	/**
	 * Gets endpoint definitions
	 * 
	 * key is the endpoint name
	 * value is the endpoint definition
	 * 
	 * @return not null map
	 */
	
	Map<String, EndpointDefinition> getEndpoints();
	
	/**
	 * Gets client definition
	 * 
	 * @return not null client definition
	 */
	
	ClientDefinition getClient();
	
	/**
	 * Gets keyspace definitions
	 * 
	 * key is the keyspace name
	 * value is the keyspace definition
	 * 
	 * @return not null map
	 */
	
	Map<String, KeyspaceDefinition> getKeyspaces();
	
	/**
	 * Gets cache mapping definitions
	 * 
	 * @return not null list
	 */
	
	List<CacheMappingDefinition> getCacheMappings();
	
	/**
	 * Gets cache definitions
	 * 
	 * key is the cache name
	 * value is the cache definition
	 * 
	 * @return not null map
	 */
	
	Map<String, CacheDefinition> getCaches();
	
}
