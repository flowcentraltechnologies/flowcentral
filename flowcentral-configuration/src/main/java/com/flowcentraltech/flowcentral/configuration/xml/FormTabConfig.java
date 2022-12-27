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
package com.flowcentraltech.flowcentral.configuration.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.flowcentraltech.flowcentral.configuration.constants.TabContentType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.TabContentTypeXmlAdapter;
import com.tcdng.unify.core.util.xml.MarshalFalseToNullXmlAdapter;
import com.tcdng.unify.core.util.xml.MarshalTrueToNullXmlAdapter;

/**
 * Form tab configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormTabConfig {

    private TabContentType contentType;

    private String name;

    private String label;

    private String applet;

    private String mappedFieldName;

    private String mappedForm;

    private String reference;

    private String filter;

    private String editAction;

    private String editFormless;

    private String editFixedRows;

    private Boolean ignoreParentCondition;

    private Boolean showSearch;

    private Boolean quickEdit;

    private Boolean visible;

    private Boolean editable;

    private Boolean disabled;

    private List<FormSectionConfig> sectionList;

    public FormTabConfig() {
        this.ignoreParentCondition = Boolean.FALSE;
        this.showSearch = Boolean.FALSE;
        this.quickEdit = Boolean.FALSE;
        this.visible = Boolean.TRUE;
        this.editable = Boolean.TRUE;
        this.disabled = Boolean.FALSE;
    }

    public TabContentType getContentType() {
        return contentType;
    }

    @XmlJavaTypeAdapter(TabContentTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setContentType(TabContentType contentType) {
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    @XmlAttribute(required = true)
    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    @XmlAttribute(required = true)
    public void setLabel(String label) {
        this.label = label;
    }

    public String getApplet() {
        return applet;
    }

    @XmlAttribute
    public void setApplet(String applet) {
        this.applet = applet;
    }

    public String getMappedFieldName() {
        return mappedFieldName;
    }

    @XmlAttribute
    public void setMappedFieldName(String mappedFieldName) {
        this.mappedFieldName = mappedFieldName;
    }

    public String getMappedForm() {
        return mappedForm;
    }

    @XmlAttribute
    public void setMappedForm(String mappedForm) {
        this.mappedForm = mappedForm;
    }

    public String getReference() {
        return reference;
    }

    @XmlAttribute
    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getFilter() {
        return filter;
    }

    @XmlAttribute
    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getEditAction() {
        return editAction;
    }

    @XmlAttribute
    public void setEditAction(String editAction) {
        this.editAction = editAction;
    }

    public String getEditFormless() {
        return editFormless;
    }

    @XmlAttribute
    public void setEditFormless(String editFormless) {
        this.editFormless = editFormless;
    }

    public String getEditFixedRows() {
        return editFixedRows;
    }

    @XmlAttribute
    public void setEditFixedRows(String editFixedRows) {
        this.editFixedRows = editFixedRows;
    }

    public Boolean getIgnoreParentCondition() {
        return ignoreParentCondition;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setIgnoreParentCondition(Boolean ignoreParentCondition) {
        this.ignoreParentCondition = ignoreParentCondition;
    }

    public Boolean getShowSearch() {
        return showSearch;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setShowSearch(Boolean showSearch) {
        this.showSearch = showSearch;
    }

    public Boolean getQuickEdit() {
        return quickEdit;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setQuickEdit(Boolean quickEdit) {
        this.quickEdit = quickEdit;
    }

    public Boolean getVisible() {
        return visible;
    }

    @XmlJavaTypeAdapter(MarshalTrueToNullXmlAdapter.class)
    @XmlAttribute
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getEditable() {
        return editable;
    }

    @XmlJavaTypeAdapter(MarshalTrueToNullXmlAdapter.class)
    @XmlAttribute
    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public List<FormSectionConfig> getSectionList() {
        return sectionList;
    }

    @XmlElement(name = "section", required = true)
    public void setSectionList(List<FormSectionConfig> sectionList) {
        this.sectionList = sectionList;
    }

}
