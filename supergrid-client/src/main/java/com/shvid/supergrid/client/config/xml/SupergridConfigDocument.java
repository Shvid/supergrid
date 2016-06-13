package com.shvid.supergrid.client.config.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.shvid.supergrid.client.config.AuthDefinition;
import com.shvid.supergrid.client.config.CacheDefinition;
import com.shvid.supergrid.client.config.CacheMappingDefinition;
import com.shvid.supergrid.client.config.ClientDefinition;
import com.shvid.supergrid.client.config.ConfigConstants;
import com.shvid.supergrid.client.config.DataDistributionType;
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
import com.shvid.supergrid.support.SupergridConfigException;

/**
 * SupergridConfigDocument
 * 
 * @author Alex Shvid
 *
 */

public final class SupergridConfigDocument extends AbstractConfigDocument {
	
	private static final String ENDPOINT_TAG = "endpoint";
	private static final String SERVER_TAG = "server";
	private static final String AUTH_TAG = "auth";
	private static final String CLIENT_TAG = "client";
	private static final String KEYSPACE_TAG = "keyspace";
	private static final String CACHE_MAPPING_TAG = "cache-mapping";
	private static final String CACHE_TAG = "cache";
	private static final String TOPOLOGY_TAG = "topology";
	private static final String ACTIVE_TAG = "active";
	private static final String STANDBY_TAG = "standby";
	private static final String ENTRY_TAG = "entry";
	private static final String GET_OPERATION_TAG = "get-operation";
	private static final String SET_OPERATION_TAG = "set-operation";
	
	private static final String NAME_ATTRIBUTE = "name";
	private static final String NAMESPACE_ATTRIBUTE = "namespace";
	private static final String HOST_ATTRIBUTE = "host";
	private static final String PORT_ATTRIBUTE = "port";
	private static final String USER_ATTRIBUTE = "user";
	private static final String PASSWORD_ATTRIBUTE = "password";
	private static final String TYPE_ATTRIBUTE = "type";
	private static final String ENDPOINT_ATTRIBUTE = "endpoint";
	private static final String TTL_SECONDS_ATTRIBUTE = "ttl-seconds";
	private static final String TIMEOUT_MILLIS_ATTRIBUTE = "timeout-millis";
	private static final String PATTERN_ATTRIBUTE = "pattern";
	private static final String DATA_DISTRIBUTION_ATTRIBUTE = "data-distribution";
	private static final String KEYSPACE_ATTRIBUTE = "keyspace";
	
	private final SupergridElement supergridElement;
	
	public SupergridConfigDocument(URL url, Properties props) throws IOException {
		super(props);
		Document doc = parseDocument(url);
		this.supergridElement = new SupergridElement(doc);
	}
	
	public SupergridConfigDocument(InputStream is, Properties props) throws IOException {
		super(props);
		Document doc = parseDocument(is);
		this.supergridElement = new SupergridElement(doc);
	}
	
	public SupergridDefinition getRoot() {
		return supergridElement;
	}
	
	public final class SupergridElement implements SupergridDefinition {
		
		private final String name;
		private final Map<String, EndpointDefinition> endpoints;
		private final ClientDefinition client;
		private final Map<String, KeyspaceDefinition> keyspaces;
		private final List<CacheMappingDefinition> cacheMappings;
		private final Map<String, CacheDefinition> caches;
		
