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
package com.flowcentraltech.flowcentral.common.constants;

import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Dialog form mode enumeration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public enum DialogFormMode implements EnumConst {

    CREATE(
            "C"),
    CREATE_SUB(
            "S"),
    UPDATE(
            "U"),
    DELETE(
            "D"),
    MOVE(
            "M");

    private final String code;

    private DialogFormMode(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return CREATE.code;
    }

    public boolean isCreate() {
        return CREATE.equals(this);
    }

    public boolean isUpdate() {
        return UPDATE.equals(this);
    }

    public boolean isDelete() {
        return DELETE.equals(this);
    }

    public boolean isMove() {
        return MOVE.equals(this);
    }

    public static DialogFormMode fromCode(String code) {
        return EnumUtils.fromCode(DialogFormMode.class, code);
    }

    public static DialogFormMode fromName(String name) {
        return EnumUtils.fromName(DialogFormMode.class, name);
    }

}
