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
