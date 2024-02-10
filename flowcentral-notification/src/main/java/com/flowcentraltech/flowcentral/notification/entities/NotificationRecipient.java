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

import com.flowcentraltech.flowcentral.common.entities.BaseVersionTenantEntity;
import com.flowcentraltech.flowcentral.configuration.constants.NotifRecipientType;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Notification recipient entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_NOTIFRECIPIENT")
public class NotificationRecipient extends BaseVersionTenantEntity {

    @ForeignKey(NotificationOutbox.class)
    private Long notificationId;

    @ForeignKey
    private NotifRecipientType type;

    @Column(length = 64)
    private String recipientName;

    @Column(length = 512)
    private String recipientContact;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    @Override
    public String getDescription() {
        return recipientName;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientContact() {
        return recipientContact;
    }

    public void setRecipientContact(String recipientContact) {
        this.recipientContact = recipientContact;
    }

    public NotifRecipientType getType() {
        return type;
    }

    public void setType(NotifRecipientType type) {
        this.type = type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }
}