		public SupergridElement(Document doc) {
			
			Node node = doc.getFirstChild();
			if (node == null) {
				throw new IllegalStateException("invalid xml document");
			}
			
			NamedNodeMap attributes = node.getAttributes();
			if (attributes == null) {
				throw new IllegalStateException("invalid xml document");
			}
			
			this.name = getString(attributes, NAME_ATTRIBUTE, ConfigConstants.DEFAULT_NAME);
			
			ClientElement clientElementOrNull = null;
			ImmutableMap.Builder<String, EndpointDefinition> endpointsBuilder = ImmutableMap.builder();
			ImmutableMap.Builder<String, KeyspaceDefinition> keyspacesBuilder = ImmutableMap.builder();
			ImmutableList.Builder<CacheMappingDefinition> cacheMappingsBuilder = ImmutableList.builder();
			ImmutableMap.Builder<String, CacheDefinition> cachesBuilder = ImmutableMap.builder();
			
			NodeList childs = node.getChildNodes();
			int size = childs.getLength();
			for (int i = 0; i != size; ++i) {
				Node child = childs.item(i);
				String tagName = child.getNodeName();
				if (ENDPOINT_TAG.equals(tagName)) {
					EndpointElement e = new EndpointElement(child);
					endpointsBuilder.put(e.getName(), e);
				}
				else if (CLIENT_TAG.equals(tagName)) {
					clientElementOrNull = new ClientElement(child);
				}
				else if (KEYSPACE_TAG.equals(tagName)) {
					KeyspaceElement e = new KeyspaceElement(child);
					keyspacesBuilder.put(e.getName(), e);
				}
				else if (CACHE_MAPPING_TAG.equals(tagName)) {
					cacheMappingsBuilder.add(new CacheMappingElement(child));
				}
				else if (CACHE_TAG.equals(tagName)) {
					CacheElement e = new CacheElement(child);
					cachesBuilder.put(e.getName(), e);
				}								
			}
			
			this.endpoints = endpointsBuilder.build();

			if (clientElementOrNull == null) {
				throw new SupergridConfigException("client tag not found");
			}			
			this.client = clientElementOrNull;

			this.keyspaces = keyspacesBuilder.build();
			this.cacheMappings = cacheMappingsBuilder.build();
			this.caches = cachesBuilder.build();
			
		}
		
		public String getName() {
			return name;
		}

		@Override
		public Map<String, EndpointDefinition> getEndpoints() {
			return endpoints;
		}

		@Override
		public ClientDefinition getClient() {
			return client;
		}

		@Override
		public Map<String, KeyspaceDefinition> getKeyspaces() {
			return keyspaces;
		}

		@Override
		public List<CacheMappingDefinition> getCacheMappings() {
			return cacheMappings;
		}

		@Override
		public Map<String, CacheDefinition> getCaches() {
			return caches;
		}
		
	}


	public final class EndpointElement implements EndpointDefinition {
		
		private final String name;
		private final List<ServerDefinition> servers;
		private final Optional<AuthDefinition> auth;
		
		public EndpointElement(Node node) {
			
			NamedNodeMap attributes = node.getAttributes();
			if (attributes == null) {
				throw new IllegalStateException("invalid xml node");
			}
			this.name = getString(attributes, NAME_ATTRIBUTE, ConfigConstants.DEFAULT_NAME);
			
			ImmutableList.Builder<ServerDefinition> serversBuilder = ImmutableList.builder();

			AuthDefinition authElementOrNull = null;
			NodeList childs = node.getChildNodes();
			int size = childs.getLength();
			for (int i = 0; i != size; ++i) {
				Node child = childs.item(i);
				String tagName = child.getNodeName();
				if (SERVER_TAG.equals(tagName)) {
					serversBuilder.add(new ServerElement(child));
				}
				else if (AUTH_TAG.equals(tagName)) {
					authElementOrNull = new AuthElement(child);
				}
			}
			
			this.servers = serversBuilder.build();
			this.auth = Optional.fromNullable(authElementOrNull);
			
		}
		
		@Override
		public String getName() {
			return name;
		}

		@Override
		public List<ServerDefinition> getServers() {
			return servers;
		}

		@Override
		public Optional<AuthDefinition> getAuthDefinition() {
			return auth;
		}
		
	}
	
	public final class ServerElement implements ServerDefinition {
		
		private final String host;
		private final int port;

		public ServerElement(Node node) {
			
			NamedNodeMap attributes = node.getAttributes();
			if (attributes == null) {
				throw new IllegalStateException("invalid xml node");
			}
			this.host = getString(attributes, HOST_ATTRIBUTE, ConfigConstants.DEFAULT_HOST);
			this.port = getInteger(attributes, PORT_ATTRIBUTE, ConfigConstants.DEFAULT_PORT);
		}
		
		@Override
		public String getHost() {
			return host;
		}

		@Override
		public int getPort() {
			return port;
		}
		
	}
	
	public final class AuthElement implements AuthDefinition {
		
		private final String user;
		private final String password;

		public AuthElement(Node node) {
			
			NamedNodeMap attributes = node.getAttributes();
			if (attributes == null) {
				throw new IllegalStateException("invalid xml node");
			}
			this.user = getString(attributes, USER_ATTRIBUTE, ConfigConstants.DEFAULT_USER);
			this.password = getString(attributes, PASSWORD_ATTRIBUTE, ConfigConstants.DEFAULT_PASSWORD);
		}
		
