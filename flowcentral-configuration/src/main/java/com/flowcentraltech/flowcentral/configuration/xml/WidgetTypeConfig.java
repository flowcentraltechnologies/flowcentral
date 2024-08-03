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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.configuration.constants.InputType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.InputTypeXmlAdapter;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.util.xml.adapter.DataTypeXmlAdapter;

/**
 * Widget type configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WidgetTypeConfig extends BaseNameConfig {

    @JsonSerialize(using = DataTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = DataTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private DataType dataType;

    @JsonSerialize(using = InputTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = InputTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private InputType inputType;

    @JacksonXmlProperty(isAttribute = true)
    private String editor;

    @JacksonXmlProperty(isAttribute = true)
    private String renderer;

    @JacksonXmlProperty(isAttribute = true)
    private boolean stretch;

    @JacksonXmlProperty(isAttribute = true)
    private boolean listOption;

    @JacksonXmlProperty(isAttribute = true)
    private boolean enumOption;

    public WidgetTypeConfig() {
        stretch = true;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public InputType getInputType() {
        return inputType;
    }

    public void setInputType(InputType inputType) {
        this.inputType = inputType;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public boolean isStretch() {
        return stretch;
    }

    public void setStretch(boolean stretch) {
        this.stretch = stretch;
    }

    public boolean isListOption() {
        return listOption;
    }

    public void setListOption(boolean listOption) {
        this.listOption = listOption;
    }

    public boolean isEnumOption() {
        return enumOption;
    }

    public void setEnumOption(boolean enumOption) {
        this.enumOption = enumOption;
    }

}
