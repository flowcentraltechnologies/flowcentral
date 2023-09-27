/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.common.business;

import java.util.List;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Workspace privilege manager.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface WorkspacePrivilegeManager extends FlowCentralComponent {

    /**
     * Finds role workspace codes.
     * 
     * @param roleCode
     *                 the role code
     * @return list of workspace codes
     * @throws UnifyException
     *                        if an error occurs
     */
    List<String> findRoleWorkspaceCodes(String roleCode) throws UnifyException;

    /**
     * Counts workspaces for role.
     * 
     * @param roleCode
     *                 the role code
     * @return number of workspaces
     * @throws UnifyException
     *                        if an error occurs
     */
    int countRoleWorkspaces(String roleCode) throws UnifyException;

    /**
     * Checks if workspace has a privilege.
     * 
     * @param workspaceCode
     *                      the workspace code
     * @param privilegeCode
     *                      the privilege code
     * @return true if supplied workspace has privilege otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean isWorkspaceWithPrivilege(String workspaceCode, String privilegeCode) throws UnifyException;
}
