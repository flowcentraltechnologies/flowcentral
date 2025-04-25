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
package com.flowcentraltech.flowcentral.workflow.web.panels.applet;

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.data.WorkflowStepInfo;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractLoadingApplet;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.flowcentraltech.flowcentral.workflow.constants.WorkflowModuleNameConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * My work item applet.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class MyWorkItemApplet extends AbstractLoadingApplet {

    public MyWorkItemApplet(Page page, AppletUtilities au, List<String> pathVariables,
            AppletWidgetReferences appletWidgetReferences, EntityFormEventHandlers formEventHandlers)
            throws UnifyException {
        super(page, au, pathVariables, appletWidgetReferences, formEventHandlers,
                WorkflowModuleNameConstants.WORKFLOW_MY_WORKITEMS_LOADING_TABLE_PROVIDER);
    }

    @Override
    protected WorkflowStepInfo getWorkflowStepInfo(AppletUtilities au, Long sourceItemId) throws UnifyException {
        WorkflowModuleService workflow = au.getComponent(WorkflowModuleService.class);
        return workflow.getWorkflowLoadingStepInfoByWorkItemEventId(sourceItemId, null, null);
    }

}
