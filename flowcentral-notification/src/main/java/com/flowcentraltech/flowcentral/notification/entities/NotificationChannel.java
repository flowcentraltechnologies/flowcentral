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

import java.util.List;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusTenantEntity;
import com.flowcentraltech.flowcentral.configuration.constants.NotifType;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Notification channel entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_NOTIFCHANNEL", uniqueConstraints = { @UniqueConstraint({ "notificationType" }),
        @UniqueConstraint({ "name" }), @UniqueConstraint({ "description" }) })
public class NotificationChannel extends BaseStatusTenantEntity {

    @ForeignKey
    private NotifType notificationType;

    @Column(name = "CHANNEL_NM", length = 48)
    private String name;

    @Column(name = "CHANNEL_DESC", length = 64)
    private String description;

    @Column(name = "SENDER_NM", length = 64)
    private String senderName;

    @Column(length = 96)
    private String senderContact;

    @ListOnly(key = "notificationType", property = "description")
    private String notificationTypeDesc;

    @ChildList
    private List<NotificationChannelProp> channelPropList;

    @Override
    public String getDescription() {
        return description;
    }

    public NotifType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotifType notificationType) {
        this.notificationType = notificationType;
    }

    public String getNotificationTypeDesc() {
        return notificationTypeDesc;
    }

    public void setNotificationTypeDesc(String notificationTypeDesc) {
        this.notificationTypeDesc = notificationTypeDesc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderContact() {
        return senderContact;
    }

    public void setSenderContact(String senderContact) {
        this.senderContact = senderContact;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public List<NotificationChannelProp> getChannelPropList() {
        return channelPropList;
    }

    public void setChannelPropList(List<NotificationChannelProp> channelPropList) {
        this.channelPropList = channelPropList;
    }


}
