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

import com.shvid.supergrid.client.config.xml.EnumParser;
import com.shvid.supergrid.support.SupergridConfigException;

/**
 * Data Distribution Type
 * 
 * @author Alex Shvid
 *
 */

public enum DataDistributionType {

	/**
	 * Partitioned data
	 */
	
	PARTITIONED("partitioned"),
	
	/**
	 * Replicated data 
	 */
	
	REPLICATED("replicated");
	
	private final String name;
	
	private DataDistributionType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public enum Parser implements EnumParser<DataDistributionType> {
		
		INSTANCE;

		@Override
		public DataDistributionType parse(String value) {
			
			for (DataDistributionType type : DataDistributionType.values()) {
				if (type.getName().equalsIgnoreCase(value)) {
					return type;
				}
			}
			
			throw new SupergridConfigException("unknown keyspace mode " + value);
			
		}
		
	}
	
}
