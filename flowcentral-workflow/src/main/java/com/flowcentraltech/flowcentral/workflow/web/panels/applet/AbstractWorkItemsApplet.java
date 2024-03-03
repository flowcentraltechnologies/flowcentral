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
import com.flowcentraltech.flowcentral.workflow.constants.WorkflowModuleNameConstants;
import com.flowcentraltech.flowcentral.workflow.data.WfDef;
import com.flowcentraltech.flowcentral.workflow.data.WfStepDef;
import com.flowcentraltech.flowcentral.workflow.data.WfUserActionDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.util.DataUtils;
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
    
    protected List<TableAction> getTableActions(List<ButtonInfo> actionBtnInfos) throws UnifyException {
        if (!DataUtils.isBlank(actionBtnInfos)) {
            List<TableAction> actions = new ArrayList<TableAction>();
            for (ButtonInfo buttonInfo : actionBtnInfos) {
                actions.add(new TableAction(WorkflowModuleNameConstants.WORKFLOW_WORKITEMS_TABLE_ACTION_POLICY + ":"
                        + buttonInfo.getValue(), buttonInfo.getLabel()));
            }

            return actions;
        }

        return Collections.emptyList();
    }
    
    public class TableAction implements Listable {
        
        private String policy;
        
        private String label;

        public TableAction(String policy, String label) {
            this.policy = policy;
            this.label = label;
        }

        @Override
        public String getListKey() {
            return policy;
        }

        @Override
        public String getListDescription() {
            return label;
        }

        public String getPolicy() {
            return policy;
        }

        public String getLabel() {
            return label;
        }        
    }
}
