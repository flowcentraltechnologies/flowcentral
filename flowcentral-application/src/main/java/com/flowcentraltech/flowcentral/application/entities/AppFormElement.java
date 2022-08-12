/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseConfigEntity;
import com.flowcentraltech.flowcentral.configuration.constants.FormColumnsType;
import com.flowcentraltech.flowcentral.configuration.constants.FormElementType;
import com.flowcentraltech.flowcentral.configuration.constants.TabContentType;
import com.flowcentraltech.flowcentral.configuration.constants.WidgetColor;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Application form element entity;
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_FORMELEMENT")
public class AppFormElement extends BaseConfigEntity {

    @ForeignKey(AppForm.class)
    private Long appFormId;

    @ForeignKey(name = "ELEMENT_TY")
    private FormElementType type;

    @ForeignKey(nullable = true)
    private TabContentType tabContentType;

    @ForeignKey(nullable = true)
    private WidgetColor color;

    @Column(name = "ELEMENT_LABEL", length = 128, nullable = true)
    private String label;

    @Column(name = "ELEMENT_NM", length = 64)
    private String elementName;

    @Column(length = 128, nullable = true)
    private String tabApplet;

    @Column(length = 128, nullable = true)
    private String tabReference;

    @Column(length = 64, nullable = true)
    private String filter;

    @Column(length = 64, nullable = true)
    private String editAction;

    @Column(length = 128, nullable = true)
    private String inputReference;

    @Column(length = 128, nullable = true)
    private String inputWidget;

    @Column(nullable = true)
    private FormColumnsType sectionColumns;

    @Column
    private int fieldColumn;

    @Column
    private boolean switchOnChange;

    @Column
    private boolean saveAs;

    @Column
    private boolean required;

    @Column
    private boolean showSearch;

    @Column
    private boolean visible;

    @Column
    private boolean editable;

    @Column
    private boolean disabled;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    @ListOnly(key = "tabContentType", property = "description")
    private String tabContentTypeDesc;

    @ListOnly(key = "color", property = "description")
    private String colorDesc;

    @Override
    public String getDescription() {
        return null;
    }

    public Long getAppFormId() {
        return appFormId;
    }

    public void setAppFormId(Long appFormId) {
        this.appFormId = appFormId;
    }

    public FormElementType getType() {
        return type;
    }

    public void setType(FormElementType type) {
        this.type = type;
    }

    public TabContentType getTabContentType() {
        return tabContentType;
    }

    public void setTabContentType(TabContentType tabContentType) {
        this.tabContentType = tabContentType;
    }

    public WidgetColor getColor() {
        return color;
    }

    public void setColor(WidgetColor color) {
        this.color = color;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTabApplet() {
        return tabApplet;
    }

    public void setTabApplet(String tabApplet) {
        this.tabApplet = tabApplet;
    }

    public String getTabReference() {
        return tabReference;
    }

    public void setTabReference(String tabReference) {
        this.tabReference = tabReference;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getEditAction() {
        return editAction;
    }

    public void setEditAction(String editAction) {
        this.editAction = editAction;
    }

    public String getInputReference() {
        return inputReference;
    }

    public void setInputReference(String inputReference) {
        this.inputReference = inputReference;
    }

    public String getInputWidget() {
        return inputWidget;
    }

    public void setInputWidget(String inputWidget) {
        this.inputWidget = inputWidget;
    }

    public FormColumnsType getSectionColumns() {
        return sectionColumns;
    }

    public void setSectionColumns(FormColumnsType sectionColumns) {
        this.sectionColumns = sectionColumns;
    }

    public int getFieldColumn() {
        return fieldColumn;
    }

    public void setFieldColumn(int fieldColumn) {
        this.fieldColumn = fieldColumn;
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

    public boolean isShowSearch() {
        return showSearch;
    }

    public void setShowSearch(boolean showSearch) {
        this.showSearch = showSearch;
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

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getTabContentTypeDesc() {
        return tabContentTypeDesc;
    }

    public void setTabContentTypeDesc(String tabContentTypeDesc) {
        this.tabContentTypeDesc = tabContentTypeDesc;
    }

    public String getColorDesc() {
        return colorDesc;
    }

    public void setColorDesc(String colorDesc) {
        this.colorDesc = colorDesc;
    }

}
