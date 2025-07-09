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
package com.flowcentraltech.flowcentral.workflow.business.policies;

import java.util.List;

import com.flowcentraltech.flowcentral.common.business.policies.EntityListActionContext;
import com.flowcentraltech.flowcentral.workflow.constants.WfReviewMode;
import com.flowcentraltech.flowcentral.workflow.constants.WorkflowModuleNameConstants;
import com.flowcentraltech.flowcentral.workflow.entities.WfItemQuery;
import com.flowcentraltech.flowcentral.workflow.util.WorkflowNameUtils;
import com.flowcentraltech.flowcentral.workflow.util.WorkflowNameUtils.WfStepLongNameParts;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.common.database.WorkEntity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Work-item entity list action policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(name = WorkflowModuleNameConstants.WORKFLOW_WORKITEMS_TABLE_ACTION_POLICY, description = "Apply User Actions to Workitems")
public class WorkItemEntityListActionPolicy extends AbstractWfEntityListActionPolicy {

    @Override
    protected Object doExecuteAction(EntityListActionContext ctx, List<? extends Entity> items, String userAction)
            throws UnifyException {
        logDebug("Applying user action [{0}] to multiple [{1}] work items...", userAction, items.size());
        int applied = 0;
        for (Entity inst : items) {
            final String source = ctx.getLoadingSource(inst);
            if (!StringUtils.isBlank(source)) {
                logDebug("Using source [{0}] on item with id [{1}]...", source, inst.getId());
                WfStepLongNameParts parts = WorkflowNameUtils.getWfStepLongNameParts(source);
                final Long workItemId = environment().value(Long.class, "id", new WfItemQuery().workRecId((Long) inst.getId())
                        .workflowName(parts.getWorkflow()).wfStepName(parts.getStepName()));
                if (workflow().applyUserAction((WorkEntity) inst, workItemId, parts.getStepName(),
                        userAction, WfReviewMode.NORMAL)) {
                    applied++;
                }                
            }
        }

        
        logDebug("Successfully applied user action [{0}] to [{1}] work items...", userAction, applied);
        return applied;
    }

}
