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

package com.flowcentraltech.flowcentral.studio.business.policies;

import java.util.List;

import com.flowcentraltech.flowcentral.application.data.AppletWorkflowCopyInfo;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.configuration.constants.ChannelDirectionType;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.flowcentraltech.flowcentral.workflow.constants.WfChannelStatus;
import com.flowcentraltech.flowcentral.workflow.entities.WfChannel;
import com.flowcentraltech.flowcentral.workflow.entities.WfStep;
import com.flowcentraltech.flowcentral.workflow.entities.Workflow;
import com.flowcentraltech.flowcentral.workflow.util.WorkflowDesignUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.NameUtils;

/**
 * Studio on create workflow policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("studiooncreateworkflow-policy")
public class StudioOnCreateWorkflowPolicy extends StudioOnCreateComponentPolicy {

    @Configurable
    private WorkflowModuleService workflowModuleService;

    @Override
    protected EntityActionResult doExecutePreAction(EntityActionContext ctx) throws UnifyException {
        super.doExecutePreAction(ctx);
        Workflow workflow = (Workflow) ctx.getInst();
        if (DataUtils.isBlank(workflow.getStepList())) {
            List<WfStep> stepList = workflowModuleService.generateWorkflowSteps(
                    WorkflowDesignUtils.DesignType.DEFAULT_WORKFLOW, workflow.getLabel(), new AppletWorkflowCopyInfo());
            workflow.setStepList(stepList);
        }

        return null;
    }

    @Override
    protected EntityActionResult doExecutePostAction(EntityActionContext ctx) throws UnifyException {
        EntityActionResult result = super.doExecutePostAction(ctx);
        Workflow workflow = (Workflow) ctx.getInst();
        final Long applicationId = workflow.getApplicationId();
        final String applicationName = application().getApplicationName(applicationId);

        WfChannel wfChannel = new WfChannel();
        wfChannel.setApplicationId(applicationId);
        wfChannel.setName(workflow.getName());
        final String description = NameUtils.describeName(workflow.getName()) + " Channel";
        wfChannel.setDescription(description);
        wfChannel.setLabel(description);
        wfChannel.setEntity(workflow.getEntity());
        wfChannel
                .setDestination(ApplicationNameUtils.getApplicationEntityLongName(applicationName, workflow.getName()));
        wfChannel.setDirection(ChannelDirectionType.INWARD);
        wfChannel.setStatus(WfChannelStatus.OPEN);

        workflowModuleService.createWorkflowChannel(wfChannel);
        return result;
    }

}
