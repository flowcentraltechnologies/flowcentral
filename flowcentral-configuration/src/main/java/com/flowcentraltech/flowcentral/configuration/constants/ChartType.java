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
 * Chart type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_CHARTTYPE")
@StaticList(name = "charttypelist", description = "$m{staticlist.charttypelist}")
public enum ChartType implements EnumConst {

    LINE(
            "LNE",
            "line"),
    AREA(
            "ARE",
            "area"),
    COLUMN(
            "COL",
            "bar"),
    BAR(
            "BAR",
            "bar"),
    RADAR(
            "RAD",
            "radar"),
    PIE(
            "PIE",
            "pie"),
    DONUT(
            "DNT",
            "donut"),
    POLAR_AREA(
            "POL",
            "polarArea");

    private final String code;

    private final String optionsType;

    private ChartType(String code, String optionsType) {
        this.code = code;
        this.optionsType = optionsType;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return LINE.code;
    }

    public String optionsType() {
        return optionsType;
    }

    public boolean plotOptions() {
        return COLUMN.equals(this) || BAR.equals(this) || PIE.equals(this) || DONUT.equals(this);
    }

    public boolean axisChart() {
        return !PIE.equals(this) && !DONUT.equals(this) && !POLAR_AREA.equals(this);
    }

    public static ChartType fromCode(String code) {
        return EnumUtils.fromCode(ChartType.class, code);
    }

    public static ChartType fromName(String name) {
        return EnumUtils.fromName(ChartType.class, name);
    }
}
