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
 * Form element type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_FORMELEMENTTYPE")
@StaticList(name = "formelementtypelist", description = "$m{staticlist.formelementtypelist}")
public enum FormElementType implements EnumConst {

    ANNOTATION(
            "ANO"),
    TAB(
            "TAB"),
    SECTION(
            "SEC"),
    FIELD(
            "FLD");

    private final String code;

    private FormElementType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return FIELD.code;
    }

    public boolean isAnnotationType() {
        return ANNOTATION.equals(this);
    }

    public boolean isTabType() {
        return TAB.equals(this);
    }

    public boolean isSectionType() {
        return SECTION.equals(this);
    }

    public boolean isFieldType() {
        return FIELD.equals(this);
    }

    public static FormElementType fromCode(String code) {
        return EnumUtils.fromCode(FormElementType.class, code);
    }

    public static FormElementType fromName(String name) {
        return EnumUtils.fromName(FormElementType.class, name);
    }
}
