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
package com.flowcentraltech.flowcentral.workflow.business;

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.ApplicationWorkItemUtilities;
import com.flowcentraltech.flowcentral.application.data.AppletWorkflowCopyInfo;
import com.flowcentraltech.flowcentral.application.web.widgets.InputArrayEntries;
import com.flowcentraltech.flowcentral.common.business.FlowCentralService;
import com.flowcentraltech.flowcentral.workflow.constants.WfReviewMode;
import com.flowcentraltech.flowcentral.workflow.data.WfChannelDef;
import com.flowcentraltech.flowcentral.workflow.data.WfDef;
import com.flowcentraltech.flowcentral.workflow.data.WfWizardDef;
import com.flowcentraltech.flowcentral.workflow.data.WorkEntityItem;
import com.flowcentraltech.flowcentral.workflow.entities.WfChannel;
import com.flowcentraltech.flowcentral.workflow.entities.WfChannelQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WfStep;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WfWizard;
import com.flowcentraltech.flowcentral.workflow.entities.Workflow;
import com.flowcentraltech.flowcentral.workflow.entities.WorkflowFilter;
import com.flowcentraltech.flowcentral.workflow.entities.WorkflowFilterQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WorkflowQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WorkflowSetValues;
import com.flowcentraltech.flowcentral.workflow.entities.WorkflowSetValuesQuery;
import com.flowcentraltech.flowcentral.workflow.util.WorkflowDesignUtils.DesignType;
import com.tcdng.unify.common.database.WorkEntity;
import com.tcdng.unify.core.UnifyException;

