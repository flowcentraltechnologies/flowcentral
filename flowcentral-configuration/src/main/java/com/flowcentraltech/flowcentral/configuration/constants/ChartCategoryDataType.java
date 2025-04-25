/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.configuration.constants;

import java.util.Date;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Chart category data type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_CHARTCATDATATYPE")
@StaticList(name = "chartcategorydatatypelist", description = "$m{staticlist.chartcategorydatatypelist}")
public enum ChartCategoryDataType implements EnumConst {

    INTEGER(
            "INT",
            "numeric",
            Integer.class),
    LONG(
            "LGN",
            "numeric",
            Integer.class),
    DATE(
            "DTE",
            "datetime",
            Date.class),
    STRING(
            "STR",
            "category",
            String.class);

    private final String code;

    private final String optionsType;

    private final Class<?> dataType;

    private ChartCategoryDataType(String code, String optionsType, Class<?> dataType) {
        this.code = code;
        this.optionsType = optionsType;
        this.dataType = dataType;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return INTEGER.code;
    }

    public boolean isString() {
        return STRING.equals(this);
    }

    public boolean isDate() {
        return DATE.equals(this);
    }
    
    public Class<?> dataType() {
        return dataType;
    }

    public String optionsType() {
        return optionsType;
    }

    public static ChartCategoryDataType fromCode(String code) {
        return EnumUtils.fromCode(ChartCategoryDataType.class, code);
    }

    public static ChartCategoryDataType fromName(String name) {
        return EnumUtils.fromName(ChartCategoryDataType.class, name);
    }
}
