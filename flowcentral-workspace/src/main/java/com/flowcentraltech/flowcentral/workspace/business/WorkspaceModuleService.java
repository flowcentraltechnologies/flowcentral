/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.workspace.business;

import java.util.List;

import com.flowcentraltech.flowcentral.common.business.FlowCentralService;
import com.flowcentraltech.flowcentral.workspace.entities.Workspace;
import com.flowcentraltech.flowcentral.workspace.entities.WorkspaceQuery;
import com.tcdng.unify.core.UnifyException;

/**
 * Workspace module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface WorkspaceModuleService extends FlowCentralService {

    /**
     * Finds workspaces.
     * 
     * @param query
     *              the query to search with
     * @return list of workspaces
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Workspace> findWorkspaces(WorkspaceQuery query) throws UnifyException;

    /**
     * Finds workspaces for role.
     * 
     * @param roleCode
     *                 the role code
     * @return list of workspaces
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Workspace> findWorkspaces(String roleCode) throws UnifyException;
}
