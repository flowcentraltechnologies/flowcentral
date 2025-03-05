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
 * System parameter type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_SYSPARAMTYPE")
@StaticList(name = "sysparamtypelist", description = "$m{staticlist.sysparamtypelist}")
public enum SysParamType implements EnumConst {

    BOOLEAN(
            "B"),
    NUMBER(
            "N"),
    STRING(
            "S"),
    NAME(
            "M"),
    CONTACT(
            "C");

    private final String code;

    private SysParamType(String code) {
        this.code = code;
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
        return "sp:" + param + ":" + code;
    }

    public String encodeFilterName(String name) {
        return "SP:" + name;
    }
    
    public static boolean isSysParam(String encoded) {
        return encoded.startsWith("sp:");
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
