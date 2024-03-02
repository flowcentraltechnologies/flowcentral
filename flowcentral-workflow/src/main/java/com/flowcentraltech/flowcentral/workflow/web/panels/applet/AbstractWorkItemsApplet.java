/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.data.WorkflowStepInfo;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.panels.applet.ManageLoadingListApplet;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.flowcentraltech.flowcentral.workflow.data.WfDef;
import com.flowcentraltech.flowcentral.workflow.data.WfStepDef;
import com.flowcentraltech.flowcentral.workflow.data.WfUserActionDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.ui.widget.data.ButtonInfo;

/**
 * Convenient abstract work items applet.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AbstractWorkItemsApplet extends ManageLoadingListApplet {

    public AbstractWorkItemsApplet(AppletUtilities au, String pathVariable, AppletWidgetReferences appletWidgetReferences,
            EntityFormEventHandlers formEventHandlers) throws UnifyException {
        super(au, pathVariable, appletWidgetReferences, formEventHandlers);
    }

    protected List<ButtonInfo> getActionButtons(WorkflowModuleService workflowModuleService,
            WorkflowStepInfo workflowStepInfo) throws UnifyException {
        WfDef wfDef = workflowModuleService.getWfDef(workflowStepInfo.getWorkflowLongName());
        List<ButtonInfo> actionBtnInfos = Collections.emptyList();
        if (wfDef.isSupportMultiItemAction()) {
            actionBtnInfos = new ArrayList<ButtonInfo>();
            WfStepDef wfStepDef = wfDef.getWfStepDef(workflowStepInfo.getStepName());
            for (WfUserActionDef wfUserActionDef : wfStepDef.getUserActions().values()) {
                actionBtnInfos.add(new ButtonInfo(wfUserActionDef.getName(), wfUserActionDef.getLabel()));
            }
        }

        return actionBtnInfos;
    }
}
