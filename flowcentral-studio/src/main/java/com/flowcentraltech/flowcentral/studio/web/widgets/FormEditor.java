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

package com.flowcentraltech.flowcentral.studio.web.widgets;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.FormColumnsType;
import com.flowcentraltech.flowcentral.configuration.constants.WidgetColor;
import com.tcdng.unify.core.UnifyException;

/**
 * Form editor object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class FormEditor {

    private final AppletUtilities au;

    private FormDef formDef;

    private Design design;

    private FormTab editTab;

    private FormSection editSection;

    private FormField editField;

    private String editorBodyPanelName;

    private String editTabPanelName;

    private String editSectionPanelName;

    private String editFieldPanelName;

    private String dialogTitle;

    private String originTabName;

    private String originSecName;

    private int originIndex;

    private boolean readOnly;

    private FormEditor(AppletUtilities au, FormDef formDef, Design design) {
        this.au = au;
        this.formDef = formDef;
        this.design = design;
    }

    public void init(String editorBodyPanelName, String editTabPanelName, String editSectionPanelName,
            String editFieldPanelName) {
        this.editorBodyPanelName = editorBodyPanelName;
        this.editTabPanelName = editTabPanelName;
        this.editSectionPanelName = editSectionPanelName;
        this.editFieldPanelName = editFieldPanelName;
    }

    public String getEditorBodyPanelName() {
        return editorBodyPanelName;
    }

    public boolean isInitialized() {
        return editorBodyPanelName != null;
    }

    public FormDef getFormDef() {
        return formDef;
    }

    public Design getDesign() {
        return design;
    }

    public void setDesign(Design design) {
        this.design = design;
    }

    public FormTab getEditTab() {
        return editTab;
    }

    public FormSection getEditSection() {
        return editSection;
    }

    public FormField getEditField() {
        return editField;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    // Tabs
    public void performTabAdd() throws UnifyException {
        design.addFormTab(originTabName, editTab);
    }

    public String prepareTabAdd(String tabName) throws UnifyException {
        dialogTitle = au.resolveSessionMessage("$m{formeditor.formtabeditorpanel.caption.add}");
        originTabName = tabName;
        editTab = new FormTab();
        editTab.setMode("CREATE");
        editTab.setEntity(formDef.getEntityDef().getLongName());
        editTab.setForm(formDef.getLongName());
        return editTabPanelName;
    }

    public String prepareTabEdit(String tabName) throws UnifyException {
        dialogTitle = au.resolveSessionMessage("$m{formeditor.formtabeditorpanel.caption.edit}");
        originTabName = tabName;
        editTab = design.getFormTab(tabName);
        editTab.setMode("UPDATE");
        editTab.setEntity(formDef.getEntityDef().getLongName());
        editTab.setForm(formDef.getLongName());
        return editTabPanelName;
    }

    public String performTabDel(String tabName) throws UnifyException {
        design.removeFormTab(tabName);
        return editorBodyPanelName;
    }

    // Sections
    public void performSectionAdd() throws UnifyException {
        FormTab formTab = design.getFormTab(originTabName);
        editSection.setEntity(formDef.getEntityDef().getLongName());
        formTab.addFormSection(originSecName, editSection);
    }

    public String prepareSectionAdd(String tabName, String sectionName) throws UnifyException {
        dialogTitle = au.resolveSessionMessage("$m{formeditor.formsectioneditorpanel.caption.add}");
        originTabName = tabName;
        originSecName = sectionName;
        editSection = new FormSection();
        editSection.setMode("CREATE");
        editSection.setEntity(formDef.getEntityDef().getLongName());
        return editSectionPanelName;
    }

    public String prepareSectionEdit(String tabName, String sectionName) throws UnifyException {
        dialogTitle = au.resolveSessionMessage("$m{formeditor.formsectioneditorpanel.caption.edit}");
        editSection = design.getFormSection(tabName, sectionName);
        editSection.setMode("UPDATE");
        editSection.setEntity(formDef.getEntityDef().getLongName());
        return editSectionPanelName;
    }

    public String performSectionDel(String tabName, String sectionName) throws UnifyException {
        design.removeFormSection(tabName, sectionName);
        return editorBodyPanelName;
    }

    // Fields
    public String performFieldAdd() throws UnifyException {
        FormSection formSection = design.getFormSection(originTabName, originSecName);
        formSection.addFormField(editField, originIndex);
        if (editField.getDataType() == null) {
            editField.setDataType(formDef.getEntityDef().getFieldDef(editField.getName()).getDataType().code());
        }
        return editorBodyPanelName;
    }

    public String prepareFieldAdd(String tabName, String sectionName, String fieldName, int column, int index)
            throws UnifyException {
        dialogTitle = au.resolveSessionMessage("$m{formeditor.formfieldeditorpanel.caption.add}");
        originTabName = tabName;
        originSecName = sectionName;
        originIndex = index;
        editField = new FormField();
        editField.setMode("CREATE");
        editField.setName(fieldName);
        editField.setColumn(column);
        editField.setLabel(formDef.getEntityDef().getFieldDef(fieldName).getFieldLabel());
        editField.setVisible(true);
        editField.setEditable(true);
        return editFieldPanelName;
    }

    public String prepareFieldEdit(String tabName, String sectionName, String fieldName) throws UnifyException {
        dialogTitle = au.resolveSessionMessage("$m{formeditor.formfieldeditorpanel.caption.edit}");
        editField = design.getFormField(tabName, sectionName, fieldName);
        editField.setMode("UPDATE");
        return editFieldPanelName;
    }

    public String performFieldDel(String tabName, String sectionName, String fieldName) throws UnifyException {
        design.removeFormField(tabName, sectionName, fieldName);
        return editorBodyPanelName;
    }

    public static Builder newBuilder(FormDef formDef) {
        return new Builder(formDef);
    }

    public static class Builder {

        private FormDef formDef;

        private List<FormTab> tabs;

        private FormTab currentTab;

        private FormSection currentSection;

        public Builder(FormDef formDef) {
            this.formDef = formDef;
            this.tabs = new ArrayList<FormTab>();
        }

        public Builder addTab(String contentType, String name, String label, String applet, String reference,
                String filter, String mappedFieldName, String mappedForm, String editAction, String editViewOnly,
                String editAllowAddition, String editFixedRows, boolean ignoreParentCondition, boolean includeSysParam,
                boolean showSearch, boolean quickEdit, boolean quickOrder, boolean visible, boolean editable,
                boolean disabled) {
            currentTab = new FormTab(contentType, name, label, applet, reference, filter, mappedFieldName, mappedForm,
                    editAction, editViewOnly, editAllowAddition, editFixedRows, ignoreParentCondition, includeSysParam,
                    showSearch, quickEdit, quickOrder, visible, editable, disabled);
            tabs.add(currentTab);
            return this;
        }

        public Builder addSection(String name, String label, FormColumnsType columns, String panel, String icon,
                boolean visible, boolean editable, boolean disabled) {
            currentSection = new FormSection(name, label, columns.code(), panel, icon, visible, editable, disabled);
            currentTab.getSections().add(currentSection);
            return this;
        }

        public Builder addField(String entity, EntityFieldDataType dataType, String name, String label,
                String inputWidget, String reference, String previewForm, WidgetColor widgetColor, int column,
                boolean switchOnChange, boolean saveAs, boolean required, boolean visible, boolean editable,
                boolean disabled) {
            String color = widgetColor != null ? widgetColor.code() : null;
            currentSection.getFields()
                    .add(new FormField(entity, dataType.code(), name, label,
                            formDef.getEntityDef().getFieldDef(name).getFieldLabel(), inputWidget, reference,
                            previewForm, color, column, switchOnChange, saveAs, required, visible, editable, disabled));
            return this;
        }

        public FormEditor build(AppletUtilities au) {
            return new FormEditor(au, formDef, new Design(tabs));
        }
    }

    public static class Design {

        private List<FormTab> tabs;

        public Design(List<FormTab> tabs) {
            this.tabs = tabs;
        }

        public Design() {

        }

        public List<FormTab> getTabs() {
            return tabs;
        }

        public void setTabs(List<FormTab> tabs) {
            this.tabs = tabs;
        }

        public void addFormTab(String originTabName, FormTab tab) {
            tabs.add(getTabIndex(originTabName) + 1, tab);
        }

        public FormTab getFormTab(String name) {
            int index = getTabIndex(name);
            if (index >= 0) {
                return tabs.get(index);
            }

            return null;
        }

        public FormSection getFormSection(String tabName, String sectionName) {
            FormTab tab = getFormTab(tabName);
            if (tab != null) {
                return tab.getFormSection(sectionName);
            }

            return null;
        }

        public FormField getFormField(String tabName, String sectionName, String fieldName) {
            FormSection section = getFormSection(tabName, sectionName);
            if (section != null) {
                return section.getFormField(fieldName);
            }

            return null;
        }

        public FormTab removeFormTab(String name) {
            int index = getTabIndex(name);
            if (index >= 0) {
                return tabs.remove(index);
            }

            return null;
        }

        public void removeFormSection(String tabName, String sectionName) {
            FormTab tab = getFormTab(tabName);
            if (tab != null) {
                tab.removeFormSection(sectionName);
            }
        }

        public void removeFormField(String tabName, String sectionName, String fieldName) {
            FormSection section = getFormSection(tabName, sectionName);
            if (section != null) {
                section.removeFormField(fieldName);
            }
        }

        private int getTabIndex(String name) {
            for (int i = 0; i < tabs.size(); i++) {
                if (tabs.get(i).getName().equals(name)) {
                    return i;
                }
            }

            return -1;
        }
    }

    public static class FormTab {

        private String entity;

        private String form;

        private String mode;

        private String caption;

        private String contentType;

        private String name;

        private String label;

        private String applet;

        private String reference;

        private String filter;

        private String mappedFieldName;

        private String mappedForm;

        private String editAction;

        private String editViewOnly;

        private String editAllowAddition;

        private String editFixedRows;

        private boolean ignoreParentCondition;

        private boolean includeSysParam;

        private boolean showSearch;

        private boolean quickEdit;

        private boolean quickOrder;

        private boolean visible;

        private boolean editable;

        private boolean disabled;

        private List<FormSection> sections;

        public FormTab(String contentType, String name, String label, String applet, String reference, String filter,
                String mappedFieldName, String mappedForm, String editAction, String editViewOnly,
                String editAllowAddition, String editFixedRows, boolean ignoreParentCondition, boolean includeSysParam,
                boolean showSearch, boolean quickEdit, boolean quickOrder, boolean visible, boolean editable,
                boolean disabled) {
            this();
            this.contentType = contentType;
            this.name = name;
            this.label = label;
            this.applet = applet;
            this.reference = reference;
            this.filter = filter;
            this.mappedFieldName = mappedFieldName;
            this.mappedForm = mappedForm;
            this.editAction = editAction;
            this.editViewOnly = editViewOnly;
            this.editAllowAddition = editAllowAddition;
            this.editFixedRows = editFixedRows;
            this.ignoreParentCondition = ignoreParentCondition;
            this.includeSysParam = includeSysParam;
            this.showSearch = showSearch;
            this.quickEdit = quickEdit;
            this.quickOrder = quickOrder;
            this.visible = visible;
            this.editable = editable;
            this.disabled = disabled;
        }

        public FormTab() {
            this.sections = new ArrayList<FormSection>();
        }

        public String getEntity() {
            return entity;
        }

        public void setEntity(String entity) {
            this.entity = entity;
        }

        public String getForm() {
            return form;
        }

        public void setForm(String form) {
            this.form = form;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getApplet() {
            return applet;
        }

        public void setApplet(String applet) {
            this.applet = applet;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getFilter() {
            return filter;
        }

        public void setFilter(String filter) {
            this.filter = filter;
        }

        public String getMappedFieldName() {
            return mappedFieldName;
        }

        public void setMappedFieldName(String mappedFieldName) {
            this.mappedFieldName = mappedFieldName;
        }

        public String getMappedForm() {
            return mappedForm;
        }

        public void setMappedForm(String mappedForm) {
            this.mappedForm = mappedForm;
        }

        public String getEditAction() {
            return editAction;
        }

        public void setEditAction(String editAction) {
            this.editAction = editAction;
        }

        public String getEditViewOnly() {
            return editViewOnly;
        }

        public void setEditViewOnly(String editViewOnly) {
            this.editViewOnly = editViewOnly;
        }

        public String getEditAllowAddition() {
            return editAllowAddition;
        }

        public void setEditAllowAddition(String editAllowAddition) {
            this.editAllowAddition = editAllowAddition;
        }

        public String getEditFixedRows() {
            return editFixedRows;
        }

        public void setEditFixedRows(String editFixedRows) {
            this.editFixedRows = editFixedRows;
        }

        public boolean isIgnoreParentCondition() {
            return ignoreParentCondition;
        }

        public void setIgnoreParentCondition(boolean ignoreParentCondition) {
            this.ignoreParentCondition = ignoreParentCondition;
        }

        public boolean isIncludeSysParam() {
            return includeSysParam;
        }

        public void setIncludeSysParam(boolean includeSysParam) {
            this.includeSysParam = includeSysParam;
        }

        public boolean isShowSearch() {
            return showSearch;
        }

        public void setShowSearch(boolean showSearch) {
            this.showSearch = showSearch;
        }

        public boolean isQuickEdit() {
            return quickEdit;
        }

        public void setQuickEdit(boolean quickEdit) {
            this.quickEdit = quickEdit;
        }

        public boolean isQuickOrder() {
            return quickOrder;
        }

        public void setQuickOrder(boolean quickOrder) {
            this.quickOrder = quickOrder;
        }

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        public boolean isEditable() {
            return editable;
        }

        public void setEditable(boolean editable) {
            this.editable = editable;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        public List<FormSection> getSections() {
            return sections;
        }

        public void setSections(List<FormSection> sections) {
            this.sections = sections;
        }

        public void addFormSection(String originSecName, FormSection editSection) {
            if (originSecName != null) {
                sections.add(editSection);
            } else {
                sections.add(getFormSectionIndex(originSecName) + 1, editSection);
            }

        }

        public FormSection getFormSection(String name) {
            int index = getFormSectionIndex(name);
            if (index >= 0) {
                return sections.get(index);
            }

            return null;
        }

        public void removeFormSection(String name) {
            int index = getFormSectionIndex(name);
            if (index >= 0) {
                sections.remove(index);
            }
        }

        private int getFormSectionIndex(String name) {
            for (int i = 0; i < sections.size(); i++) {
                if (sections.get(i).getName().equals(name)) {
                    return i;
                }
            }

            return -1;
        }
    }

    public static class FormSection {

        private String entity;

        private String mode;

        private String name;

        private String label;

        private String columns;

        private String panel;

        private String icon;

        private boolean visible;

        private boolean editable;

        private boolean disabled;

        private List<FormField> fields;

        public FormSection(String name, String label, String columns, String panel, String icon, boolean visible,
                boolean editable, boolean disabled) {
            this.name = name;
            this.label = label;
            this.columns = columns;
            this.panel = panel;
            this.icon = icon;
            this.visible = visible;
            this.editable = editable;
            this.disabled = disabled;
            this.fields = new ArrayList<FormField>();
        }

        public FormSection() {
            this.fields = new ArrayList<FormField>();
        }

        public String getEntity() {
            return entity;
        }

        public void setEntity(String entity) {
            this.entity = entity;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getColumns() {
            return columns;
        }

        public void setColumns(String columns) {
            FormColumnsType current = FormColumnsType.fromCode(this.columns);
            FormColumnsType target = FormColumnsType.fromCode(columns);
            int currentcol = current != null ? current.columns() : 0;
            if (currentcol > target.columns()) {
                int trim = target.columns() - 1;
                if (fields != null) {
                    for (FormField formField : fields) {
                        if (formField.getColumn() > trim) {
                            formField.setColumn(trim);
                        }
                    }
                }
            }

            this.columns = columns;
        }

        public String getPanel() {
            return panel;
        }

        public void setPanel(String panel) {
            this.panel = panel;
        }

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        public boolean isEditable() {
            return editable;
        }

        public void setEditable(boolean editable) {
            this.editable = editable;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        public List<FormField> getFields() {
            return fields;
        }

        public void setFields(List<FormField> fields) {
            this.fields = fields;
        }

        public void addFormField(FormField formField, int index) {
            int column = formField.getColumn();
            int i = 0;
            for (; i < fields.size(); i++) {
                if (fields.get(i).getColumn() == column) {
                    if (index <= 0) {
                        break;
                    } else {
                        index--;
                    }
                }
            }

            if (i >= fields.size()) {
                fields.add(formField);
            } else {
                fields.add(i, formField);
            }
        }

        public FormField getFormField(String name) {
            int index = getFormFieldIndex(name);
            if (index >= 0) {
                return fields.get(index);
            }

            return null;
        }

        public void removeFormField(String name) {
            int index = getFormFieldIndex(name);
            if (index >= 0) {
                fields.remove(index);
            }
        }

        private int getFormFieldIndex(String name) {
            for (int i = 0; i < fields.size(); i++) {
                if (fields.get(i).getName().equals(name)) {
                    return i;
                }
            }

            return -1;
        }
    }

    public static class FormField {

        private String entity;

        private String dataType;

        private String mode;

        private String name;

        private String label;

        private String fldLabel;

        private String inputWidget;

        private String reference;

        private String previewForm;

        private String color;

        private int column;

        private boolean switchOnChange;

        private boolean saveAs;

        private boolean required;

        private boolean visible;

        private boolean editable;

        private boolean disabled;

        public FormField(String entity, String dataType, String name, String label, String fldLabel, String inputWidget,
                String reference, String previewForm, String color, int column, boolean switchOnChange, boolean saveAs,
                boolean required, boolean visible, boolean editable, boolean disabled) {
            this.entity = entity;
            this.dataType = dataType;
            this.name = name;
            this.label = label;
            this.fldLabel = fldLabel;
            this.inputWidget = inputWidget;
            this.reference = reference;
            this.previewForm = previewForm;
            this.color = color;
            this.column = column;
            this.switchOnChange = switchOnChange;
            this.saveAs = saveAs;
            this.required = required;
            this.visible = visible;
            this.editable = editable;
            this.disabled = disabled;
        }

        public FormField() {

        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getEntity() {
            return entity;
        }

        public void setEntity(String entity) {
            this.entity = entity;
        }

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getFldLabel() {
            return fldLabel;
        }

        public void setFldLabel(String fldLabel) {
            this.fldLabel = fldLabel;
        }

        public String getInputWidget() {
            return inputWidget;
        }

        public void setInputWidget(String inputWidget) {
            this.inputWidget = inputWidget;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getPreviewForm() {
            return previewForm;
        }

        public void setPreviewForm(String previewForm) {
            this.previewForm = previewForm;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public int getColumn() {
            return column;
        }

        public void setColumn(int column) {
            this.column = column;
        }

        public boolean isSwitchOnChange() {
            return switchOnChange;
        }

        public void setSwitchOnChange(boolean switchOnChange) {
            this.switchOnChange = switchOnChange;
        }

        public boolean isSaveAs() {
            return saveAs;
        }

        public void setSaveAs(boolean saveAs) {
            this.saveAs = saveAs;
        }

        public boolean isRequired() {
            return required;
        }

        public void setRequired(boolean required) {
            this.required = required;
        }

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        public boolean isEditable() {
            return editable;
        }

        public void setEditable(boolean editable) {
            this.editable = editable;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }
    }
}
