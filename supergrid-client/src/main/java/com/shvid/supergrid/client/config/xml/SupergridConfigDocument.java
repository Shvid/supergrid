package com.shvid.supergrid.client.config.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

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
import com.shvid.supergrid.client.config.CacheSettingsDefinition;
import com.shvid.supergrid.client.config.CacheSettingsHolder;
import com.shvid.supergrid.client.config.ClientDefinition;
import com.shvid.supergrid.client.config.ConfigConstants;
import com.shvid.supergrid.client.config.ServerDefinition;
import com.shvid.supergrid.client.config.SupergridDefinition;
import com.shvid.supergrid.support.SupergridConfigException;

/**
 * SupergridConfigDocument
 * 
 * @author Alex Shvid
 *
 */

public final class SupergridConfigDocument extends AbstractConfigDocument {
	
	private static final String SERVER_TAG = "server";
	private static final String AUTH_TAG = "auth";
	private static final String CLIENT_TAG = "client";
	private static final String CACHE_MAPPING_TAG = "cache-mapping";
	private static final String CACHE_TAG = "cache";
	private static final String SETTINGS_TAG = "settings";
	
	private static final String NAME_ATTRIBUTE = "name";
	private static final String HOST_ATTRIBUTE = "host";
	private static final String PORT_ATTRIBUTE = "port";
	private static final String USER_ATTRIBUTE = "user";
	private static final String PASSWORD_ATTRIBUTE = "password";
	private static final String TTL_SECONDS_ATTRIBUTE = "ttl-seconds";
	private static final String TIMEOUT_MILLIS_ATTRIBUTE = "timeout-millis";
	private static final String PATTERN_ATTRIBUTE = "pattern";
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
		private final ClientDefinition client;
		private final List<CacheMappingDefinition> cacheMappings;
		private final Map<String, CacheDefinition> caches;
		
		public SupergridElement(Document doc) {
			
			Node node = doc.getFirstChild();
			if (node == null) {
				throw new IllegalStateException("invalid xml document");
			}
			
			NamedNodeMap attributes = getAttributes(node);
			this.name = getRequiredString(attributes, NAME_ATTRIBUTE);
			
			ClientElement clientElementOrNull = null;			
			NodeList childs = node.getChildNodes();
			int size = childs.getLength();
			for (int i = 0; i != size; ++i) {
				Node child = childs.item(i);
				String tagName = child.getNodeName();
				if (CLIENT_TAG.equals(tagName)) {
					clientElementOrNull = new ClientElement(child);
				}						
			}
			
			if (clientElementOrNull == null) {
				throw new SupergridConfigException("'client' tag is required");
			}			
			
			this.client = clientElementOrNull;

			ImmutableList.Builder<CacheMappingDefinition> cacheMappingsBuilder = ImmutableList.builder();
			ImmutableMap.Builder<String, CacheDefinition> cachesBuilder = ImmutableMap.builder();
			for (int i = 0; i != size; ++i) {
				Node child = childs.item(i);
				String tagName = child.getNodeName();
				if (CACHE_MAPPING_TAG.equals(tagName)) {
					cacheMappingsBuilder.add(new CacheMappingElement(clientElementOrNull, child));
				}
				else if (CACHE_TAG.equals(tagName)) {
					CacheElement e = new CacheElement(clientElementOrNull, child);
					cachesBuilder.put(e.getName(), e);
				}								
			}

			this.cacheMappings = cacheMappingsBuilder.build();
			this.caches = cachesBuilder.build();
			
		}
		
		public String getName() {
			return name;
		}

