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
package com.flowcentraltech.flowcentral.configuration.xml;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.configuration.constants.TabContentType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.TabContentTypeXmlAdapter;

/**
 * Form tab configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class FormTabConfig extends BaseConfig {

    @JsonSerialize(using = TabContentTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = TabContentTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private TabContentType contentType;

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String label;

    @JacksonXmlProperty(isAttribute = true)
    private String applet;

    @JacksonXmlProperty(isAttribute = true)
    private String mappedFieldName;

    @JacksonXmlProperty(isAttribute = true)
    private String mappedForm;

    @JacksonXmlProperty(isAttribute = true)
    private String reference;

    @JacksonXmlProperty(isAttribute = true)
    private String filter;

    @JacksonXmlProperty(isAttribute = true)
    private String editAction;

    @JacksonXmlProperty(isAttribute = true)
    private String editFormless;

    @JacksonXmlProperty(isAttribute = true)
    private String editFixedRows;
    
    @JacksonXmlProperty(isAttribute = true)
    private String editAllowAddition;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean ignoreParentCondition;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean includeSysParam;
    
    @JacksonXmlProperty(isAttribute = true)
    private Boolean showSearch;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean quickEdit;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean quickOrder;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean visible;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean editable;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean disabled;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "section")
    private List<FormSectionConfig> sectionList;

    public FormTabConfig() {
        this.ignoreParentCondition = Boolean.FALSE;
        this.includeSysParam = Boolean.FALSE;
        this.showSearch = Boolean.FALSE;
        this.quickEdit = Boolean.FALSE;
        this.quickOrder = Boolean.FALSE;
        this.visible = Boolean.TRUE;
        this.editable = Boolean.TRUE;
        this.disabled = Boolean.FALSE;
    }

    public TabContentType getContentType() {
        return contentType;
    }

    public void setContentType(TabContentType contentType) {
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

    public String getEditAction() {
        return editAction;
    }

    public void setEditAction(String editAction) {
        this.editAction = editAction;
    }

    public String getEditFormless() {
        return editFormless;
    }

    public void setEditFormless(String editFormless) {
        this.editFormless = editFormless;
    }

    public String getEditFixedRows() {
        return editFixedRows;
    }

    public void setEditFixedRows(String editFixedRows) {
        this.editFixedRows = editFixedRows;
    }

    public String getEditAllowAddition() {
        return editAllowAddition;
    }

    public void setEditAllowAddition(String editAllowAddition) {
        this.editAllowAddition = editAllowAddition;
    }

    public Boolean getIgnoreParentCondition() {
        return ignoreParentCondition;
    }

    public void setIgnoreParentCondition(Boolean ignoreParentCondition) {
        this.ignoreParentCondition = ignoreParentCondition;
    }

    public Boolean getIncludeSysParam() {
        return includeSysParam;
    }

    public void setIncludeSysParam(Boolean includeSysParam) {
        this.includeSysParam = includeSysParam;
    }

    public Boolean getShowSearch() {
        return showSearch;
    }

    public void setShowSearch(Boolean showSearch) {
        this.showSearch = showSearch;
    }

    public Boolean getQuickEdit() {
        return quickEdit;
    }

    public void setQuickEdit(Boolean quickEdit) {
        this.quickEdit = quickEdit;
    }

    public Boolean getQuickOrder() {
        return quickOrder;
    }

    public void setQuickOrder(Boolean quickOrder) {
        this.quickOrder = quickOrder;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public List<FormSectionConfig> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<FormSectionConfig> sectionList) {
        this.sectionList = sectionList;
    }

}
