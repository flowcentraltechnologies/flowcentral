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

package com.flowcentraltech.flowcentral.studio.web.panels;

import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepType;
import com.flowcentraltech.flowcentral.studio.web.widgets.WorkflowEditor;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.ui.widget.panel.AbstractDialogPanel;

/**
 * Workflow step creation panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-wfstepcreationpanel")
@UplBinding("web/studio/upl/wfstepcreationpanel.upl")
public class WfStepCreationPanel extends AbstractDialogPanel {

    @Override
    public void onPageConstruct() throws UnifyException {
        super.onPageConstruct();
        setDisabled("frmType", true);
    }

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        WorkflowEditor workflowEditor = getValue(WorkflowEditor.class);
        WorkflowStepType type = workflowEditor.getEditStep() != null ? workflowEditor.getEditStep().getType() : null;
        boolean isPolicy = type != null ? type.isPolicy() : false;
        setVisible("frmPolicy", isPolicy);
        setVisible("frmRule", isPolicy);
        setVisible("frmBinaryConditionName", WorkflowStepType.BINARY_ROUTING.equals(type));
        setVisible("frmActionType", WorkflowStepType.RECORD_ACTION.equals(type));
        setVisible("frmAppletName", WorkflowStepType.USER_ACTION.equals(type));
        setVisible("frmReadOnlyConditionName", WorkflowStepType.USER_ACTION.equals(type));
    }

}