		@Override
		public ClientDefinition getClient() {
			return client;
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

	public final class ServerElement implements ServerDefinition {
		
		private final String host;
		private final int port;

		public ServerElement(Node node) {
			NamedNodeMap attributes = getAttributes(node);
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
			NamedNodeMap attributes = getAttributes(node);
			this.user = getRequiredString(attributes, USER_ATTRIBUTE);
			this.password = getRequiredString(attributes, PASSWORD_ATTRIBUTE);
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

	public final class ClientElement extends AbstractOptionalSettingsHolder implements ClientDefinition {
		
		private final String name;
		private final List<ServerDefinition> servers;
		private final Optional<AuthDefinition> auth;
		
		public ClientElement(Node node) {
			super(null, node);
			
			NamedNodeMap attributes = getAttributes(node);
			this.name = getRequiredString(attributes, NAME_ATTRIBUTE);
			
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
	
	public final class CacheMappingElement extends AbstractOptionalSettingsHolder implements CacheMappingDefinition {
		
		private final Pattern pattern;
		private final String keyspace;

		public CacheMappingElement(AbstractOptionalSettingsHolder parent, Node node) {
			super(parent, node);
			
			NamedNodeMap attributes = getAttributes(node);
			
			this.pattern = Pattern.compile(getRequiredString(attributes, PATTERN_ATTRIBUTE));
			this.keyspace = getRequiredString(attributes, KEYSPACE_ATTRIBUTE);
			
		}

		@Override
		public Pattern getPattern() {
			return pattern;
		}

		@Override
		public String getKeyspace() {
			return keyspace;
		}

	}
	
	public final class CacheElement extends AbstractOptionalSettingsHolder implements CacheDefinition {
		
		private final String name;
		private final String keyspace;

		public CacheElement(AbstractOptionalSettingsHolder parent, Node node) {
			super(parent, node);
			
			NamedNodeMap attributes = getAttributes(node);

			this.name = getRequiredString(attributes, NAME_ATTRIBUTE);
			this.keyspace = getRequiredString(attributes, KEYSPACE_ATTRIBUTE);

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
	
	public abstract class AbstractOptionalSettingsHolder implements CacheSettingsHolder {
		
		private final CacheSettingsDefinition settings;

		AbstractOptionalSettingsHolder(AbstractOptionalSettingsHolder parentOrNull, Node node) {
			
			CacheSettingsDefinition settingsElementOrNull = null;
			
			NodeList childs = node.getChildNodes();
			int size = childs.getLength();
			for (int i = 0; i != size; ++i) {
				Node child = childs.item(i);
				String tagName = child.getNodeName();
				if (SETTINGS_TAG.equals(tagName)) {
					settingsElementOrNull = new CacheSettingsElement(
							parentOrNull != null ? parentOrNull.getSettings() : null, child);
				}
			}

			if (settingsElementOrNull == null) {
				settingsElementOrNull = new CacheSettingsElement(parentOrNull != null ? parentOrNull.getSettings() : null);
			}
			
			this.settings = settingsElementOrNull;
		}
		
		@Override
		public CacheSettingsDefinition getSettings() {
			return settings;
		}
		
	}
	
	public final class CacheSettingsElement implements CacheSettingsDefinition {
		
		private final CacheSettingsDefinition parentOrNull;
		private final int ttlSeconds;
		private final int timeoutMillis;

		public CacheSettingsElement(CacheSettingsDefinition parentOrNull) {
			this.parentOrNull = parentOrNull;
			this.ttlSeconds = ConfigConstants.DEFAULT_TTL;
			this.timeoutMillis = ConfigConstants.DEFAULT_TIMEOUT;
		}
		
		public CacheSettingsElement(CacheSettingsDefinition parentOrNull, Node node) {
			this.parentOrNull = parentOrNull;
			NamedNodeMap attributes = getAttributes(node);
			this.ttlSeconds = getInteger(attributes, TTL_SECONDS_ATTRIBUTE, ConfigConstants.DEFAULT_TTL);
			this.timeoutMillis = getInteger(attributes, TIMEOUT_MILLIS_ATTRIBUTE, ConfigConstants.DEFAULT_TIMEOUT);
		}
		
		@Override
		public int getTtlSeconds(boolean effective) {
			if (effective && ttlSeconds == ConfigConstants.DEFAULT_TTL && parentOrNull != null) {
				return parentOrNull.getTtlSeconds(effective);
			}
			return ttlSeconds;
		}
		
		@Override
		public int getTimeoutMillis(boolean effective) {
			if (effective && timeoutMillis == ConfigConstants.DEFAULT_TIMEOUT && parentOrNull != null) {
				return parentOrNull.getTimeoutMillis(effective);
			}
			return timeoutMillis;
		}
		
		
		
	}
	

}
