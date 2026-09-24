/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.flowcentraltech.flowcentral.application.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Application definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ApplicationAllPaths { 

	private Set<String> paths;

	private Map<String, Set<String>> rolePaths;

	public ApplicationAllPaths(List<String> _paths) {
		this.paths = Collections.unmodifiableSet(new HashSet<String>(_paths));
		this.rolePaths = new HashMap<String, Set<String>>();
	}

	public Set<String> getPaths() {
		return paths;
	}

	public boolean isApplicationPath(String path) {
		return paths.contains(path);
	}

	public synchronized void setRolePaths(String role, List<String> _paths) {
		rolePaths.put(role, Collections.unmodifiableSet(new HashSet<String>(_paths)));
	}

	public synchronized boolean isRolePath(String roleCode, String path) {
		return roleCode != null ? (rolePaths.containsKey(roleCode) ? rolePaths.get(roleCode).contains(path) : false)
				: true;
	}

	public synchronized boolean isWithRolePaths(String role) {
		return rolePaths.containsKey(role);
	}

	public synchronized void invalidateRole(String roleCode) {
		rolePaths.remove(roleCode);
	}
}
