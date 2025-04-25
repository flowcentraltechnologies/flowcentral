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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.configuration.constants.WidgetColor;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.WidgetColorXmlAdapter;

/**
 * Form field configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class FormFieldConfig extends BaseConfig {

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String label;

    @JacksonXmlProperty(isAttribute = true)
    private String inputWidget;

    @JacksonXmlProperty(isAttribute = true)
    private String reference;

    @JacksonXmlProperty(isAttribute = true)
    private String previewForm;

    @JsonSerialize(using = WidgetColorXmlAdapter.Serializer.class)
    @JsonDeserialize(using = WidgetColorXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private WidgetColor color;
    
    @JacksonXmlProperty(isAttribute = true)
    private int column;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean switchOnChange;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean saveAs;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean required;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean visible;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean editable;

    @JacksonXmlProperty(isAttribute = true)
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

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public WidgetColor getColor() {
        return color;
    }

    public void setColor(WidgetColor color) {
        this.color = color;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public Boolean getSwitchOnChange() {
        return switchOnChange;
    }

    public void setSwitchOnChange(Boolean switchOnChange) {
        this.switchOnChange = switchOnChange;
    }

    public Boolean getSaveAs() {
        return saveAs;
    }

    public void setSaveAs(Boolean saveAs) {
        this.saveAs = saveAs;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
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

}
