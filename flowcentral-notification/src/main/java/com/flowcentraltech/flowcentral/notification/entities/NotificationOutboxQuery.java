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
package com.flowcentraltech.flowcentral.notification.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditTenantEntityQuery;
import com.flowcentraltech.flowcentral.configuration.constants.NotificationType;
import com.flowcentraltech.flowcentral.notification.constants.NotificationOutboxStatus;
import com.tcdng.unify.core.util.CalendarUtils;

/**
 * Notification outbox query class.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class NotificationOutboxQuery extends BaseAuditTenantEntityQuery<NotificationOutbox> {

    public NotificationOutboxQuery() {
        super(NotificationOutbox.class);
    }

    public NotificationOutboxQuery notificationChannelId(Long notificationChannelId) {
        return (NotificationOutboxQuery) addEquals("notificationChannelId", notificationChannelId);
    }

    public NotificationOutboxQuery moduleId(Long moduleId) {
        return (NotificationOutboxQuery) addEquals("moduleId", moduleId);
    }

    public NotificationOutboxQuery moduleName(String moduleName) {
        return (NotificationOutboxQuery) addEquals("moduleName", moduleName);
    }

    public NotificationOutboxQuery type(NotificationType type) {
        return (NotificationOutboxQuery) addEquals("type", type);
    }

    public NotificationOutboxQuery sentOn(Date date) {
        return (NotificationOutboxQuery) addBetween("sentDt", CalendarUtils.getMidnightDate(date),
                CalendarUtils.getLastSecondDate(date));
    }

    public NotificationOutboxQuery due(Date now) {
        return (NotificationOutboxQuery) addLessThanEqual("nextAttemptDt", now);
    }

    public NotificationOutboxQuery status(NotificationOutboxStatus status) {
        return (NotificationOutboxQuery) addEquals("status", status);
    }
}
