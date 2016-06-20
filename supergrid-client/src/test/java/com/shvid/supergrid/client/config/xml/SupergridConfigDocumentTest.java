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
package com.shvid.supergrid.client.config.xml;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Optional;
import com.shvid.supergrid.client.config.AuthDefinition;
import com.shvid.supergrid.client.config.CacheDefinition;
import com.shvid.supergrid.client.config.CacheMappingDefinition;
import com.shvid.supergrid.client.config.CacheSettingsDefinition;
import com.shvid.supergrid.client.config.ClientDefinition;
import com.shvid.supergrid.client.config.ServerDefinition;
import com.shvid.supergrid.client.config.SupergridDefinition;

/**
 * SupergridConfigDocumentTest
 * 
 * @author Alex Shvid
 *
 */

public class SupergridConfigDocumentTest {

	@Test
	public void test() throws IOException {
		
		URL url = getClass().getClassLoader().getResource("supergrid-test.xml");
		
		SupergridConfigDocument doc = new SupergridConfigDocument(url, new Properties());
		
		SupergridDefinition root = doc.getRoot();
		
		Assert.assertEquals("supergrid-test", root.getName());
		
		/**
		 * Test client
		 */
    
    ClientDefinition client = root.getClient();
    Assert.assertEquals("super-app", client.getName());
    assertClientSettings(client);
		assertCacheSettings(client.getSettings(), 500, 200);
		
		/**
		 * Test cache-mappings
		 */
		
		List<CacheMappingDefinition> cacheMappings = root.getCacheMappings();
		Assert.assertEquals(2, cacheMappings.size());
		
		CacheMappingDefinition cacheMapping = cacheMappings.get(0);
		Assert.assertEquals("PRD.", cacheMapping.getPattern().pattern());
		Assert.assertEquals("Account", cacheMapping.getKeyspace());

	  CacheSettingsDefinition settings = cacheMapping.getSettings();
		assertCacheSettings(settings, 200, 400);
		
		cacheMapping = cacheMappings.get(1);
		Assert.assertEquals("LT.", cacheMapping.getPattern().pattern());
		Assert.assertEquals("Account", cacheMapping.getKeyspace());
		assertParentCacheSettings(cacheMapping.getSettings());
		
		/**
		 * Test caches
		 */
		
		Map<String, CacheDefinition> caches = root.getCaches();
		Assert.assertEquals(2, caches.size());
		
		CacheDefinition cache = caches.get("TestCache");
		Assert.assertNotNull(cache);
		
		Assert.assertEquals("TestCache", cache.getName());
		Assert.assertEquals("Account", cache.getKeyspace());
		assertCacheSettings(cache.getSettings(), 300, 100);
		
		cache = caches.get("TestCache2");
		Assert.assertNotNull(cache);
		
		Assert.assertEquals("TestCache2", cache.getName());
		Assert.assertEquals("Account", cache.getKeyspace());
		assertParentCacheSettings(cache.getSettings());
		
		
	}

	private void assertCacheSettings(CacheSettingsDefinition settings, int ttl, int timeout) {
		Assert.assertEquals(ttl, settings.getTtlSeconds(false));
		Assert.assertEquals(timeout, settings.getTimeoutMillis(false));
		Assert.assertEquals(ttl, settings.getTtlSeconds(true));
		Assert.assertEquals(timeout, settings.getTimeoutMillis(true));
	}

	private void assertParentCacheSettings(CacheSettingsDefinition settings) {
		Assert.assertEquals(500, settings.getTtlSeconds(true));
		Assert.assertEquals(200, settings.getTimeoutMillis(true));
		Assert.assertEquals(0, settings.getTtlSeconds(false));
		Assert.assertEquals(0, settings.getTimeoutMillis(false));
	}
	
	private void assertClientSettings(ClientDefinition client) {
		
		Assert.assertEquals(1, client.getServers().size());
		
		ServerDefinition server = client.getServers().get(0);
		Assert.assertEquals("localhost", server.getHost());
		Assert.assertEquals(5000, server.getPort());
		
		Optional<AuthDefinition> auth = client.getAuthDefinition();
		Assert.assertTrue(auth.isPresent());

		Assert.assertEquals("test", auth.get().getUser());
		Assert.assertEquals("GnfnDngmF", auth.get().getPassword());
		
		
	}

	
}
