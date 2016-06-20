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

import com.google.common.base.Optional;


/**
 * Client Definition
 * 
 * @author Alex Shvid
 *
 */

public interface ClientDefinition extends CacheSettingsHolder {

	/**
	 * Gets name of the client application
	 * 
	 * @return not null client name or unknown
	 */
	
	String getName();
	
	/**
	 * Gets server definitions
	 * 
	 * @return not null list
	 */
	
	List<ServerDefinition> getServers();
	
	/**
	 * Gets auth definition if exists
	 * 
	 * @return auth definition
	 */
	
	Optional<AuthDefinition> getAuthDefinition();
	

}
