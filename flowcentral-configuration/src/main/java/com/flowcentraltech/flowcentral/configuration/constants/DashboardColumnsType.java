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
package com.flowcentraltech.flowcentral.configuration.constants;

import java.util.Arrays;
import java.util.List;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Dashboard columns type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_DASHBOARDCOLUMNSTYPE")
@StaticList(name = "dashboardcolumnstypelist", description = "$m{staticlist.dashboardcolumnstypelist}")
public enum DashboardColumnsType implements EnumConst {

    TYPE_1(
            "1", "100%", 1),
    TYPE_1_1(
            "1_1", "50% 50%", 2),
    TYPE_1_2(
            "1_2", "33% 67%", 2),
    TYPE_2_1(
            "2_1", "67% 33%", 2),
    TYPE_1_1_1(
            "1_1_1", "33% 34% 33%", 3),
    TYPE_1_3(
            "1_3", "25% 75%", 2),
    TYPE_1_1_2(
            "1_1_2", "25% 25% 50%", 3),
    TYPE_1_2_1(
            "1_2_1", "25% 50% 25%", 3),
    TYPE_2_1_1(
            "2_1_1", "50% 25% 25%", 3),
    TYPE_3_1(
            "3_1", "75% 25%", 2),
    TYPE_1_1_1_1(
            "1_1_1_1", "25% 25% 25% 25%", 4),
    TYPE_1_1_1_1_1(
            "1_1_1_1_1", "20% 20% 20% 20% 20%", 5),
    TYPE_1_1_1_1_1_1(
            "1_1_1_1_1_1", "16% 17% 17% 17% 17% 16%", 6);

    private final String code;

    private final List<String> dimension;

    private final int columns;

    private DashboardColumnsType(String code, String dimension, int columns) {
        this.code = code;
        this.dimension = Arrays.asList(dimension.split("\\s+"));
        this.columns = columns;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return TYPE_1.code;
    }

    public List<String> dimension() {
        return this.dimension;
    }

   public int columns() {
        return columns;
    }

    public static DashboardColumnsType fromCode(String code) {
        return EnumUtils.fromCode(DashboardColumnsType.class, code);
    }

    public static DashboardColumnsType fromName(String name) {
        return EnumUtils.fromName(DashboardColumnsType.class, name);
    }
}

