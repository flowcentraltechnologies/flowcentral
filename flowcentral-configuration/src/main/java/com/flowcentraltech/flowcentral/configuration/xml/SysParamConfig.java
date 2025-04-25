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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.configuration.constants.SysParamType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.SysParamTypeXmlAdapter;

/**
 * System parameter configuration
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class SysParamConfig extends BaseCodeConfig {

    @JsonSerialize(using = SysParamTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = SysParamTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private SysParamType type;

    @JacksonXmlProperty(isAttribute = true)
    private String editor;

    @JacksonXmlProperty(isAttribute = true)
    private String defaultVal;

    @JacksonXmlProperty(isAttribute = true, localName = "filter")
    private String filterName;

    @JacksonXmlProperty(isAttribute = true)
    private boolean control;

    @JacksonXmlProperty(isAttribute = true)
    private boolean editable;

    public SysParamConfig() {
        type = SysParamType.STRING;
    }

    public SysParamType getType() {
        return type;
    }

    public void setType(SysParamType type) {
        this.type = type;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public boolean isControl() {
        return control;
    }

    public void setControl(boolean control) {
        this.control = control;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

}
