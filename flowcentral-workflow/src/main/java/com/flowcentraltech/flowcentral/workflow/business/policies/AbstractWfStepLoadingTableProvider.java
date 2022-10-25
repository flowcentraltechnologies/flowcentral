/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.workflow.business.policies;

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityItem;
import com.flowcentraltech.flowcentral.application.data.LoadingWorkItemInfo;
import com.flowcentraltech.flowcentral.application.policies.AbstractApplicationLoadingTableProvider;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.flowcentraltech.flowcentral.workflow.constants.WfReviewMode;
import com.flowcentraltech.flowcentral.workflow.data.WfDef;
import com.flowcentraltech.flowcentral.workflow.data.WfStepDef;
import com.flowcentraltech.flowcentral.workflow.entities.WfItemQuery;
import com.flowcentraltech.flowcentral.workflow.util.WorkflowEntityUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Convenient abstract base class for workflow step loading table providers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractWfStepLoadingTableProvider extends AbstractApplicationLoadingTableProvider {

    @Configurable
    private WorkflowModuleService workflowModuleService;

    @Configurable
    private AppletUtilities appletUtilities;

    private final String workflowName;

    private final String wfStepName;

    private final String loadingLabel;

    public AbstractWfStepLoadingTableProvider(String sourceEntity, String workflowName, String wfStepName,
            String loadingLabel) {
        super(sourceEntity);
        this.workflowName = workflowName;
        this.wfStepName = wfStepName;
        this.loadingLabel = loadingLabel;
    }

    public final void setWorkflowModuleService(WorkflowModuleService workflowModuleService) {
        this.workflowModuleService = workflowModuleService;
    }

    public final void setAppletUtilities(AppletUtilities appletUtilities) {
        this.appletUtilities = appletUtilities;
    }

    @Override
    public String getLoadingLabel() throws UnifyException {
        return loadingLabel;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public String getWfStepName() {
        return wfStepName;
    }

    @Override
    public EntityItem getSourceItem(Long sourceItemId) throws UnifyException {
        return workflowModuleService.getWfItemWorkEntity(sourceItemId, null);
    }

    @Override
    public String getSourceItemFormApplet() throws UnifyException {
        WfDef wfDef = workflowModuleService.getWfDef(workflowName);
        WfStepDef wfStepDef = wfDef.getWfStepDef(wfStepName);
        return wfStepDef.isWithApplet() ? wfStepDef.getAppletDef().getLongName() : null;
    }

    @Override
    public LoadingWorkItemInfo getLoadingWorkItemInfo(WorkEntity inst) throws UnifyException {
        WfDef wfDef = workflowModuleService.getWfDef(workflowName);
        WfStepDef wfStepDef = wfDef.getWfStepDef(wfStepName);
        ValueStore currEntityInstValueStore = new BeanValueStore(inst);
        final boolean comments = WorkflowEntityUtils.isWorkflowConditionMatched(appletUtilities,
                currEntityInstValueStore, wfDef, wfStepDef.getComments());
        final boolean emails = WorkflowEntityUtils.isWorkflowConditionMatched(appletUtilities, currEntityInstValueStore,
                wfDef, wfStepDef.getEmails());
        final boolean readOnly = WorkflowEntityUtils.isWorkflowConditionMatched(appletUtilities,
                currEntityInstValueStore, wfDef, wfStepDef.getReadOnlyConditionName());
        return new LoadingWorkItemInfo(wfStepDef.getFormActionDefList(), readOnly, comments, emails,
                wfStepDef.isError());
    }

    @Override
    public final void commitChange(ValueStore itemValueStore) throws UnifyException {
        CommitChangeInfo commitChangeInfo = resolveCommitChangeInfo(itemValueStore);
        if (commitChangeInfo != null && commitChangeInfo.isPresent()) {
            workflowModuleService.applyUserAction(commitChangeInfo.getWorkEntity(), commitChangeInfo.getWfItemId(),
                    wfStepName, commitChangeInfo.getActionName(), commitChangeInfo.getComment(), null,
                    WfReviewMode.NORMAL);
        }
    }

    /**
     * Gets IDs of items in workflow step.
     * 
     * @param heldBy
     *               Optional. If supplied, Restricts fetched items to those held by
     *               user
     * @return a list of work item IDs
     * @throws UnifyException
     *                        if an error occurs
     */
    protected List<Long> getIdsOfItemsInWfStep(String heldBy) throws UnifyException {
        WfItemQuery query = new WfItemQuery();
        query.workflowName(workflowName);
        query.wfStepName(wfStepName);
        if (!StringUtils.isBlank(heldBy)) {
            query.heldBy(heldBy);
        }

        return environment().valueList(Long.class, "id", query);
    }

    protected abstract CommitChangeInfo resolveCommitChangeInfo(ValueStore itemValueStore) throws UnifyException;
    
    protected static class CommitChangeInfo {
        
        private final Long wfItemId;
        
        private final WorkEntity workEntity;
        
        private final String actionName;
        
        private final String comment;

        public CommitChangeInfo(Long wfItemId, WorkEntity workEntity, String actionName, String comment) {
            this.wfItemId = wfItemId;
            this.workEntity = workEntity;
            this.actionName = actionName;
            this.comment = comment;
        }

        public CommitChangeInfo() {
            this.wfItemId = null;
            this.workEntity = null;
            this.actionName = null;
            this.comment = null;
        }
        
        public Long getWfItemId() {
            return wfItemId;
        }

        public WorkEntity getWorkEntity() {
            return workEntity;
        }

        public String getActionName() {
            return actionName;
        }

        public String getComment() {
            return comment;
        }
        
        public boolean isPresent() {
            return wfItemId != null && workEntity != null && !StringUtils.isBlank(actionName);
        }
    }
}
