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
package com.flowcentraltech.flowcentral.configuration.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.annotation.Table;
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
            "1", 1),
    TYPE_1_1(
            "1-1", 2),
    TYPE_1_2(
            "1-2", 2),
    TYPE_2_1(
            "2-1", 2),
    TYPE_1_1_1(
            "1-1-1", 3),
    TYPE_1_3(
            "1-3", 2),
    TYPE_1_1_2(
            "1-1-2", 3),
    TYPE_1_2_1(
            "1-2-1", 3),
    TYPE_2_1_1(
            "2-1-1", 3),
    TYPE_3_1(
            "3-1", 2),
    TYPE_1_1_1_1(
            "1-1-1-1", 4);

    private final String code;

    private final int columns;

    private DashboardColumnsType(String code, int columns) {
        this.code = code;
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
