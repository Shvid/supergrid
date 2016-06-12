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
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.shvid.supergrid.client.config.PropertyPlaceholder;
import com.shvid.supergrid.support.SupergridConfigException;

/**
 * AbstractConfigDocument for xml config parsing
 * 
 * @author Alex Shvid
 *
 */

abstract class AbstractConfigDocument {

	private static Logger logger = LoggerFactory.getLogger(AbstractConfigDocument.class);
	
	protected final PropertyPlaceholder propertyPlaceholder;
	
	AbstractConfigDocument(Properties props) {
		this.propertyPlaceholder = new PropertyPlaceholder(props);
	}
	
	protected Document parseDocument(URL url) throws IOException, ParserConfigurationException, SAXException {
    if (url == null) {
      throw new SupergridConfigException("empty url");
    }
		
		InputStream is = url.openStream();
		try {
			return parseDocument(is);
		}
		finally {
			try {
				is.close();
			}
			catch(IOException e) {
				logger.warn("Error parsing document", e);
			}
		}
	}
	
	protected Document parseDocument(InputStream is) throws ParserConfigurationException, SAXException, IOException {
    if (is == null) {
      throw new SupergridConfigException("empty input stream");
    }
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		return docBuilder.parse(is);
	}
	
	protected NamedNodeMap getAttributes(Node node) {
		NamedNodeMap attributes = node.getAttributes();
		if (attributes == null) {
			throw new IllegalStateException("invalid xml node");
		}
		return attributes;
	}

	protected String getString(NamedNodeMap attributes, String name) {
		return getString(attributes, name, null);
	}
	
	protected String getString(NamedNodeMap attributes, String name, String defaultValue) {
		Node attribute = attributes.getNamedItem(name);
		if (attribute != null) {
			String val = attribute.getNodeValue();
			if (val != null && val.length() > 0) {
				val = propertyPlaceholder.replaceAll(val);
				if (val != null && val.length() > 0) {
					return val;
				}
			}
		}
		return defaultValue;
	}
	
	protected Integer getInteger(NamedNodeMap attributes, String name) {
		String str = getString(attributes, name);
		if (str != null) {
			try {
				return Integer.parseInt(str);
			} catch (NumberFormatException e) {
				throw new SupergridConfigException("wrong number in attribute " + name, e);
			}
		}
		return null;
	}
	
	protected int getInteger(NamedNodeMap attributes, String name, int defaultValue) {
		String str = getString(attributes, name);
		if (str != null) {
			try {
				return Integer.parseInt(str);
			} catch (NumberFormatException e) {
				throw new SupergridConfigException("wrong number in attribute " + name, e);
			}
		}
		return defaultValue;
	}
	
	protected Boolean getBoolean(NamedNodeMap attributes, String name) {
		String str = getString(attributes, name);
		if (str != null) {
			return Boolean.parseBoolean(str);
		}
		return null;
	}
	
	protected boolean getBoolean(NamedNodeMap attributes, String name, boolean defaultValue) {
		String str = getString(attributes, name);
		if (str != null) {
			return Boolean.parseBoolean(str);
		}
		return defaultValue;
	}

	protected <E extends Enum<E>> E getEnum(NamedNodeMap attributes, String name, EnumParser<E> parser) {
		return getEnum(attributes, name, parser, null);
	}
	
	protected <E extends Enum<E>> E getEnum(NamedNodeMap attributes, String name, EnumParser<E> parser, E defaultValue) {
		String str = getString(attributes, name);
		if (str != null) {
			return parser.parse(str);
		}
		return defaultValue;
	}
	
}
