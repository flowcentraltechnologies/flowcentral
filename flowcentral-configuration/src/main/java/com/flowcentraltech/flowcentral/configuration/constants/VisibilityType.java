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
 * Visibility type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_VISIBILITYTYPE")
@StaticList(name = "visibilitytypelist", description = "$m{staticlist.visibilitytypelist}")
public enum VisibilityType implements EnumConst {

    FIXED(
            "F"),
    DISAPPEARING(
            "D"),
    CLOSABLE(
            "C");

    private final String code;

    private VisibilityType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return FIXED.code;
    }

    public boolean isFixed() {
        return FIXED.equals(this);
    }

    public boolean isDisappearing() {
        return DISAPPEARING.equals(this);
    }

    public boolean isClosable() {
        return CLOSABLE.equals(this);
    }
    
    public static VisibilityType fromCode(String code) {
        return EnumUtils.fromCode(VisibilityType.class, code);
    }

    public static VisibilityType fromName(String name) {
        return EnumUtils.fromName(VisibilityType.class, name);
    }
}
