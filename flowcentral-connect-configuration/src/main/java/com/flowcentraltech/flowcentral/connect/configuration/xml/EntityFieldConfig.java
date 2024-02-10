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
package com.flowcentraltech.flowcentral.connect.configuration.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.flowcentraltech.flowcentral.connect.configuration.constants.ConnectFieldDataType;
import com.flowcentraltech.flowcentral.connect.configuration.xml.adapter.FieldDataTypeXmlAdapter;

/**
 * Entity field configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityFieldConfig {

    private ConnectFieldDataType type;

    private String name;

    private String description;

    private String column;

    private String references;

    private String enumImplClass;

    private int scale;

    private int precision;

    private int length;

    private boolean nullable;

    public EntityFieldConfig() {
        this.nullable = true;
    }
    
    public ConnectFieldDataType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(FieldDataTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setType(ConnectFieldDataType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    @XmlAttribute(required = true)
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    @XmlAttribute
    public void setDescription(String description) {
        this.description = description;
    }

    public String getColumn() {
        return column;
    }

    @XmlAttribute
    public void setColumn(String column) {
        this.column = column;
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

    public int getScale() {
        return scale;
    }

    @XmlAttribute
    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getPrecision() {
        return precision;
    }

    @XmlAttribute
    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getLength() {
        return length;
    }

    @XmlAttribute
    public void setLength(int length) {
        this.length = length;
    }

    public boolean isNullable() {
        return nullable;
    }

    @XmlAttribute
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

}
