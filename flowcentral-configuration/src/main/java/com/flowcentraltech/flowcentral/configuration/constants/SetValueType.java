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
 * Set value type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_SETVALUETYPE")
@StaticList(name = "setvaluetypelist", description = "$m{staticlist.setvaluetypelist}")
public enum SetValueType implements EnumConst {

    NULL(
            "NUL",
            "$m{setvaluetype.issettonull}", true),
    IMMEDIATE(
            "IMM",
            "$m{setvaluetype.issetto}", false),
    IMMEDIATE_LINGUAL(
            "IML",
            "$m{setvaluetype.issettolingual}", false),
    IMMEDIATE_FIELD(
            "IMF",
            "$m{setvaluetype.issettofield}", true),
    PROCESS_VARIABLE(
            "PVR",
            "$m{setvaluetype.issettoprocessvariable}", false),
    SESSION_PARAMETER(
            "SPM",
            "$m{setvaluetype.issettosessionparameter}", false),
    GENERATOR(
            "GEN",
            "$m{setvaluetype.isgeneratedfrom}", true);

    private final String code;

    private final String label;
    
    private final boolean supportsRef;

    private SetValueType(String code, String label, boolean supportsRef) {
        this.code = code;
        this.label = label;
        this.supportsRef = supportsRef;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return IMMEDIATE.code;
    }

    public boolean isSupportsRef() {
        return supportsRef;
    }

    public boolean isNoParam() {
        return NULL.equals(this);
    }

    public boolean isLingual() {
        return IMMEDIATE_LINGUAL.equals(this);
    }

    public boolean isProcessVariable() {
        return PROCESS_VARIABLE.equals(this);
    }

    public boolean isSessionParameter() {
        return SESSION_PARAMETER.equals(this);
    }

    public String label() {
        return this.label;
    }

    public static SetValueType fromCode(String code) {
        return EnumUtils.fromCode(SetValueType.class, code);
    }

    public static SetValueType fromName(String name) {
        return EnumUtils.fromName(SetValueType.class, name);
    }
}