		@Override
		public String getUser() {
			return user;
		}

		@Override
		public String getPassword() {
			return password;
		}
		
	}

	public final class ClientElement extends AbstractOptionalConfigurations implements ClientDefinition {
		
		private final String name;
		private final String namespace;
		private final TopologyDefinition topology;

		public ClientElement(Node node) {
			super(node);
			
			NamedNodeMap attributes = getAttributes(node);
			this.name = getString(attributes, NAME_ATTRIBUTE, ConfigConstants.DEFAULT_NAME);
			this.namespace = getString(attributes, NAMESPACE_ATTRIBUTE, ConfigConstants.DEFAULT_NAMESPACE);
			
			TopologyDefinition topologyElementOrNull = null;
			
			NodeList childs = node.getChildNodes();
			int size = childs.getLength();
			for (int i = 0; i != size; ++i) {
				Node child = childs.item(i);
				String tagName = child.getNodeName();
				if (TOPOLOGY_TAG.equals(tagName)) {
					topologyElementOrNull = new TopologyElement(child);
				}
			}

			if (topologyElementOrNull == null) {
				throw new SupergridConfigException("topology not found for client");
			}
			this.topology = topologyElementOrNull;
		}
		
		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getNamespace() {
			return namespace;
		}

		@Override
		public TopologyDefinition getTopology() {
			return topology;
		}
		
	}

	
	public final class TopologyElement implements TopologyDefinition {
		
		private final TopologyType type;
		private final Map<String, EndpointRole> endpoints;

		public TopologyElement(Node node) {
			
			NamedNodeMap attributes = getAttributes(node);

			this.type = getEnum(attributes, TYPE_ATTRIBUTE, TopologyType.Parser.INSTANCE);
			
			if (this.type == null) {
				throw new SupergridConfigException("topology type is undefinded");
			}
			
			ImmutableMap.Builder<String, EndpointRole> endpointsBuilder = ImmutableMap.builder();

			NodeList childs = node.getChildNodes();
			int size = childs.getLength();
			for (int i = 0; i != size; ++i) {
				Node child = childs.item(i);
				String tagName = child.getNodeName();
				if (ACTIVE_TAG.equals(tagName)) {
					TopologyEndpointElement e = new TopologyEndpointElement(child);
					endpointsBuilder.put(e.getEndpoint(), EndpointRole.ACTIVE);
				}
				else if (STANDBY_TAG.equals(tagName)) {
					TopologyEndpointElement e = new TopologyEndpointElement(child);
					endpointsBuilder.put(e.getEndpoint(), EndpointRole.STANDBY);
				}
			}

			this.endpoints = endpointsBuilder.build();
			
		}

		@Override
		public TopologyType getType() {
			return type;
		}

		@Override
		public Map<String, EndpointRole> getEndpoints() {
			return endpoints;
		}
		
	}
	
	public final class TopologyEndpointElement {
		
		private final String endpoint;

		public TopologyEndpointElement(Node node) {
			NamedNodeMap attributes = getAttributes(node);
			this.endpoint = getString(attributes, ENDPOINT_ATTRIBUTE, ConfigConstants.DEFAULT_NAME);
		}
		
		public String getEndpoint() {
			return endpoint;
		}
		
	}
	
	public final class EntryElement implements EntryDefinition {
		
		private final int ttlSeconds;

		public EntryElement(Node node) {
			NamedNodeMap attributes = getAttributes(node);
			this.ttlSeconds = getInteger(attributes, TTL_SECONDS_ATTRIBUTE, ConfigConstants.DEFAULT_TTL);
		}

		@Override
		public int getTtlSeconds() {
			return ttlSeconds;
		}
		
	}
	
	public final class GetOperationElement implements GetOperationDefinition {
		
		private final int timeoutMillis;

		public GetOperationElement(Node node) {
			NamedNodeMap attributes = getAttributes(node);
			this.timeoutMillis = getInteger(attributes, TIMEOUT_MILLIS_ATTRIBUTE, ConfigConstants.DEFAULT_TIMEOUT);
		}

		@Override
		public int getTimeoutMillis() {
			return timeoutMillis;
		}

	}
	
	public final class SetOperationElement implements SetOperationDefinition {
		
		private final int timeoutMillis;

