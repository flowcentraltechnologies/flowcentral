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

package com.flowcentraltech.flowcentral.application.business;

import java.util.List;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.WorkflowLoadingTableInfo;
import com.flowcentraltech.flowcentral.application.data.WorkflowStepInfo;
import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.tcdng.unify.core.UnifyException;

/**
 * Application work item utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface ApplicationWorkItemUtilities extends FlowCentralComponent {

    /**
     * Submit entity instance to workflow channel.
     * 
     * @param entityDef
     *                            the entity definition
     * @param workflowChannelName
     *                            the application workflow channel name
     * @param inst
     *                            the entity instance to submit to workflow
     * @param policyName
     *                            an optional entity action policy name
     * @throws UnifyException
     *                        if workflow is unknown. If instance type does not
     *                        match workflow entity definition. If an error occurs
     */
    EntityActionResult submitToWorkflowChannel(EntityDef entityDef, String workflowChannelName, WorkEntity inst,
            String policyName) throws UnifyException;

    /**
     * Submit entity instance to workflow.
     * 
     * @param entityDef
     *                     the entity definition
     * @param workflowName
     *                     the application workflow name
     * @param inst
     *                     the entity instance to submit to workflow
     * @param policyName
     *                     an optional entity action policy name
     * @throws UnifyException
     *                        if workflow is unknown. If instance type does not
     *                        match workflow entity definition. If an error occurs
     */
    EntityActionResult submitToWorkflow(EntityDef entityDef, String workflowName, WorkEntity inst, String policyName)
            throws UnifyException;

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
     * Gets loading workflow step by work item ID.
     * 
     * @param workItemId
     *                       the work item ID
     * @param branchCode
     *                       optional branch code
     * @param departmentCode
     *                       optional department code
     * @return list of workflow steps
     * @throws UnifyException
     *                        if an error occurs
     */
    WorkflowStepInfo getWorkflowLoadingStepInfoByWorkItemId(Long workItemId, String branchCode, String departmentCode)
            throws UnifyException;

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

    /**
     * Ensures workflows are defined for applet if applet has its workflow copy flag
     * set.
     * 
     * @param appletName
     *                    the applet name
     * @param forceUpdate
     *                    force workflow update
     * @throws UnifyException
     *                        if an error occurs.
     */
    void ensureWorkflowCopyWorkflows(String appletName, boolean forceUpdate) throws UnifyException;

    /**
     * Ensures workflow user interaction loading applets.
     * 
     * @param forceUpdate
     *                    force loading applets update
     * @throws UnifyException
     *                        if an error occurs.
     */
    void ensureWorkflowUserInteractionLoadingApplets(boolean forceUpdate) throws UnifyException;

    /**
     * Ensures workflow user interaction loading applet.
     * 
     * @param loadingTableName
     *                         the loading table name
     * @param forceUpdate
     *                         force loading applet update
     * @throws UnifyException
     *                        if an error occurs
     */
    void ensureWorkflowUserInteractionLoadingApplet(final String loadingTableName, boolean forceUpdate)
            throws UnifyException;
}
