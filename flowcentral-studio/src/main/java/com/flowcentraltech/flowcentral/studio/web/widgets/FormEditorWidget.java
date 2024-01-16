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

package com.flowcentraltech.flowcentral.studio.web.widgets;

import com.flowcentraltech.flowcentral.common.constants.DialogFormMode;
import com.flowcentraltech.flowcentral.common.web.widgets.AbstractFlowCentralMultiControl;
import com.flowcentraltech.flowcentral.studio.web.widgets.FormEditor.Design;
import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.stream.JsonObjectStreamer;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.Control;

/**
 * Form editor widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-formeditor")
@UplAttributes({ @UplAttribute(name = "choiceWidth", type = String.class, defaultVal = "260px") })
public class FormEditorWidget extends AbstractFlowCentralMultiControl {

    public static final String WORK_CONTENT = "content";

    public static final String FIELDS_TAB = "fields";

    @Configurable(ApplicationComponents.APPLICATION_JSONOBJECTSTREAMER)
    private JsonObjectStreamer jsonObjectStreamer;

    private Control valueCtrl;

    private Control editTabCtrl;

    private Control editSectionCtrl;

    private Control editFieldCtrl;

    private Control editColCtrl;

    private Control editIndexCtrl;

    private Control editModeCtrl;

    private String design;

    private String tabName;

    private String sectionName;

    private String fieldName;

    private int column;

    private int index;

    private DialogFormMode editMode;

    @Override
    protected void doOnPageConstruct() throws UnifyException {
        valueCtrl = (Control) addInternalChildWidget("!ui-hidden binding:design");
        editTabCtrl = (Control) addInternalChildWidget("!ui-hidden binding:tabName");
        editSectionCtrl = (Control) addInternalChildWidget("!ui-hidden binding:sectionName");
        editFieldCtrl = (Control) addInternalChildWidget("!ui-hidden binding:fieldName");
        editColCtrl = (Control) addInternalChildWidget("!ui-hidden binding:column");
        editIndexCtrl = (Control) addInternalChildWidget("!ui-hidden binding:index");
        editModeCtrl = (Control) addInternalChildWidget("!ui-hidden binding:editMode");
    }

    @Override
    public void addPageAliases() throws UnifyException {
        addPageAlias(valueCtrl);
    }

    @Action
    public void update() throws UnifyException {

    }

    @Action
    public void editTab() throws UnifyException {
        if (editMode != null) {
            switch (editMode) {
                case CREATE:
                    commandShowPopup(getFormEditor().prepareTabAdd(tabName));
                    break;
                case CREATE_SUB:
                    commandShowPopup(getFormEditor().prepareSectionAdd(tabName, null));
                    break;
                case UPDATE:
                    commandShowPopup(getFormEditor().prepareTabEdit(tabName));
                    break;
                case DELETE:
                    commandRefreshPanels(getFormEditor().performTabDel(tabName));
                    break;
                default:
                    break;
            }
        }
    }

    @Action
    public void editSection() throws UnifyException {
        if (editMode != null) {
            switch (editMode) {
                case CREATE:
                    commandShowPopup(getFormEditor().prepareSectionAdd(tabName, sectionName));
                    break;
                case CREATE_SUB:
                    break;
                case UPDATE:
                    commandShowPopup(getFormEditor().prepareSectionEdit(tabName, sectionName));
                    break;
                case DELETE:
                    commandRefreshPanels(getFormEditor().performSectionDel(tabName, sectionName));
                    break;
                default:
                    break;
            }
        }
    }

    @Action
    public void editField() throws UnifyException {
        if (editMode != null) {
            switch (editMode) {
                case CREATE:
                    getFormEditor().prepareFieldAdd(tabName, sectionName, fieldName, column, index);
                    commandRefreshPanels(getFormEditor().performFieldAdd());
                    break;
                case CREATE_SUB:
                    break;
                case UPDATE:
                    commandShowPopup(getFormEditor().prepareFieldEdit(tabName, sectionName, fieldName));
                    break;
                case DELETE:
                    commandRefreshPanels(getFormEditor().performFieldDel(tabName, sectionName, fieldName));
                    break;
                default:
                    break;
            }
        }
    }

    public String getDesign() {
        return design;
    }

    public void setDesign(String design) throws UnifyException {
        if (!StringUtils.isBlank(design)) {
            getFormEditor().setDesign(jsonObjectStreamer.unmarshal(Design.class, design));
        }
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public DialogFormMode getEditMode() {
        return editMode;
    }

    public void setEditMode(DialogFormMode editMode) {
        this.editMode = editMode;
    }

    public Control getValueCtrl() {
        return valueCtrl;
    }

    public Control getEditTabCtrl() {
        return editTabCtrl;
    }

    public Control getEditSectionCtrl() {
        return editSectionCtrl;
    }

    public Control getEditFieldCtrl() {
        return editFieldCtrl;
    }

    public Control getEditColCtrl() {
        return editColCtrl;
    }

    public Control getEditIndexCtrl() {
        return editIndexCtrl;
    }

    public Control getEditModeCtrl() {
        return editModeCtrl;
    }

    public String getChoiceWidth() throws UnifyException {
        return getUplAttribute(String.class, "choiceWidth");
    }

    public FormEditor getFormEditor() throws UnifyException {
        return getValue(FormEditor.class);
    }

    public String getFieldBaseId() throws UnifyException {
        return getPrefixedId("field_base_");
    }

    public String getDesignBaseId() throws UnifyException {
        return getPrefixedId("design_base_");
    }

    public String getChoiceId() throws UnifyException {
        return getPrefixedId("choice_");
    }

}
