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
package com.flowcentraltech.flowcentral.workflow.web.widgets;

import java.util.Map;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityItem;
import com.flowcentraltech.flowcentral.application.data.LoadingWorkItemInfo;
import com.flowcentraltech.flowcentral.application.policies.AbstractApplicationLoadingTableProvider;
import com.flowcentraltech.flowcentral.application.web.widgets.InputArrayEntries;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.flowcentraltech.flowcentral.workflow.constants.WfReviewMode;
import com.flowcentraltech.flowcentral.workflow.data.WfDef;
import com.flowcentraltech.flowcentral.workflow.data.WfStepDef;
import com.flowcentraltech.flowcentral.workflow.entities.WfItemQuery;
import com.flowcentraltech.flowcentral.workflow.util.WorkflowEntityUtils;
import com.tcdng.unify.common.database.WorkEntity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.RequirementType;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Convenient abstract base class for workflow step loading table providers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractWfStepLoadingTableProvider extends AbstractApplicationLoadingTableProvider {

    @Configurable
    private WorkflowModuleService workflowModuleService;

    @Configurable
    private AppletUtilities appletUtilities;

    private final WfReviewMode wfReviewMode;

    private final String workflowName;

    private final String wfStepName;

    private final String loadingLabel;

    public AbstractWfStepLoadingTableProvider(WfReviewMode wfReviewMode, String sourceEntity, String workflowName,
            String wfStepName, String loadingLabel) {
        super(sourceEntity);
        this.wfReviewMode = wfReviewMode;
        this.workflowName = workflowName;
        this.wfStepName = wfStepName;
        this.loadingLabel = loadingLabel;
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
    public EntityItem getSourceItemById(Long sourceItemId, int options) throws UnifyException {
        return workflowModuleService.getWfItemWorkEntityFromWorkItemId(sourceItemId, wfReviewMode);
    }

    @Override
    public String getSourceItemFormApplet() throws UnifyException {
        WfDef wfDef = workflowModuleService.getWfDef(workflowName);
        WfStepDef wfStepDef = wfDef.getWfStepDef(wfStepName);
        return wfStepDef.getStepAppletName();
    }

    @Override
    public LoadingWorkItemInfo getLoadingWorkItemInfo(WorkEntity inst) throws UnifyException {
        WfDef wfDef = workflowModuleService.getWfDef(workflowName);
        WfStepDef wfStepDef = wfDef.getWfStepDef(wfStepName);
        ValueStoreReader reader = new BeanValueStore(inst).getReader();
        final boolean comments = WorkflowEntityUtils.isWorkflowConditionMatched(appletUtilities, reader, wfDef,
                wfStepDef.getComments());
        final boolean emails = WorkflowEntityUtils.isWorkflowConditionMatched(appletUtilities, reader, wfDef,
                wfStepDef.getEmails());
        final boolean readOnly = WorkflowEntityUtils.isWorkflowConditionMatched(appletUtilities, reader, wfDef,
                wfStepDef.getReadOnlyConditionName());
        return new LoadingWorkItemInfo(wfStepDef.getFormActionDefList(), wfDef.getDescription(), wfStepDef.getLabel(),
                readOnly, comments, emails, wfStepDef.isError(), wfStepDef.isWithAttachmentProviderName());
    }

    @Override
    public boolean applyUserAction(WorkEntity wfEntityInst, Long sourceItemId, String userAction, String comment,
            InputArrayEntries emails, boolean listing) throws UnifyException {
        return workflowModuleService.applyUserAction(wfEntityInst, sourceItemId, wfStepName, userAction, comment,
                emails, wfReviewMode, listing);
    }

    @Override
    public boolean isNewCommentRequired(String userAction) throws UnifyException {
        WfDef wfDef = workflowModuleService.getWfDef(workflowName);
        WfStepDef wfStepDef = wfDef.getWfStepDef(wfStepName);
        return RequirementType.MANDATORY.equals(wfStepDef.getUserActionDef(userAction).getCommentRequirement());
    }

    @Override
    public final void commitChange(ValueStore itemValueStore, boolean listing) throws UnifyException {
        CommitChangeInfo commitChangeInfo = resolveCommitChangeInfo(itemValueStore);
        if (commitChangeInfo != null && commitChangeInfo.isPresent()) {
            workflowModuleService.applyUserAction(commitChangeInfo.getWorkEntity(), commitChangeInfo.getWfItemId(),
                    wfStepName, commitChangeInfo.getActionName(), commitChangeInfo.getComment(), null, wfReviewMode,
                    listing);
        }
    }

    /**
     * Gets IDs of items in workflow step.
     * 
     * @param heldBy
     *               Optional. If supplied, Restricts fetched items to those held by
     *               user
     * @return a map of work item IDs by workflow entity IDs
     * @throws UnifyException
     *                        if an error occurs
     */
    protected Map<Long, Long> getIdsOfItemsInWfStep(String heldBy) throws UnifyException {
        WfItemQuery query = new WfItemQuery();
        query.workflowName(workflowName);
        query.wfStepName(wfStepName);
        if (!StringUtils.isBlank(heldBy)) {
            query.heldBy(heldBy);
        }

        return environment().valueMap(Long.class, "workRecId", Long.class, "id", query);
    }

    protected WfReviewMode getWfReviewMode() {
        return wfReviewMode;
    }

    protected WorkflowModuleService workflow() {
        return workflowModuleService;
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
