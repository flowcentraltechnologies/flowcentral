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
package com.flowcentraltech.flowcentral.connect.configuration.xml;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.connect.configuration.xml.adapter.FieldDataTypeXmlAdapter;
import com.tcdng.unify.common.constants.ConnectFieldDataType;

/**
 * Entity field configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EntityFieldConfig {

    @JsonSerialize(using = FieldDataTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = FieldDataTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private ConnectFieldDataType type;

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String description;

    @JacksonXmlProperty(isAttribute = true)
    private String column;

    @JacksonXmlProperty(isAttribute = true)
    private String references;

    @JacksonXmlProperty(isAttribute = true, localName = "enum-impl")
    private String enumImplClass;

    @JacksonXmlProperty(isAttribute = true)
    private int scale;

    @JacksonXmlProperty(isAttribute = true)
    private int precision;

    @JacksonXmlProperty(isAttribute = true)
    private int length;

    @JacksonXmlProperty(isAttribute = true)
    private boolean nullable;

    public EntityFieldConfig() {
        this.nullable = true;
    }
    
    public ConnectFieldDataType getType() {
        return type;
    }

    public void setType(ConnectFieldDataType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public String getEnumImplClass() {
        return enumImplClass;
    }

    public void setEnumImplClass(String enumImplClass) {
        this.enumImplClass = enumImplClass;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

}
