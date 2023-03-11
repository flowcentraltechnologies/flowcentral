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
package com.flowcentraltech.flowcentral.configuration.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.flowcentraltech.flowcentral.configuration.constants.FormColumnsType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.FormColumnsTypeXmlAdapter;
import com.tcdng.unify.core.util.xml.MarshalFalseToNullXmlAdapter;
import com.tcdng.unify.core.util.xml.MarshalTrueToNullXmlAdapter;

/**
 * Form section configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormSectionConfig {

    private String name;

    private String label;

    private FormColumnsType columns;

    private Boolean visible;

    private Boolean editable;

    private Boolean disabled;

    private List<FormFieldConfig> fieldList;

    public FormSectionConfig() {
        this.visible = Boolean.TRUE;
        this.editable = Boolean.TRUE;
        this.disabled = Boolean.FALSE;
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

    @XmlAttribute
    public void setLabel(String label) {
        this.label = label;
    }

    public FormColumnsType getColumns() {
        return columns;
    }

    @XmlJavaTypeAdapter(FormColumnsTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setColumns(FormColumnsType columns) {
        this.columns = columns;
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

    public List<FormFieldConfig> getFieldList() {
        return fieldList;
    }

    @XmlElement(name = "field", required = true)
    public void setFieldList(List<FormFieldConfig> fieldList) {
        this.fieldList = fieldList;
    }

}
