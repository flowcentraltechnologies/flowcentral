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

import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.flowcentraltech.flowcentral.messaging.os.data.UserAction;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * Convenient abstract base class for work-item external accessibility providers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractWorkItemExternalAccessibilityProvider extends AbstractFlowCentralComponent
        implements WorkItemExternalAccessibilityProvider {

    @Configurable
    private WorkflowModuleService workflowModuleService;

    @Override
    public boolean releaseFromExternalWithUserAction(Long workRecId, String workflowName, String stepName,
            List<? extends UserAction> actions) throws UnifyException {
        // For now just do single action
        final UserAction action = actions.get(0);
        return workflowModuleService.applyUserAction(workRecId, workflowName, stepName, action.getActionName(),
                action.getActionDate(), action.getActionBy());
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

}
