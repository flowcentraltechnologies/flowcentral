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
package com.flowcentraltech.flowcentral.notification.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Notification outbox status constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_NOTIFOUTBOXSTATUS")
@StaticList(name = "notificationoutboxstatuslist", description = "$m{staticlist.notificationoutboxstatuslist}")
public enum NotificationOutboxStatus implements EnumConst {

    NOT_SENT(
            "N"),
    SENT(
            "S"),
    ABORTED(
            "A");

    private final String code;

    private NotificationOutboxStatus(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return NOT_SENT.code;
    }

    public static NotificationOutboxStatus fromCode(String code) {
        return EnumUtils.fromCode(NotificationOutboxStatus.class, code);
    }

    public static NotificationOutboxStatus fromName(String name) {
        return EnumUtils.fromName(NotificationOutboxStatus.class, name);
    }
}
