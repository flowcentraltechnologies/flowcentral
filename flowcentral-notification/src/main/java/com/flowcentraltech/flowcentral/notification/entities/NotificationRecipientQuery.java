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
package com.flowcentraltech.flowcentral.notification.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseVersionEntityQuery;

/**
 * Notification recipient query class;
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class NotificationRecipientQuery extends BaseVersionEntityQuery<NotificationRecipient> {

    public NotificationRecipientQuery() {
        super(NotificationRecipient.class);
    }

    public NotificationRecipientQuery notificationId(Long notificationId) {
        return (NotificationRecipientQuery) addEquals("notificationId", notificationId);
    }

    public NotificationRecipientQuery recipientNameLike(String recipientName) {
        return (NotificationRecipientQuery) addLike("recipientName", recipientName);
    }
}
