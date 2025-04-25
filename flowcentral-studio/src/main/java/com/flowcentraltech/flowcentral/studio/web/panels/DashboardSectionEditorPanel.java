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

package com.flowcentraltech.flowcentral.studio.web.panels;

import com.flowcentraltech.flowcentral.common.constants.DialogFormMode;
import com.flowcentraltech.flowcentral.studio.web.widgets.DashboardEditor;
import com.flowcentraltech.flowcentral.studio.web.widgets.DashboardEditor.DDashboardSection;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.panel.AbstractDialogPanel;

/**
 * Dashboard section editor panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-dashboardsectioneditorpanel")
@UplBinding("web/studio/upl/dashboardsectioneditorpanel.upl")
public class DashboardSectionEditorPanel extends AbstractDialogPanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        DDashboardSection formSection = getValue(DashboardEditor.class).getEditSection();
        boolean isCreate = DialogFormMode.CREATE.equals(DialogFormMode.fromName(formSection.getMode()));
        setVisible("addBtn", isCreate);
        setVisible("applyBtn", !isCreate);
    }

    @Action
    public void add() throws UnifyException {
        getValue(DashboardEditor.class).performSectionAdd();
        commandHidePopup();
    }

    @Action
    public void apply() throws UnifyException {
        commandHidePopup();
    }

}
