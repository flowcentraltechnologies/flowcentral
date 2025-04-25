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
package com.flowcentraltech.flowcentral.configuration.xml;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.configuration.constants.FormColumnsType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.FormColumnsTypeXmlAdapter;

/**
 * Form section configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class FormSectionConfig extends BaseConfig {

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String label;

    @JacksonXmlProperty(isAttribute = true)
    private String panel;

    @JacksonXmlProperty(isAttribute = true)
    private String icon;

    @JsonSerialize(using = FormColumnsTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = FormColumnsTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private FormColumnsType columns;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean visible;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean editable;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean disabled;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "field")
    private List<FormFieldConfig> fieldList;

    public FormSectionConfig() {
        this.visible = Boolean.TRUE;
        this.editable = Boolean.TRUE;
        this.disabled = Boolean.FALSE;
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

    public String getPanel() {
        return panel;
    }

    public void setPanel(String panel) {
        this.panel = panel;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public FormColumnsType getColumns() {
        return columns;
    }

    public void setColumns(FormColumnsType columns) {
        this.columns = columns;
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

    public List<FormFieldConfig> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<FormFieldConfig> fieldList) {
        this.fieldList = fieldList;
    }

}
