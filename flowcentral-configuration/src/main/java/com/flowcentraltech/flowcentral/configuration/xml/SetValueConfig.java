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
import com.flowcentraltech.flowcentral.configuration.constants.SetValueType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.SetValueTypeXmlAdapter;

/**
 * Set value configuration configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class SetValueConfig extends BaseConfig {

    @JsonSerialize(using = SetValueTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = SetValueTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "type")
    private SetValueType type;

    @JacksonXmlProperty(isAttribute = true, localName = "field")
    private String fieldName;

    @JacksonXmlProperty(isAttribute = true)
    private String value;

    public SetValueConfig() {
        this.type = SetValueType.IMMEDIATE;
    }

    public SetValueType getType() {
        return type;
    }

    public void setType(SetValueType type) {
        this.type = type;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
