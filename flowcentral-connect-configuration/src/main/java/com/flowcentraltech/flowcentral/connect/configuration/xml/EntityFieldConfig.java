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
package com.flowcentraltech.flowcentral.connect.configuration.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.flowcentraltech.flowcentral.connect.configuration.constants.FieldDataType;
import com.flowcentraltech.flowcentral.connect.configuration.xml.adapter.FieldDataTypeXmlAdapter;

/**
 * Entity field configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityFieldConfig {

    private FieldDataType type;

    private String name;

    private String references;

    private String enumImplClass;

    public FieldDataType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(FieldDataTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setType(FieldDataType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    @XmlAttribute(required = true)
    public void setName(String name) {
        this.name = name;
    }

    public String getReferences() {
        return references;
    }

    @XmlAttribute
    public void setReferences(String references) {
        this.references = references;
    }

    public String getEnumImplClass() {
        return enumImplClass;
    }

    @XmlAttribute(name = "enum-impl")
    public void setEnumImplClass(String enumImplClass) {
        this.enumImplClass = enumImplClass;
    }

}
