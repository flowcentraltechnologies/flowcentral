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

package com.flowcentraltech.flowcentral.application.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Process variable list.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_PROCESSVARIABLE")
@StaticList(name = "processvariablelist", description = "$m{staticlist.processvariablelist}")
public enum ProcessVariable implements EnumConst {

    FORWARDED_BY(
            "FWB", "prcForwardedBy"),
    FORWARDED_BY_NAME(
            "FWN", "prcForwardedByName"),
    FORWARD_TO(
            "FWT", "prcForwardTo"),
    HELD_BY(
            "HDB", "prcHeldBy"),
    ENTITY_NAME(
            "ENM", "prcEntityName"),
    ENTITY_DESC(
            "END", "prcEntityDesc"),
    APP_TITLE(
            "APT", "prcAppTitle"),
    APP_CORRESPONDER(
            "APC", "prcAppCorresponder"),
    APP_URL(
            "APU", "prcAppURL"),
    APP_HTML_LINK(
            "APH", "prcAppHtmlLink");

    private final String code;

    private final String variableKey;

    private ProcessVariable(String code, String variableKey) {
        this.code = code;
        this.variableKey = variableKey;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return FORWARDED_BY.code;
    }

    public String variableKey() {
        return this.variableKey;
    }

    public static ProcessVariable fromCode(String code) {
        return EnumUtils.fromCode(ProcessVariable.class, code);
    }

    public static ProcessVariable fromName(String name) {
        return EnumUtils.fromName(ProcessVariable.class, name);
    }
}
