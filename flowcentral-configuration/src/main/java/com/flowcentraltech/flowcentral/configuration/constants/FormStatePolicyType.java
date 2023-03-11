/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Form state policy type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_FORMSTATEPOLICYTYPE")
@StaticList(name = "formstatepolicytypelist", description = "$m{staticlist.formstatepolicytypelist}")
public enum FormStatePolicyType implements EnumConst {

    /* Executes on creation of form*/
    ON_CREATE(
            "CRT"),
    /* Executes on switch of a field*/
    ON_SWITCH(
            "SWH"),
    /* Executes on construction of form. Only value generators are fired*/
    ON_FORM_CONSTRUCT_SET_VALUES(
            "FCV"),
    /* Executes on actual 'save' or 'update'. Only value generators are fired*/
    ON_DELAYED_SET_VALUES(
            "DSV");

    private final String code;

    private FormStatePolicyType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return ON_CREATE.code;
    }

    public static FormStatePolicyType fromCode(String code) {
        return EnumUtils.fromCode(FormStatePolicyType.class, code);
    }

    public static FormStatePolicyType fromName(String name) {
        return EnumUtils.fromName(FormStatePolicyType.class, name);
    }
}
