package com.shvid.supergrid.api.schema;

/**
 * SupergridField
 * 
 * @author Alex Shvid
 *
 */

public interface SupergridField {

	/**
	 * Returns table
	 * 
	 * @return not null table
	 */
	
	SupergridTable getTable();
	
	/**
	 * Return immutable fieldId in the table
	 * 
	 * @return field tag number
	 */
	
	int getFieldId();
	
	/**
	 * Returns field name, this name is local for table
	 * 
	 * @return not null field name
	 */
	
	String getFieldName();
	
	/**
	 * Gets type id
	 * @return type id
	 */
	
	int getTypeId();
	
}
