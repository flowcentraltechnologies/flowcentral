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

import com.flowcentraltech.flowcentral.common.entities.BaseAuditTenantEntityQuery;
import com.flowcentraltech.flowcentral.notification.constants.NotificationInboxStatus;

/**
 * Notification inbox query class;
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class NotificationInboxQuery extends BaseAuditTenantEntityQuery<NotificationInbox> {

    public NotificationInboxQuery() {
        super(NotificationInbox.class);
    }

    public NotificationInboxQuery userLoginId(String userLoginId) {
        return (NotificationInboxQuery) addEquals("userLoginId", userLoginId);
    }

    public NotificationInboxQuery status(NotificationInboxStatus status) {
        return (NotificationInboxQuery) addEquals("status", status);
    }
}
