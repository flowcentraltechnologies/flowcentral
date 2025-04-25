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
package com.flowcentraltech.flowcentral.common.constants;

import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;

/**
 * Search input restriction data type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public enum SearchInputRestrictionDataType {

    BOOLEAN(
            EntityFieldDataType.BOOLEAN),
    INTEGER(
            EntityFieldDataType.INTEGER),
    LONG(
            EntityFieldDataType.LONG),
    DECIMAL(
            EntityFieldDataType.DECIMAL),
    DATE(
            EntityFieldDataType.DATE),
    STRING(
            EntityFieldDataType.STRING),
    ENUM(
            EntityFieldDataType.ENUM),
    ENUM_REF(
            EntityFieldDataType.ENUM_REF),
    ENUM_DYN(
            EntityFieldDataType.ENUM_DYN),
    REF(
            EntityFieldDataType.REF);

    private final EntityFieldDataType dataType;

    private SearchInputRestrictionDataType(EntityFieldDataType dataType) {
        this.dataType = dataType;
    }

    public EntityFieldDataType dataType() {
        return dataType;
    }

    public boolean isDate() {
        return dataType.isDate();
    }

    public boolean isBoolean() {
        return dataType.isBoolean();
    }

    public boolean isNumber() {
        return dataType.isNumber();
    }

    public boolean isDecimal() {
        return dataType.isDecimal();
    }

    public boolean isString() {
        return dataType.isString();
    }

    public boolean isEntityRef() {
        return dataType.isEntityRef();
    }

    public boolean isForeignKey() {
        return dataType.isForeignKey();
    }
}
