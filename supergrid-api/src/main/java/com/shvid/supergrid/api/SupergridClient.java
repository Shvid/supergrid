package com.shvid.supergrid.api;


/**
 * Supergrid client
 * 
 * @author Alex Shvid
 *
 */

public interface SupergridClient {

	/**
	 * Executes supergrid operation
	 * 
	 * @param operation
	 * @return not null future
	 */
	
	SupergridFuture execute(SupergridOperation operation, int timeoutMillis);
	
}
