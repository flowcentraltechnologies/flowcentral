/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import com.tcdng.unify.convert.constants.EnumConst;
import com.tcdng.unify.core.annotation.StaticList;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Chart category data type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_CHARTCATDATATYPE")
@StaticList(name = "chartcategorydatatypelist", description = "$m{staticlist.chartcategorydatatypelist}")
public enum ChartCategoryDataType implements EnumConst {

    INTEGER(
            "INT",
            "numeric"),
    LONG(
            "LGN",
            "numeric"),
    DATE(
            "DTE",
            "datetime"),
    STRING(
            "STR",
            "category");

    private final String code;

    private final String optionsType;

    private ChartCategoryDataType(String code, String optionsType) {
        this.code = code;
        this.optionsType = optionsType;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return INTEGER.code;
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
