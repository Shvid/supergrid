package com.shvid.supergrid.client.config.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.shvid.supergrid.client.config.AuthDefinition;
import com.shvid.supergrid.client.config.ConfigConstants;
import com.shvid.supergrid.client.config.EndpointDefinition;
import com.shvid.supergrid.client.config.ServerDefinition;
import com.shvid.supergrid.client.config.SupergridDefinition;

/**
 * SupergridConfigDocument
 * 
 * @author Alex Shvid
 *
 */

public final class SupergridConfigDocument extends AbstractConfigDocument {
	
	private static final String NAME_ATTRIBUTE = "version";

	private static final String ENDPOINT_TAG = "endpoint";
	private static final String SERVER_TAG = "server";
	private static final String AUTH_TAG = "auth";

	private static final String HOST_ATTRIBUTE = "host";
	private static final String PORT_ATTRIBUTE = "port";
	private static final String USER_ATTRIBUTE = "user";
	private static final String PASSWORD_ATTRIBUTE = "password";

	private final SupergridElement supergridElement;
	
	public SupergridConfigDocument(URL url, Properties props) throws ParserConfigurationException, MalformedURLException, SAXException, IOException {
		super(props);
		Document doc = parseDocument(url);
		this.supergridElement = new SupergridElement(doc);
	}
	
	public SupergridConfigDocument(InputStream is, Properties props) throws ParserConfigurationException, SAXException, IOException {
		super(props);
		Document doc = parseDocument(is);
		this.supergridElement = new SupergridElement(doc);
	}
	
	public SupergridElement getRoot() {
		return supergridElement;
	}
	
	public final class SupergridElement implements SupergridDefinition {
		
		private final String name;
		private final Map<String, EndpointDefinition> endpoints;
		
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
			
			ImmutableMap.Builder<String, EndpointDefinition> endpointsBuilder = ImmutableMap.builder();
			
			NodeList childs = node.getChildNodes();
			int size = childs.getLength();
			for (int i = 0; i != size; ++i) {
				Node child = childs.item(i);
				String tagName = child.getNodeName();
				if (ENDPOINT_TAG.equals(tagName)) {
					EndpointElement e = new EndpointElement(child);
					endpointsBuilder.put(e.getName(), e);
				}
			}
			
			this.endpoints = endpointsBuilder.build();
			
		}
		
		public String getName() {
			return name;
		}

		@Override
		public Map<String, EndpointDefinition> getEndpoints() {
			return endpoints;
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

}