		public SetOperationElement(Node node) {
			NamedNodeMap attributes = getAttributes(node);
			this.timeoutMillis = getInteger(attributes, TIMEOUT_MILLIS_ATTRIBUTE, ConfigConstants.DEFAULT_TIMEOUT);
		}

		@Override
		public int getTimeoutMillis() {
			return timeoutMillis;
		}

	}
	
	public final class KeyspaceElement implements KeyspaceDefinition {
		
		private final String name;
		private final Optional<String> namespace;
		private final DataDistributionType type;

		public KeyspaceElement(Node node) {
			NamedNodeMap attributes = getAttributes(node);
			this.name = getString(attributes, NAME_ATTRIBUTE, ConfigConstants.DEFAULT_NAME);
			
			String namespaceOrNull = getString(attributes, NAMESPACE_ATTRIBUTE);
			this.namespace = Optional.fromNullable(namespaceOrNull);
			
			this.type = getEnum(attributes, DATA_DISTRIBUTION_ATTRIBUTE, DataDistributionType.Parser.INSTANCE);
			if (this.type == null) {
				throw new SupergridConfigException("empty data-distribution in keyspace");
			}
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public Optional<String> getNamespace() {
			return namespace;
		}

		@Override
		public DataDistributionType getDataDistribution() {
			return type;
		}

	}

	public final class CacheMappingElement extends AbstractOptionalConfigurations implements CacheMappingDefinition {
		
		private final String pattern;
		private final String keyspace;

		public CacheMappingElement(Node node) {
			super(node);
			
			NamedNodeMap attributes = getAttributes(node);
			this.pattern = getString(attributes, PATTERN_ATTRIBUTE, ConfigConstants.DEFAULT_PATTERN);
			
			this.keyspace = getString(attributes, KEYSPACE_ATTRIBUTE);
			if (this.keyspace == null) {
				throw new SupergridConfigException("empty keyspace attribute in cache-mapping: " + node);
			}
			
		}

		@Override
		public String getPattern() {
			return pattern;
		}

		@Override
		public String getKeyspace() {
			return keyspace;
		}

	}
	
	public final class CacheElement extends AbstractOptionalConfigurations implements CacheDefinition {
		
		private final String name;
		private final String keyspace;

		public CacheElement(Node node) {
			super(node);
			
			NamedNodeMap attributes = getAttributes(node);
			this.name = getString(attributes, NAME_ATTRIBUTE, ConfigConstants.DEFAULT_NAME);
			
			this.keyspace = getString(attributes, KEYSPACE_ATTRIBUTE);
			if (this.keyspace == null) {
				throw new SupergridConfigException("empty keyspace attribute in cache-mapping: " + node);
			}

		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getKeyspace() {
			return keyspace;
		}

	}
	
	public abstract class AbstractOptionalConfigurations implements OptionalSettingsDefinition {
		
		private final Optional<EntryDefinition> entry;
		private final Optional<GetOperationDefinition> getOperation;
		private final Optional<SetOperationDefinition> setOperation;
		
		AbstractOptionalConfigurations(Node node) {

			EntryDefinition entryElementOrNull = null;
			GetOperationDefinition getOperationElementOrNull = null;
			SetOperationDefinition setOperationElementOrNull = null;
			
			NodeList childs = node.getChildNodes();
			int size = childs.getLength();
			for (int i = 0; i != size; ++i) {
				Node child = childs.item(i);
				String tagName = child.getNodeName();
				if (ENTRY_TAG.equals(tagName)) {
					entryElementOrNull = new EntryElement(child);
				}
				else if (GET_OPERATION_TAG.equals(tagName)) {
					getOperationElementOrNull = new GetOperationElement(child);
				}
				else if (SET_OPERATION_TAG.equals(tagName)) {
					setOperationElementOrNull = new SetOperationElement(child);
				}
			}

			this.entry = Optional.fromNullable(entryElementOrNull);
			this.getOperation = Optional.fromNullable(getOperationElementOrNull);
			this.setOperation = Optional.fromNullable(setOperationElementOrNull);
		}
		
		@Override
		public Optional<EntryDefinition> getEntry() {
			return entry;
		}

		@Override
		public Optional<GetOperationDefinition> getGetOperation() {
			return getOperation;
		}

		@Override
		public Optional<SetOperationDefinition> getSetOperation() {
			return setOperation;
		}

		
	}
	
}
