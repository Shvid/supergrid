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
