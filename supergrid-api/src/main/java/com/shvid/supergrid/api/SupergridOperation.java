package com.shvid.supergrid.api;


/**
 * All operations in Supergrid are asynronous
 * 
 * Reactive programming
 * 
 * All operations are thread-safe
 * 
 * @author Alex Shvid
 *
 */

public interface SupergridOperation {

	/**
	 * Checks if operation was completed
	 * 
	 * @return true if completed
	 */
	
	boolean isCompleted();
	
	/**
	 * Returns result of operation
	 * 
	 * @return not null result or throws NotCompletedOperationException if not completed
	 */
	
	SupergridResult getResult();
	
	/**
	 * This method gives ability to check if result was successful or not 
	 * 
	 * It will throw NotCompletedOperationException or any error exception on error
	 */
	
	void assertNoError();
	
	/**
	 * Resets operation, remove existing result
	 */
	
	void reset();
	
	/**
	 * Sets result of operation 
	 * 
	 * @param result of the operation
	 */
	
	void complete(SupergridResult result);
	
}
