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

package com.flowcentraltech.flowcentral.application.data;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Usage type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_USAGETYPE")
@StaticList(name = "usagetypelist", description = "$m{staticlist.usagetypelist}")
public enum UsageType implements EnumConst {

    APPLET("APL"),
    ENTITY("ENT"),
    FORM("FRM"),
    REF("REF"),
    TABLE("TBL");

    private final String code;

    private UsageType(String code) {
        this.code = code;
     }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return ENTITY.code;
    }

    public boolean isEntity() {
        return ENTITY.equals(this);
    }

    public boolean isApplet() {
        return APPLET.equals(this);
    }

    public boolean isForm() {
        return FORM.equals(this);
    }

    public boolean isRef() {
        return REF.equals(this);
    }

    public boolean isTable() {
        return TABLE.equals(this);
    }

    public static boolean isQualifiesEntity(UsageType type) {
        return type == null || ENTITY.equals(type);
    }

    public static boolean isQualifiesApplet(UsageType type) {
        return type == null || APPLET.equals(type);
    }

    public static boolean isQualifiesForm(UsageType type) {
        return type == null || FORM.equals(type);
    }

    public static boolean isQualifiesRef(UsageType type) {
        return type == null || REF.equals(type);
    }

    public static boolean isQualifiesTable(UsageType type) {
        return type == null || TABLE.equals(type);
    }
    
    public static UsageType fromCode(String code) {
        return EnumUtils.fromCode(UsageType.class, code);
    }

    public static UsageType fromName(String name) {
        return EnumUtils.fromName(UsageType.class, name);
    }
}
