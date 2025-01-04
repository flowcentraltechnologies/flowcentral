/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * System Date Format Type enumeration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_SYSDATEFORMATTYPE")
@StaticList(name = "sysdateformattypelist", description = "$m{staticlist.sysdateformattypelist}")
public enum SysDateFormatType implements EnumConst {

    DD_MM_YYYY_SLASH(
            "DMYS",
            "dd/MM/yyyy"),
    MM_DD_YYYY_SLASH(
            "MDYS",
            "MM/dd/yyyy"),
    YYYY_MM_DD_SLASH(
            "YMDS",
            "yyyy/MM/dd"),
    MM_DD_YY_SLASH(
            "MDZS",
            "MM/dd/yy"),
    DD_MM_YY_SLASH(
            "DMZS",
            "dd/MM/yy"),
    DD_MM_YYYY(
            "DMY",
            "dd-MM-yyyy"),
    MM_DD_YYYY(
            "MDY",
            "MM-dd-yyyy"),
    YYYY_MM_DD(
            "YMD",
            "yyyy-MM-dd"),
    MM_DD_YY(
            "MDZ",
            "MM-dd-yy"),
    DD_MM_YY(
            "DMZ",
            "dd-MM-yy");

    private final String code;

    private final String format;

    private SysDateFormatType(String code, String format) {
        this.code = code;
        this.format = format;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return DD_MM_YYYY_SLASH.code;
    }

    public String format() {
        return format;
    }

    public static SysDateFormatType fromCode(String code) {
        return EnumUtils.fromCode(SysDateFormatType.class, code);
    }

    public static SysDateFormatType fromName(String name) {
        return EnumUtils.fromName(SysDateFormatType.class, name);
    }

}
