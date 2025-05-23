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
 * Workflow alert type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_WFALERTTYPE")
@StaticList(name = "wfalerttypelist", description = "$m{staticlist.wfalerttypelist}")
public enum WorkflowAlertType implements EnumConst {

    PASS_THROUGH(
            "P"),
    USER_INTERACT(
            "U"),
    REMINDER_NOTIFICATION(
            "R"),
    CRITICAL_NOTIFICATION(
            "C"),
    EXPIRATION_NOTIFICATION(
            "E");

    private final String code;

    private WorkflowAlertType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return USER_INTERACT.code;
    }

    public boolean isPassThrough() {
        return PASS_THROUGH.equals(this);
    }

    public boolean isUserInteract() {
        return USER_INTERACT.equals(this);
    }

    public boolean isOnReminder() {
        return REMINDER_NOTIFICATION.equals(this);
    }

    public boolean isOnCritical() {
        return CRITICAL_NOTIFICATION.equals(this);
    }

    public boolean isOnExpiration() {
        return EXPIRATION_NOTIFICATION.equals(this);
    }

    public static WorkflowAlertType fromCode(String code) {
        return EnumUtils.fromCode(WorkflowAlertType.class, code);
    }

    public static WorkflowAlertType fromName(String name) {
        return EnumUtils.fromName(WorkflowAlertType.class, name);
    }
}
