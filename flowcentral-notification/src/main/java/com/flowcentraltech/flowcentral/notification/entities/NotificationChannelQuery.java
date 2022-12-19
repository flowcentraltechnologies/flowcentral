/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.common.entities.BaseStatusTenantEntityQuery;
import com.flowcentraltech.flowcentral.configuration.constants.NotificationType;

/**
 * Notification channel query class;
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class NotificationChannelQuery extends BaseStatusTenantEntityQuery<NotificationChannel> {

    public NotificationChannelQuery() {
        super(NotificationChannel.class);
    }

    public NotificationChannelQuery notificationType(NotificationType notificationType) {
        return (NotificationChannelQuery) addEquals("notificationType", notificationType);
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
