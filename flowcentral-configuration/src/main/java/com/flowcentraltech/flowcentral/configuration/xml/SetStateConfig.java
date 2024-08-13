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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.configuration.constants.FormElementType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.FormElementTypeXmlAdapter;
import com.tcdng.unify.core.constant.TriState;
import com.tcdng.unify.core.util.xml.adapter.TriStateXmlAdapter;

/**
 * Set state configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class SetStateConfig extends BaseConfig {

    @JsonSerialize(using = FormElementTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = FormElementTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private FormElementType type;

    @JacksonXmlProperty(isAttribute = true, localName = "target")
    private String target;

    @JsonSerialize(using = TriStateXmlAdapter.Serializer.class)
    @JsonDeserialize(using = TriStateXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private TriState required;

    @JsonSerialize(using = TriStateXmlAdapter.Serializer.class)
    @JsonDeserialize(using = TriStateXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private TriState visible;

    @JsonSerialize(using = TriStateXmlAdapter.Serializer.class)
    @JsonDeserialize(using = TriStateXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private TriState editable;

    @JsonSerialize(using = TriStateXmlAdapter.Serializer.class)
    @JsonDeserialize(using = TriStateXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private TriState disabled;

    public SetStateConfig() {
        required = TriState.CONFORMING;
        visible = TriState.CONFORMING;
        editable = TriState.CONFORMING;
        disabled = TriState.CONFORMING;
    }

    public FormElementType getType() {
        return type;
    }

    public void setType(FormElementType type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public TriState getRequired() {
        return required;
    }

    public void setRequired(TriState required) {
        this.required = required;
    }

    public TriState getVisible() {
        return visible;
    }

    public void setVisible(TriState visible) {
        this.visible = visible;
    }

    public TriState getEditable() {
        return editable;
    }

    public void setEditable(TriState editable) {
        this.editable = editable;
    }

    public TriState getDisabled() {
        return disabled;
    }

    public void setDisabled(TriState disabled) {
        this.disabled = disabled;
    }

}
