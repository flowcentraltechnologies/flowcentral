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

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * System parameter type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_SYSPARAMTYPE")
@StaticList(name = "sysparamtypelist", description = "$m{staticlist.sysparamtypelist}")
public enum SysParamType implements EnumConst {
    
    BOOLEAN(
            "B", Boolean.class),
    NUMBER(
            "N", Integer.class),
    STRING(
            "S", String.class),
    NAME(
            "M", String.class),
    CONTACT(
            "C", String.class);

    private static final String PREFIX = "sp:";

    private static final String PREFIX_UPPERCASE = PREFIX.toUpperCase();

    private final String code;

    private final Class<?> dataType;

    private SysParamType(String code, Class<?> dataType) {
        this.code = code;
        this.dataType = dataType;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return STRING.code;
    }

    public String encodeFilterCode(String param) {
        return PREFIX + param + ":" + code;
    }

    public String encodeFilterName(String name) {
        return PREFIX_UPPERCASE + name;
    }

    public Class<?> dataType() {
        return dataType;
    }
    
    public static boolean isSysParam(String encoded) {
        return encoded.startsWith(PREFIX);
    }
    
    public static String getSysParamCode(String encoded) {
        return encoded.substring(PREFIX.length(), encoded.length() - 2);
    }
    
    public static SysParamType fromEncoded(String encoded) {
        int index = encoded.lastIndexOf(':');
        return fromCode(encoded.substring(index + 1));
    }
    
    public static SysParamType fromCode(String code) {
        return EnumUtils.fromCode(SysParamType.class, code);
    }

    public static SysParamType fromName(String name) {
        return EnumUtils.fromName(SysParamType.class, name);
    }
}
