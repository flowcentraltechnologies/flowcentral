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

package com.flowcentraltech.flowcentral.common.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Collaboration type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_COLLABORATIONTYPE")
@StaticList(name = "collaborationtypelist", description = "$m{staticlist.collaborationtypelist}")
public enum CollaborationType implements EnumConst {

    WIDGET(
            "WDG"),
    ENTITY(
            "ENT"),
    REFERENCE(
            "REF"),
    APPLET(
            "APL"),
    CHART(
            "CRT"),
    DASHBOARD(
            "DSH"),
    NOTIFICATION_TEMPLATE(
            "NTP"),
    REPORT_CONFIGURATION(
            "RPC"),
    FORM(
            "FRM"),
    TABLE(
            "TBL"),
    WORKFLOW(
            "WRK");

    private final String code;

    private CollaborationType(String code) {
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

    public static CollaborationType fromCode(String code) {
        return EnumUtils.fromCode(CollaborationType.class, code);
    }

    public static CollaborationType fromName(String name) {
        return EnumUtils.fromName(CollaborationType.class, name);
    }
}
