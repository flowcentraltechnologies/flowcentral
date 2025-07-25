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
 * Record action type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_RECORDACTIONTYPE")
@StaticList(name = "recordactiontypelist", description = "$m{staticlist.recordactiontypelist}")
public enum RecordActionType implements EnumConst {

    CREATE(
            "C"),
    CREATEAS(
            "S"),
    UPDATE(
            "U"),
    UPDATE_ORIGINAL(
            "Y"),
    DELETE(
            "D");

    private final String code;

    private RecordActionType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return UPDATE.code;
    }

    public boolean isCreate() {
        return CREATE.equals(this);
    }

    public boolean isCreateAs() {
        return CREATEAS.equals(this);
    }

    public boolean isCreateOnly() {
        return CREATE.equals(this) || CREATEAS.equals(this);
    }

    public boolean isUpdate() {
        return CREATE.equals(this) || CREATEAS.equals(this) || UPDATE.equals(this) || UPDATE_ORIGINAL.equals(this);
    }

    public boolean isUpdateOnly() {
        return UPDATE.equals(this) || UPDATE_ORIGINAL.equals(this);
    }

    public static RecordActionType fromCode(String code) {
        return EnumUtils.fromCode(RecordActionType.class, code);
    }

    public static RecordActionType fromName(String name) {
        return EnumUtils.fromName(RecordActionType.class, name);
    }
}
