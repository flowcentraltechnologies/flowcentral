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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.flowcentraltech.flowcentral.configuration.constants.SetValueType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.SetValueTypeXmlAdapter;

/**
 * Set value configuration configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SetValueConfig {

    private SetValueType type;

    private String fieldName;

    private String value;

    public SetValueConfig() {
        this.type = SetValueType.IMMEDIATE;
    }

    public SetValueType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(SetValueTypeXmlAdapter.class)
    @XmlAttribute(name = "type")
    public void setType(SetValueType type) {
        this.type = type;
    }

    public String getFieldName() {
        return fieldName;
    }

    @XmlAttribute(name = "field", required = true)
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getValue() {
        return value;
    }

    @XmlAttribute
    public void setValue(String value) {
        this.value = value;
    }

}
