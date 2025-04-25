/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.studio.web.lists;

import com.flowcentraltech.flowcentral.common.web.lists.AbstractEntityTypeListCommand;
import com.flowcentraltech.flowcentral.notification.senders.NotificationAlertSender;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.flowcentraltech.flowcentral.workflow.entities.WfStep;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.list.LongParam;

/**
 * Studio workflow step notification sender list command
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("studiowfstepnotifsenderlist")
public class StudioWfStepNotifSenderListCommand extends AbstractEntityTypeListCommand<NotificationAlertSender, LongParam> {

    @Configurable
    private WorkflowModuleService workflowModuleService;

    public StudioWfStepNotifSenderListCommand() {
        super(NotificationAlertSender.class, LongParam.class);
    }

    @Override
    protected String getEntityName(LongParam param) throws UnifyException {
        if (param.isPresent()) {
            WfStep wfStep = workflowModuleService.findLeanWorkflowStepById(param.getValue());
            return wfStep.getEntityName();
        }

        return null;
    }

}
