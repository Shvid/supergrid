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
package com.shvid.supergrid.support;

public class NamespaceNotFoundException extends SupergridException {

	private static final long serialVersionUID = -3537542670090400719L;

	public NamespaceNotFoundException(String namespace) {
		super(namespace);
	}

	public NamespaceNotFoundException(String namespace, Throwable t) {
		super(namespace, t);
	}

}
