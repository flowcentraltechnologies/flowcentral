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

import com.flowcentraltech.flowcentral.connect.configuration.constants.ConnectFieldDataType;

/**
 * Field information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityFieldInfo {

    private ConnectFieldDataType type;

    private String name;

    private String description;

    private String column;

    private String references;

    private Class<? extends Enum<?>> enumImplClass;

    private int precision;

    private int scale;

    private int length;
    
    private boolean nullable;

    public EntityFieldInfo(ConnectFieldDataType type, String name, String description, String column, String references,
            Class<? extends Enum<?>> enumImplClass, int precision, int scale, int length, boolean nullable) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.column = column;
        this.references = references;
        this.enumImplClass = enumImplClass;
        this.precision = precision;
        this.scale = scale;
        this.length = length;
        this.nullable = nullable;
    }

    public ConnectFieldDataType getType() {
        return type;
    }
    
    public Class<?> getJavaClass() {
        return type.javaClass();
    }

    public String getName() {
        return name;
    }

    public String getReferences() {
        return references;
    }

    public Class<? extends Enum<?>> getEnumImplClass() {
        return enumImplClass;
    }

    public String getDescription() {
        return description;
    }

    public String getColumn() {
        return column;
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

    public boolean isNullable() {
        return nullable;
    }

    public boolean references() {
        return type.references();
    }
    
    public boolean isRef() {
        return type.isRef();
    }
    
    public boolean isEnum() {
        return type.isEnum();
    }
    
    public boolean isListOnly() {
        return type.isListOnly();
    }
    
    public boolean isChild() {
        return type.isChild();
    }
    
    public boolean isChildList() {
        return type.isChildList();
    }

    public boolean isDate() {
        return type.isDate();
    }

    public boolean isTimestamp() {
        return type.isTimestamp();
    }

    public boolean isBoolean() {
        return type.isBoolean();
    }

    public boolean isString() {
        return type.isString();
    }
    
    public boolean isInteger() {
        return type.isInteger();
    }
    
    public boolean isDouble() {
        return type.isDouble();
    }
    
    public boolean isDecimal() {
        return type.isDecimal();
    }

}
