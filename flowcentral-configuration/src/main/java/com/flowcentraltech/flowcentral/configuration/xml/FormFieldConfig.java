/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.flowcentraltech.flowcentral.configuration.constants.WidgetColor;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.WidgetColorXmlAdapter;
import com.tcdng.unify.core.util.xml.MarshalFalseToNullXmlAdapter;
import com.tcdng.unify.core.util.xml.MarshalTrueToNullXmlAdapter;

/**
 * Form field configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormFieldConfig extends BaseConfig {

    private String name;

    private String label;

    private String inputWidget;

    private String reference;

    private WidgetColor color;
    
    private int column;

    private Boolean switchOnChange;

    private Boolean saveAs;

    private Boolean required;

    private Boolean visible;

    private Boolean editable;

    private Boolean disabled;

    public FormFieldConfig() {
        this.switchOnChange = Boolean.FALSE;
        this.saveAs = Boolean.FALSE;
        this.required = Boolean.FALSE;
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

    public String getInputWidget() {
        return inputWidget;
    }

    @XmlAttribute(required = true)
    public void setInputWidget(String inputWidget) {
        this.inputWidget = inputWidget;
    }

    public String getReference() {
        return reference;
    }

    @XmlAttribute
    public void setReference(String reference) {
        this.reference = reference;
    }

    public WidgetColor getColor() {
        return color;
    }

    @XmlJavaTypeAdapter(WidgetColorXmlAdapter.class)
    @XmlAttribute
    public void setColor(WidgetColor color) {
        this.color = color;
    }

    public int getColumn() {
        return column;
    }

    @XmlAttribute(required = true)
    public void setColumn(int column) {
        this.column = column;
    }

    public Boolean getSwitchOnChange() {
        return switchOnChange;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setSwitchOnChange(Boolean switchOnChange) {
        this.switchOnChange = switchOnChange;
    }

    public Boolean getSaveAs() {
        return saveAs;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setSaveAs(Boolean saveAs) {
        this.saveAs = saveAs;
    }

    public Boolean getRequired() {
        return required;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setRequired(Boolean required) {
        this.required = required;
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

}
