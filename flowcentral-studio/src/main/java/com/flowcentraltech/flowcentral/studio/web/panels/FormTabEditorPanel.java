/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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
import com.flowcentraltech.flowcentral.configuration.constants.TabContentType;
import com.flowcentraltech.flowcentral.studio.web.widgets.FormEditor;
import com.flowcentraltech.flowcentral.studio.web.widgets.FormEditor.FormTab;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.panel.AbstractDialogPanel;

/**
 * Form tab editor panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-formtabeditorpanel")
@UplBinding("web/studio/upl/formtabeditorpanel.upl")
public class FormTabEditorPanel extends AbstractDialogPanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        FormTab formTab = getValue(FormEditor.class).getEditTab();
        boolean isCreate = DialogFormMode.CREATE.equals(DialogFormMode.fromName(formTab.getMode()));
        setVisible("addBtn", isCreate);
        setVisible("applyBtn", !isCreate);

        TabContentType type = TabContentType.fromName(formTab.getContentType());
        boolean referenceVisible = false;
        boolean appletVisible = false;
        boolean editActionVisible = false;
        boolean mapped = false;
        if (type != null) {
            switch (type) {
                case CHILD:
                case CHILD_LIST:
                    referenceVisible = true;
                    appletVisible = true;
                    editActionVisible = true;
                    break;
                case FIELD_SEQUENCE:
                case SEQUENCE:
                    break;
                case FILTER_CONDITION:
                    break;
                case SEARCH_INPUT:
                    break;
                case MINIFORM:
                    break;
                case MINIFORM_CHANGELOG:
                    break;
                case MINIFORM_MAPPED:
                    mapped = true;
                    formTab.setEditable(false);
                    formTab.setDisabled(true);
                    setDisabled("frmEditable", true);
                    setDisabled("frmDisabled", true);
                    break;
                case PARAM_VALUES:
                    break;
                case PROPERTY_LIST:
                    break;
                case USAGE_LIST:
                    break;
                case SET_VALUES:
                    break;
                case WIDGET_RULES:
                    break;
                default:
                    break;
            }
        }

        setVisible("frmReference", referenceVisible);
        setVisible("frmApplet", appletVisible);
        setVisible("frmEditAction", editActionVisible);
        setVisible("frmEditViewOnly", editActionVisible);
        setVisible("frmEditAllowAddition", editActionVisible);
        setVisible("frmEditFixedRows", editActionVisible);

        setVisible("frmMappedField", mapped);
        setVisible("frmMappedForm", mapped);
   }

    @Action
    public void add() throws UnifyException {
        getValue(FormEditor.class).performTabAdd();
        commandHidePopup();
    }

    @Action
    public void apply() throws UnifyException {
        commandHidePopup();
    }
}
