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

import java.util.Properties;

/**
 * PropertyPlaceholder is using to replace properties like ${key} by value from java.util.Properties
 * 
 * @author Alex Shvid
 *
 */

public final class PropertyPlaceholder {

	private final Properties props;
	
	public PropertyPlaceholder(Properties props) {
		this.props = props;
	}
	
	/**
	 * Lookup substrings by pattern ${key} and replaces by value from Properties
	 * 
	 * @param val - string value from xml config
	 * @return replaced value by properties if found
	 */
	
	public String replaceAll(String val) {
		
		int first = val.indexOf('$');
		if (first == -1) {
			return val;
		}
		
		int len = val.length();
		StringBuilder result = new StringBuilder();
		int i = first;
		int begin = 0;
		while(i != -1) {
			
			if (begin < i) {
				result.append(val, begin, i);
			}
			
			boolean printDollar = true;
			i++;
			if (i < len && val.charAt(i) == '{') {
				
					i++;
					int startProp = i;
					i = val.indexOf('}', i);
					
					if (i != -1) {
						String key = val.substring(startProp, i);
						String value = props.getProperty(key);
						if (value != null) {
							result.append(value);
						}
						
						i++;
						
						printDollar = false;
					}
					else {
						i = startProp - 1;
					}
					
			}
			
			if (printDollar) {
				result.append('$');
			}

			begin = i;
			i = val.indexOf('$', i);
		}
		
		if (begin < len) {
			result.append(val, begin, len);
		}
		
		return result.toString();
	}

}
