/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.application.business;

import java.util.List;

import com.flowcentraltech.flowcentral.application.data.WorkflowLoadingTableInfo;
import com.flowcentraltech.flowcentral.application.data.WorkflowStepInfo;
import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Application work item role utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface ApplicationWorkItemRoleUtilities extends FlowCentralComponent {


    /**
     * Counts workflow loading tables by role.
     * 
     * @param roleCode
     *                 the role code
     * @return number of
     * @throws UnifyException
     *                        if an error occurs
     */
    int countWorkflowLoadingTableInfoByRole(String roleCode) throws UnifyException;

    /**
     * Finds workflow loading tables by role.
     * 
     * @param roleCode
     *                 the role code
     * @return list of workflows
     * @throws UnifyException
     *                        if an error occurs
     */
    List<WorkflowLoadingTableInfo> findWorkflowLoadingTableInfoByRole(String roleCode) throws UnifyException;

    /**
     * Gets loading workflow step by work item event ID.
     * 
     * @param workItemEventId
     *                        the work item event ID
     * @param branchCode
     *                        optional branch code
     * @param departmentCode
     *                        optional department code
     * @return list of workflow steps
     * @throws UnifyException
     *                        if an error occurs
     */
    WorkflowStepInfo getWorkflowLoadingStepInfoByWorkItemEventId(Long workItemEventId, String branchCode,
            String departmentCode) throws UnifyException;

    /**
     * Finds loading workflow steps by role.
     * 
     * @param loadingTableName
     *                         the loading table name
     * @param roleCode
     *                         the role code
     * @param branchCode
     *                         optional branch code
     * @param departmentCode
     *                         optional department code
     * @return list of workflow steps
     * @throws UnifyException
     *                        if an error occurs
     */
    List<WorkflowStepInfo> findWorkflowLoadingStepInfoByRole(String loadingTableName, String roleCode,
            String branchCode, String departmentCode) throws UnifyException;

    /**
     * Finds loading workflow exception steps by role.
     * 
     * @param loadingTableName
     *                         the loading table name
     * @param roleCode
     *                         the role code
     * @param branchCode
     *                         optional branch code
     * @param departmentCode
     *                         optional department code
     * @return list of workflow steps
     * @throws UnifyException
     *                        if an error occurs
     */
    List<WorkflowStepInfo> findWorkflowLoadingExceptionStepInfoByRole(String loadingTableName, String roleCode,
            String branchCode, String departmentCode) throws UnifyException;
}
