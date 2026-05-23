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

import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.common.business.policies.AbstractFormActionPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.flowcentraltech.flowcentral.workflow.entities.Workflow;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Workflow publish action policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@EntityReferences({ "workflow.workflow" })
@Component(name = "wfpublish-actionpolicy", description = "$m{workflow.entityactionpolicy.publish}")
public class PublishWorkflowActionPolicy extends AbstractFormActionPolicy {

    @Configurable
    private WorkflowModuleService workflowModuleService;

    @Override
    public boolean checkAppliesTo(ValueStoreReader reader) throws UnifyException {
        return !((Workflow) reader.getValueObject()).isPublished()
                && !((Workflow) reader.getValueObject()).isRunnable();
    }

    @Override
    public String getConfirmation() throws UnifyException {
        return "$m{workflow.workflow.publish.confirm}";
    }

    @Override
    protected EntityActionResult doExecutePreAction(EntityActionContext ctx) throws UnifyException {
        Workflow workflow = (Workflow) ctx.getInst();
        logDebug("Executing publish workflow pre-action policy for workflow [{0}] ...", workflow.getDescription());
        final String workflowName = ApplicationNameUtils.getApplicationEntityLongName(workflow.getApplicationName(),
                workflow.getName());
        workflowModuleService.publishWorkflow(workflowName);
        workflow.setPublished(true);
        return null;
    }

    @Override
    protected EntityActionResult doExecutePostAction(EntityActionContext ctx) throws UnifyException {
        Workflow workflow = (Workflow) ctx.getInst();
        logDebug("Executing publish workflow post-action policy for workflow [{0}] ...", workflow.getDescription());
        EntityActionResult entityActionResult = new EntityActionResult(ctx);
        entityActionResult.setSuccessHint("$m{workflow.workflow.publish.success.hint}");
        return entityActionResult;
    }

}
