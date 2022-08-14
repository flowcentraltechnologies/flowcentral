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

    private String reference;

    private String filter;

    private String editAction;

    private String editFormless;

    private String editFixedRows;
    
    private boolean ignoreParentCondition;

    private boolean showSearch;

    private boolean visible;

    private boolean editable;

    private boolean disabled;

    private List<FormSectionConfig> sectionList;

    public FormTabConfig() {
        this.visible = true;
        this.editable = true;
        this.disabled = false;
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

    public boolean isIgnoreParentCondition() {
        return ignoreParentCondition;
    }

    @XmlAttribute
    public void setIgnoreParentCondition(boolean ignoreParentCondition) {
        this.ignoreParentCondition = ignoreParentCondition;
    }

    public boolean isShowSearch() {
        return showSearch;
    }

    @XmlAttribute
    public void setShowSearch(boolean showSearch) {
        this.showSearch = showSearch;
    }

    public boolean isVisible() {
        return visible;
    }

    @XmlAttribute
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isEditable() {
        return editable;
    }

    @XmlAttribute
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isDisabled() {
        return disabled;
    }

    @XmlAttribute
    public void setDisabled(boolean disabled) {
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
