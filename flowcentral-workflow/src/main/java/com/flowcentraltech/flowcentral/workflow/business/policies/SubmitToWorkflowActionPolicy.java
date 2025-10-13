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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.common.business.policies.AbstractFormActionPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.flowcentraltech.flowcentral.workflow.entities.Workflow;
import com.flowcentraltech.flowcentral.workflow.entities.WorkflowQuery;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.common.database.WorkEntity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Submit to workflow action policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(name = "submittoworkflow-actionpolicy", description = "$m{workflow.entityactionpolicy.submittiworkflow}")
public class SubmitToWorkflowActionPolicy extends AbstractFormActionPolicy {

    @Configurable
    private WorkflowModuleService workflowModuleService;

    @Configurable
    private ApplicationModuleService applicationModuleService;

    @Override
    public boolean checkAppliesTo(ValueStoreReader reader) throws UnifyException {
        return true;
    }

    @Override
    public List<? extends Listable> getRuleList(String entityName) throws UnifyException {
        final List<Workflow> workflows = workflowModuleService.findWorkflows((WorkflowQuery) new WorkflowQuery()
                .entity(entityName).addSelect("applicationName", "name", "description"));
        if (!DataUtils.isBlank(workflows)) {
            List<ListData> result = new ArrayList<ListData>();
            for (Workflow workflow : workflows) {
                result.add(new ListData(
                        ApplicationNameUtils.ensureLongNameReference(workflow.getApplicationName(), workflow.getName()),
                        workflow.getDescription()));
            }

            return result;
        }

        return Collections.emptyList();
    }

    @Override
    protected EntityActionResult doExecutePreAction(EntityActionContext ctx) throws UnifyException {
        if (ctx.isWithActionRule()) {
            logDebug("Submitting work entity to workflow [{0}] ...", ctx.getActionRule());
            WorkEntity workEntity = (WorkEntity) ctx.getInst();
            workflowModuleService.submitToWorkflowByName(ctx.getActionRule(), workEntity);
            return null;
        }

        EntityActionResult entityActionResult = new EntityActionResult(ctx);
        entityActionResult.setSuccessHint("$m{workflow.workflow.submit.failed.hint}");
        return entityActionResult;
    }

    @Override
    protected EntityActionResult doExecutePostAction(EntityActionContext ctx) throws UnifyException {
        EntityActionResult entityActionResult = new EntityActionResult(ctx);
        entityActionResult.setSuccessHint("$m{workflow.workflow.submit.success.hint}");
        entityActionResult.setCloseView(true);
        return entityActionResult;
    }

}
