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
 * Font family type enumeration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_FONTFAMILYTYPE")
@StaticList(name = "fontfamilylist", description = "$m{staticlist.fontfamilylist}")
public enum FontFamilyType implements EnumConst {

    SERIF(
            "serif"),
    SANS_SERIF(
            "sans-serif"),
    MONOSPACE(
            "monospace"),
    CURSIVE(
            "cursive"),
    FANTASY(
            "fantasy");

    private final String code;

    private FontFamilyType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return SERIF.code;
    }

    public static FontFamilyType fromCode(String code) {
        return EnumUtils.fromCode(FontFamilyType.class, code);
    }

    public static FontFamilyType fromName(String name) {
        return EnumUtils.fromName(FontFamilyType.class, name);
    }

}
