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
package com.flowcentraltech.flowcentral.application.data;

import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;

/**
 * Entity field schema.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityFieldSchema {

    private EntityFieldDataType dataType;

    private String name;

    private String description;

    private String column;

    private String references;

    private int scale;

    private int precision;

    private int length;

    public EntityFieldSchema(EntityFieldDataType dataType, String name, String description, String column,
            String references, int scale, int precision, int length) {
        this.name = name;
        this.description = description;
        this.column = column;
        this.references = references;
        this.scale = scale;
        this.precision = precision;
        this.length = length;
    }

    public EntityFieldDataType getDataType() {
        return dataType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getColumn() {
        return column;
    }

    public String getReferences() {
        return references;
    }

    public int getScale() {
        return scale;
    }

    public int getPrecision() {
        return precision;
    }

    public int getLength() {
        return length;
    }
}