/**
 * Workflow business service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface WorkflowModuleService extends FlowCentralService, ApplicationWorkItemUtilities {

    /**
     * Publishes a workflow.
     * 
     * @param workflowName
     *                     the workflow name
     * @throws UnifyException
     *                        if an error occurs
     */
    void publishWorkflow(String workflowName) throws UnifyException;

    /**
     * Submit work entity instance to workflow by name.
     * 
     * @param workflowName
     *                     the workflow name
     * @param entity
     *                     the entity long name
     * @param id
     *                     the entity ID
     * @throws UnifyException
     *                        if workflow channel is unknown. If instance type does
     *                        not match workflow entity definition. If an error
     *                        occurs
     */
    void submitToWorkflowByName(String workflowName, String entity, Long id) throws UnifyException;

    /**
     * Submit entity instance to workflow by workflow name.
     * 
     * @param workflowName
     *                     the workflow long name
     * @param inst
     *                     the entity instance to submit to workflow
     * @throws UnifyException
     *                        if workflow is unknown. If instance type does not
     *                        match workflow entity definition. If an error occurs
     */
    void submitToWorkflowByName(String workflowName, WorkEntity inst) throws UnifyException;

    /**
     * Submit work entity instance to workflow by channel name.
     * 
     * @param workflowChannelName
     *                            the workflow channel name
     * @param entity
     *                            the entity long name
     * @param id
     *                            the entity ID
     * @return the result code
     * @throws UnifyException
     *                        if workflow channel is unknown. If instance type does
     *                        not match workflow entity definition. If an error
     *                        occurs
     */
    int submitToWorkflowByChannel(String wfDocChannelName, String entity, Long id) throws UnifyException;

    /**
     * Submit work entity instance to workflow by channel name.
     * 
     * @param workflowChannelName
     *                            the workflow channel name
     * @param inst
     *                            the entity instance to submit to workflow
     * @return the result code
     * @throws UnifyException
     *                        if workflow channel is unknown. If instance type does
     *                        not match workflow entity definition. If an error
     *                        occurs
     */
    int submitToWorkflowByChannel(String wfDocChannelName, WorkEntity inst) throws UnifyException;

    /**
     * Submit work entity instances to workflow by channel name.
     * 
     * @param workflowChannelName
     *                            the workflow channel name
     * @param entityList
     *                            the entity instances to submit to workflow
     * @return the result code
     * @throws UnifyException
     *                        if workflow channel is unknown. If instances type does
     *                        not match workflow entity definition. If an error
     *                        occurs
     */
    int submitToWorkflowByChannel(String wfDocChannelName, List<WorkEntity> entityList) throws UnifyException;

    /**
     * Submit work entity instances to workflow by channel name.
     * 
     * @param workflowChannelName
     *                            the workflow channel name
     * @param branchCode
     *                            optional branch code
     * @param departmentCode
     *                            optional department code
     * @param entityList
     *                            the entity instances to submit to workflow
     * @return the result code
     * @throws UnifyException
     *                        if workflow channel is unknown. If instances type does
     *                        not match workflow entity definition. If an error
     *                        occurs
     */
    int submitToWorkflowByChannel(String wfDocChannelName, String branchCode, String departmentCode,
            List<WorkEntity> entityList) throws UnifyException;

    /**
     * Finds workflow by ID.
     * 
     * @param workflowId
     *                   the workflow ID
     * @return the workflow
     * @throws UnifyException
     *                        if workflow with ID is not found. If an error occurs
     */
    Workflow findWorkflow(Long workflowId) throws UnifyException;

    /**
     * Finds workflow ID list for application.
     * 
     * @param applicationName
     *                        the application name
     * @return list of application workflow IDs
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Long> findCustomWorkflowIdList(String applicationName) throws UnifyException;

    /**
     * Finds workflow channel by ID.
     * 
     * @param wfChannelId
     *                    the workflow channel ID
     * @return the workflow channel
     * @throws UnifyException
     *                        if workflow channel with ID is not found. If an error
     *                        occurs
     */
    WfChannel findWfChannel(Long wfChannelId) throws UnifyException;

    /**
     * Finds workflow channel ID list for application.
     * 
     * @param applicationName
     *                        the application name
     * @return list of application workflow channel IDs
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Long> findCustomWfChannelIdList(String applicationName) throws UnifyException;

    /**
     * Finds workflow wizard by ID.
     * 
     * @param wfWizardId
     *                   the workflow wizard ID
     * @return the workflow wizard
     * @throws UnifyException
     *                        if workflow wizard with ID is not found. If an error
     *                        occurs
     */
    WfWizard findWfWizard(Long wfWizardId) throws UnifyException;

    /**
     * Finds workflow wizard ID list for application.
     * 
     * @param applicationName
     *                        the application name
     * @return list of application workflow wizard IDs
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Long> findCustomWfWizardIdList(String applicationName) throws UnifyException;

    /**
     * Finds workflow by ID.
     * 
     * @param workflowId
     *                   the workflow ID
     * @return the workflow
     * @throws UnifyException
     *                        if an error occurs
     */
    Workflow findLeanWorkflowById(Long workflowId) throws UnifyException;

    /**
     * Finds workflows by criteria.
     * 
     * @param query
     *              the query
     * @return list of workflows
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Workflow> findWorkflows(WorkflowQuery query) throws UnifyException;

    /**
     * Finds workflow filters by query.
     * 
     * @param query
     *              the query to use
     * @return list of workflow filters
     * @throws UnifyException
     *                        if an error occurs
     */
    List<WorkflowFilter> findWorkflowFilters(WorkflowFilterQuery query) throws UnifyException;

    /**
     * Finds workflow set-values by query.
     * 
     * @param query
     *              the query to use
     * @return list of workflow set-values
     * @throws UnifyException
     *                        if an error occurs
     */
    List<WorkflowSetValues> findWorkflowSetValues(WorkflowSetValuesQuery query) throws UnifyException;

    /**
     * Finds workflow step by ID.
     * 
     * @param wfStepId
     *                 the workflow step ID
     * @return the workflow step
     * @throws UnifyException
     *                        if an error occurs
     */
    WfStep findLeanWorkflowStepById(Long wfStepId) throws UnifyException;

    /**
     * Finds application ID by workflow step by ID.
     * 
     * @param wfStepId
     *                 the workflow step ID
     * @return the application ID
     * @throws UnifyException
     *                        if an error occurs
     */
    Long findApplicationIdWorkflowStepById(Long wfStepId) throws UnifyException;

    /**
     * Finds workflow steps by query.
     * 
     * @param query
     *              the workflow step query
     * @return the workflow steps
     * @throws UnifyException
     *                        if an error occurs
     */
    List<WfStep> findWorkflowSteps(WfStepQuery query) throws UnifyException;

    /**
     * Gets an application workflow definition.
     * 
     * @param workflowName
     *                     the workflow long name
     * @return the workflow definition
     * @throws UnifyException
     *                        if an error occurs
     */
    WfDef getWfDef(String workflowName) throws UnifyException;

    /**
     * Gets the work entity instance for a workflow item
     * 
     * @param wfItemId
     *                     the workflow item ID
     * @param wfReviewMode
     *                     the review mode
     * @return the workflow item work entity instance
     * @throws UnifyException
     *                        if an error occurs
     */
    WorkEntityItem getWfItemWorkEntityFromWorkItemId(Long wfItemId, WfReviewMode wfReviewMode) throws UnifyException;

    /**
     * Applies user action on workflow item.
     * 
     * @param wfEntityInst
     *                     the workflow item work entity instance
     * @param wfItemId
     *                     the workflow item ID
     * @param stepName
     *                     the workflow step name
     * @param userAction
     *                     the user action to apply
     * @param comment
     *                     optional comment
     * @param emails
     *                     optional email update
     * @param wfReviewMode
     *                     the review mode
     * @param listing
     *                     indicates of listing
     * @return true if successfully applied otherwise false when workflow item is
     *         not in step
     */
    boolean applyUserAction(WorkEntity wfEntityInst, Long wfItemId, String stepName, String userAction,
            WfReviewMode wfReviewMode);

    /**
     * Applies user action on workflow item.
     * 
     * @param wfEntityInst
     *                     the workflow item work entity instance
     * @param wfItemId
     *                     the workflow item ID
     * @param stepName
     *                     the workflow step name
     * @param userAction
     *                     the user action to apply
     * @param comment
     *                     optional comment
     * @param emails
     *                     optional email update
     * @param wfReviewMode
     *                     the review mode
     * @param listing
     *                     indicates of listing
     * @return true if successfully applied otherwise false when workflow item is
     *         not in step
     */
    boolean applyUserAction(WorkEntity wfEntityInst, Long wfItemId, String stepName, String userAction, String comment,
            InputArrayEntries emails, WfReviewMode wfReviewMode, boolean listing);

    /**
     * Gets application workflow wizard definition.
     * 
     * @param wfWizardName
     *                     the workflow wizard long name
     * @return the workflow wizard definition
     * @throws UnifyException
     *                        if an error occurs
     */
    WfWizardDef getWfWizardDef(String wfWizardName) throws UnifyException;

    /**
     * Graduates workflow wizard item from DRAFT to ACTUAL.
     * 
     * @param wfWizardName
     *                     the workflow wizard name
     * @param workEntityId
     *                     the work entity ID
     * @throws UnifyException
     *                        if an error occurs
     */
    void graduateWfWizardItem(String wfWizardName, Long workEntityId) throws UnifyException;

    /**
     * Creates a workflow channel.
     * 
     * @param wfChannel
     *                  the workflow channel to create.
     * @return the ID of the created workflow channel
     * @throws UnifyException
     *                        if an error occurs
     */
    Long createWorkflowChannel(WfChannel wfChannel) throws UnifyException;

    /**
     * Updates a workflow channel.
     * 
     * @param wfChannel
     *                  the workflow channel to update.
     * @return the ID of the updated workflow channel
     * @throws UnifyException
     *                        if an error occurs
     */
    int updateWorkflowChannel(WfChannel wfChannel) throws UnifyException;

    /**
     * Finds workflow channels by criteria.
     * 
     * @param query
     *              the query
     * @return list of workflow channels
     * @throws UnifyException
     *                        if an error occurs
     */
    List<WfChannel> findWorkflowChannels(WfChannelQuery query) throws UnifyException;

    /**
     * Gets application workflow channel.
     * 
     * @param workflowChannelName
     *                            the workflow channel name
     * @return the workflow channel definition
     * @throws UnifyException
     *                        if an error occurs
     */
    WfChannelDef getWfChannelDef(String workflowChannelName) throws UnifyException;
    
    /**
     * Generates workflow steps.
     * 
     * @param type
     *                               the design type
     * @param stepLabel
     *                               the step label
     * @param appletWorkflowCopyInfo
     *                               the workflow copy
     * @return list of steps
     * @throws UnifyException
     *                        if an error occurs
     */
    List<WfStep> generateWorkflowSteps(final DesignType type, final String stepLabel,
            final AppletWorkflowCopyInfo appletWorkflowCopyInfo) throws UnifyException;
}
