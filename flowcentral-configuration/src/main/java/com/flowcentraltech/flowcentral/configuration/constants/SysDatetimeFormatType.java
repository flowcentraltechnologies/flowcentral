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

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * System Date-time Format Type enumeration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_SYSDATETIMEFORMATTYPE")
@StaticList(name = "sysdatetimeformattypelist", description = "$m{staticlist.sysdatetimeformattypelist}")
public enum SysDatetimeFormatType implements EnumConst {

    DD_MM_YYYY_SLASH(
            "DMYS",
            "dd/MM/yyyy HH:mm:ss"),
    MM_DD_YYYY_SLASH(
            "MDYS",
            "MM/dd/yyyy HH:mm:ss"),
    YYYY_MM_DD_SLASH(
            "YMDS",
            "yyyy/MM/dd HH:mm:ss"),
    MM_DD_YY_SLASH(
            "MDZS",
            "MM/dd/yy HH:mm:ss"),
    DD_MM_YY_SLASH(
            "DMZS",
            "dd/MM/yy HH:mm:ss"),
    DD_MM_YYYY(
            "DMY",
            "dd-MM-yyyy HH:mm:ss"),
    MM_DD_YYYY(
            "MDY",
            "MM-dd-yyyy HH:mm:ss"),
    YYYY_MM_DD(
            "YMD",
            "yyyy-MM-dd HH:mm:ss"),
    MM_DD_YY(
            "MDZ",
            "MM-dd-yy HH:mm:ss"),
    DD_MM_YY(
            "DMZ",
            "dd-MM-yy HH:mm:ss");

    private final String code;

    private final String format;

    private SysDatetimeFormatType(String code, String format) {
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

    public static SysDatetimeFormatType fromCode(String code) {
        return EnumUtils.fromCode(SysDatetimeFormatType.class, code);
    }

    public static SysDatetimeFormatType fromName(String name) {
        return EnumUtils.fromName(SysDatetimeFormatType.class, name);
    }

}
