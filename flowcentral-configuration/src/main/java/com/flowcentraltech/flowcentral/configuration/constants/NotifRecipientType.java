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

package com.flowcentraltech.flowcentral.configuration.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.notification.EmailRecipient;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Notification recipient type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_NOTIFRECIPIENTTYPE")
@StaticList(name = "notificationrecipienttypelist", description = "$m{staticlist.notificationrecipienttypelist}")
public enum NotifRecipientType implements EnumConst {

    TO(
            "TO",
            EmailRecipient.TYPE.TO),
    CC(
            "CC",
            EmailRecipient.TYPE.CC),
    BCC(
            "BC",
            EmailRecipient.TYPE.BCC);

    private final String code;

    private final EmailRecipient.TYPE emailRecipientType;

    private NotifRecipientType(String code, EmailRecipient.TYPE emailRecipientType) {
        this.code = code;
        this.emailRecipientType = emailRecipientType;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return TO.code;
    }

    public EmailRecipient.TYPE emailRecipientType() {
        return emailRecipientType;
    }

    public static NotifRecipientType fromCode(String code) {
        return EnumUtils.fromCode(NotifRecipientType.class, code);
    }

    public static NotifRecipientType fromName(String name) {
        return EnumUtils.fromName(NotifRecipientType.class, name);
    }
}
