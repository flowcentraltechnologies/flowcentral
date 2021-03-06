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

import com.flowcentraltech.flowcentral.common.entities.BaseEntity;
import com.flowcentraltech.flowcentral.configuration.constants.NotificationMessageFormat;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.Table;

/**
 * Notification message entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_NOTIFOUTBOXMESSAGE")
public class NotificationOutboxMessage extends BaseEntity {

    @ForeignKey(NotificationOutbox.class)
    private Long notificationId;

    @ForeignKey
    private NotificationMessageFormat format;

    @Column(type = ColumnType.CLOB)
    private String message;

    public NotificationOutboxMessage(NotificationMessageFormat format, String message) {
        this.format = format;
        this.message = message;
    }

    public NotificationOutboxMessage() {

    }

    @Override
    public String getDescription() {
        return null;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public NotificationMessageFormat getFormat() {
        return format;
    }

    public void setFormat(NotificationMessageFormat format) {
        this.format = format;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
