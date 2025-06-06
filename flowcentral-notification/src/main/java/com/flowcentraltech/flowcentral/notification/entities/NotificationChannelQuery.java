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
package com.flowcentraltech.flowcentral.notification.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntityQuery;
import com.flowcentraltech.flowcentral.configuration.constants.NotifType;

/**
 * Notification channel query class;
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class NotificationChannelQuery extends BaseStatusEntityQuery<NotificationChannel> {

    public NotificationChannelQuery() {
        super(NotificationChannel.class);
    }

    public NotificationChannelQuery notifType(NotifType notifType) {
        return (NotificationChannelQuery) addEquals("notificationType", notifType);
    }

    public NotificationChannelQuery name(String name) {
        return (NotificationChannelQuery) addEquals("name", name);
    }

    public NotificationChannelQuery nameLike(String name) {
        return (NotificationChannelQuery) addLike("name", name);
    }

    public NotificationChannelQuery descriptionLike(String description) {
        return (NotificationChannelQuery) addLike("description", description);
    }

}
