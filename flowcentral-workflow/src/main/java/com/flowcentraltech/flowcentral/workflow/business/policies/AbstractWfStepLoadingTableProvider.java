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

import java.util.Map;

import com.flowcentraltech.flowcentral.application.policies.AbstractApplicationLoadingTableProvider;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.flowcentraltech.flowcentral.workflow.constants.WfReviewMode;
import com.flowcentraltech.flowcentral.workflow.entities.WfItemQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
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

    private final String workflowName;

    private final String wfStepName;

    private final String loadingLabel;

    public AbstractWfStepLoadingTableProvider(String workflowName, String wfStepName, String loadingLabel) {
        this.workflowName = workflowName;
        this.wfStepName = wfStepName;
        this.loadingLabel = loadingLabel;
    }

    public final void setWorkflowModuleService(WorkflowModuleService workflowModuleService) {
        this.workflowModuleService = workflowModuleService;
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
    public void commitChange(ValueStore itemValueStore) throws UnifyException {
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
