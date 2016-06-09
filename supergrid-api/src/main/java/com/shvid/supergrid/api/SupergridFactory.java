package com.shvid.supergrid.api;

import com.shvid.supergrid.support.NamespaceNotFoundException;

public interface SupergridFactory {

	/**
	 * Connects to the remote cluster
	 * 
	 * @param url
	 * @param namespace
	 * @return
	 * @throws NamespaceNotFoundException
	 */
	
	SupergridClient connect(String url, String namespace) throws NamespaceNotFoundException;
	
}

