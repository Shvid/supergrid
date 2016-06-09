package com.shvid.supergrid.api;

import com.google.common.base.Optional;
import com.shvid.supergrid.api.schema.SupergridField;

/**
 * SupergridKey
 * 
 * Key clearly defines path to the value
 * 
 * @author Alex Shvid
 *
 */

public interface SupergridKey {

	/**
	 * Returns super key or null
	 * 
	 * For replicated tables superKey is always null, because it is not needed
	 * For partitioned tables superKey can be null, in this case majorKey is a superKey
	 * 
	 * @return null or super key
	 */
	
	Optional<String> getSuperKey();
	
	/**
	 * Returns major key
	 * 
	 * @return not null major key
	 */
	
	String getMajorKey();
	
	/**
	 * Returns minor key or null
	 * 
	 * @return null or minor key
	 */
	
	Optional<String> getMinorKey();
	
	/**
	 * Returns field
	 * 
	 * @return returns not null field
	 */
	
	SupergridField getField();
	
}
