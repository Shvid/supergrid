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
import com.shvid.supergrid.client.config.ClientDefinition;
import com.shvid.supergrid.client.config.EndpointDefinition;
import com.shvid.supergrid.client.config.EndpointRole;
import com.shvid.supergrid.client.config.EntryDefinition;
import com.shvid.supergrid.client.config.GetOperationDefinition;
import com.shvid.supergrid.client.config.KeyspaceDefinition;
import com.shvid.supergrid.client.config.OptionalSettingsDefinition;
import com.shvid.supergrid.client.config.ServerDefinition;
import com.shvid.supergrid.client.config.SetOperationDefinition;
import com.shvid.supergrid.client.config.SupergridDefinition;
import com.shvid.supergrid.client.config.TopologyDefinition;
import com.shvid.supergrid.client.config.TopologyType;

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
		 * Test endpoints
		 */
		
		Map<String, EndpointDefinition> endpoints = root.getEndpoints();
		Assert.assertEquals(2, endpoints.size());
		
		EndpointDefinition clusterA = endpoints.get("clusterA");
		Assert.assertNotNull(clusterA);
		Assert.assertEquals("clusterA", clusterA.getName());
    assertEndpointSettings(clusterA, 5000);
		
		EndpointDefinition clusterB = endpoints.get("clusterB");
		Assert.assertNotNull(clusterB);
		Assert.assertEquals("clusterB", clusterB.getName());
    assertEndpointSettings(clusterB, 7000);

		/**
		 * Test client
		 */
    
    ClientDefinition client = root.getClient();
    Assert.assertEquals("super-app", client.getName());
    Assert.assertEquals("test", client.getNamespace());
    
    TopologyDefinition topology = client.getTopology();
    Assert.assertEquals(TopologyType.FAILOVER, topology.getType());
    
    Map<String, EndpointRole> endpointsRoles = topology.getEndpoints();
		Assert.assertEquals(2, endpointsRoles.size());
		Assert.assertEquals(EndpointRole.ACTIVE, endpointsRoles.get("clusterA"));
		Assert.assertEquals(EndpointRole.STANDBY, endpointsRoles.get("clusterB"));
		
		assertOptionalSettings(client);
		
		/**
		 * Test keyspace
		 */
		
		Map<String, KeyspaceDefinition> keyspaces = root.getKeyspaces();
		Assert.assertEquals(1, keyspaces.size());
		
		KeyspaceDefinition keyspace = keyspaces.get("Account");
		Assert.assertNotNull(keyspace);

		Assert.assertEquals("Account", keyspace.getName());
		Assert.assertTrue(keyspace.getNamespace().isPresent());
		Assert.assertEquals("test", keyspace.getNamespace().get());
		
		/**
		 * Test cache-mappings
		 */
		
		List<CacheMappingDefinition> cacheMappings = root.getCacheMappings();
		Assert.assertEquals(1, cacheMappings.size());
		
		CacheMappingDefinition cacheMapping = cacheMappings.get(0);
		Assert.assertEquals("*", cacheMapping.getPattern());
		Assert.assertEquals("Account", cacheMapping.getKeyspace());

		assertOptionalSettings(cacheMapping);
		
		/**
		 * Test caches
		 */
		
		Map<String, CacheDefinition> caches = root.getCaches();
		Assert.assertEquals(1, caches.size());
		
		CacheDefinition cache = caches.get("TestCache");
		Assert.assertNotNull(cache);
		
		Assert.assertEquals("TestCache", cache.getName());
		Assert.assertEquals("Account", cache.getKeyspace());
		
		assertOptionalSettings(cache);
		
		
	}
	
	private void assertEndpointSettings(EndpointDefinition endpoint, int port) {
		
		Assert.assertEquals(1, endpoint.getServers().size());
		
		ServerDefinition server = endpoint.getServers().get(0);
		Assert.assertEquals("localhost", server.getHost());
		Assert.assertEquals(port, server.getPort());
		
		Optional<AuthDefinition> auth = endpoint.getAuthDefinition();
		Assert.assertTrue(auth.isPresent());

		Assert.assertEquals("test", auth.get().getUser());
		Assert.assertEquals("GnfnDngmF", auth.get().getPassword());
		
		
	}
	
	private void assertOptionalSettings(OptionalSettingsDefinition settings) {
		
		Optional<EntryDefinition> entry = settings.getEntry();
		Assert.assertTrue(entry.isPresent());
		Assert.assertEquals(300, entry.get().getTtlSeconds());
		
		Optional<GetOperationDefinition> getOperation = settings.getGetOperation();
		Assert.assertTrue(getOperation.isPresent());
		Assert.assertEquals(100, getOperation.get().getTimeoutMillis());

		Optional<SetOperationDefinition> setOperation = settings.getSetOperation();
		Assert.assertTrue(setOperation.isPresent());
		Assert.assertEquals(100, setOperation.get().getTimeoutMillis());

	}
	
}
