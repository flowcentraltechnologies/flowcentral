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
 * Notification type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_NOTIFICATIONTYPE")
@StaticList(name = "notificationtypelist", description = "$m{staticlist.notificationtypelist}")
public enum NotifType implements EnumConst {

    SYSTEM(
            "SYS"),
    EMAIL(
            "EML"),
    SMS(
            "SMS");

    private final String code;

    private NotifType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return SYSTEM.code;
    }

    public boolean isSystem() {
        return SYSTEM.equals(this);
    }

    public boolean isEmail() {
        return EMAIL.equals(this);
    }

    public boolean isSMS() {
        return SMS.equals(this);
    }

    public static NotifType fromCode(String code) {
        return EnumUtils.fromCode(NotifType.class, code);
    }

    public static NotifType fromName(String name) {
        return EnumUtils.fromName(NotifType.class, name);
    }
}
