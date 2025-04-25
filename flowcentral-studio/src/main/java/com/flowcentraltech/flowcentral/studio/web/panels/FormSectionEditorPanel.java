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
import com.flowcentraltech.flowcentral.configuration.constants.FormColumnsType;
import com.flowcentraltech.flowcentral.studio.web.widgets.FormEditor;
import com.flowcentraltech.flowcentral.studio.web.widgets.FormEditor.FormSection;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.panel.AbstractDialogPanel;

/**
 * Form section editor panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-formsectioneditorpanel")
@UplBinding("web/studio/upl/formsectioneditorpanel.upl")
public class FormSectionEditorPanel extends AbstractDialogPanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        FormSection formSection = getValue(FormEditor.class).getEditSection();
        final boolean isCreate = DialogFormMode.CREATE.equals(DialogFormMode.fromName(formSection.getMode()));
        setVisible("addBtn", isCreate);
        setVisible("applyBtn", !isCreate);

        final boolean isSingleColumn = FormColumnsType.TYPE_1
                .equals(FormColumnsType.fromCode(formSection.getColumns()));
        setVisible("frmPanel", isSingleColumn);
        if (!isSingleColumn) {
            formSection.setPanel(null);
        }
    }

    @Action
    public void add() throws UnifyException {
        getValue(FormEditor.class).performSectionAdd();
        commandHidePopup();
    }

    @Action
    public void apply() throws UnifyException {
        commandHidePopup();
    }

    @Action
    public void columnChange() throws UnifyException {

    }

}
