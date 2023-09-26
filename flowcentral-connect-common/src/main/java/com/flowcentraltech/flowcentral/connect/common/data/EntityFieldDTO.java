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
package com.flowcentraltech.flowcentral.connect.common.data;

import com.flowcentraltech.flowcentral.connect.configuration.constants.FieldDataType;

/**
 * Entity field DTO.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityFieldDTO {

    private FieldDataType type;

    private String name;

    private String description;

    private String column;

    private String references;

    private int scale;

    private int precision;

    private int length;

    public EntityFieldDTO(EntityFieldInfo entityFieldInfo) {
        this.type = entityFieldInfo.getType();
        this.name = entityFieldInfo.getName();
        this.description = entityFieldInfo.getDescription();
        this.column = entityFieldInfo.getColumn();
        this.references = entityFieldInfo.getReferences();
        this.scale = entityFieldInfo.getScale();
        this.precision = entityFieldInfo.getPrecision();
        this.length = entityFieldInfo.getLength();
    }

    public EntityFieldDTO() {

    }

    public FieldDataType getType() {
        return type;
    }

    public void setType(FieldDataType type) {
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

}