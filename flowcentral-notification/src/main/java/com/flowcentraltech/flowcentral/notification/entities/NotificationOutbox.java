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

import java.util.Date;
import java.util.List;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditTenantEntity;
import com.flowcentraltech.flowcentral.common.entities.FileAttachment;
import com.flowcentraltech.flowcentral.configuration.constants.NotificationType;
import com.flowcentraltech.flowcentral.notification.constants.NotificationOutboxStatus;
import com.tcdng.unify.core.annotation.Child;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Notification outbox entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_NOTIFOUTBOX")
public class NotificationOutbox extends BaseAuditTenantEntity {

    @ForeignKey(name = "NOTIFICATION_TY")
    private NotificationType type;

    @ForeignKey(name = "REC_ST")
    private NotificationOutboxStatus status;

    @Column(name = "NOTIFACTION_SUBJECT", length = 96)
    private String subject;

    @Column
    private int attempts;

    @Column(type = ColumnType.TIMESTAMP_UTC, nullable = true)
    private Date expiryDt;

    @Column(type = ColumnType.TIMESTAMP_UTC)
    private Date nextAttemptDt;

    @Column(type = ColumnType.TIMESTAMP_UTC, nullable = true)
    private Date sentDt;

    @Child
    private NotificationOutboxMessage notificationMessage;

    @ChildList
    private List<NotificationRecipient> notificationRecipientList;

    @ChildList(category = "notification")
    private List<FileAttachment> attachmentList;

    @ListOnly(key = "status", property = "description")
    private String statusDesc;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    @Override
    public String getDescription() {
        return subject;
    }

    public NotificationOutboxStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationOutboxStatus status) {
        this.status = status;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public Date getExpiryDt() {
        return expiryDt;
    }

    public void setExpiryDt(Date expiryDt) {
        this.expiryDt = expiryDt;
    }

    public Date getNextAttemptDt() {
        return nextAttemptDt;
    }

    public void setNextAttemptDt(Date nextAttemptDt) {
        this.nextAttemptDt = nextAttemptDt;
    }

    public Date getSentDt() {
        return sentDt;
    }

    public void setSentDt(Date sentDt) {
        this.sentDt = sentDt;
    }

    public NotificationOutboxMessage getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(NotificationOutboxMessage notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public List<NotificationRecipient> getNotificationRecipientList() {
        return notificationRecipientList;
    }

    public void setNotificationRecipientList(List<NotificationRecipient> notificationRecipientList) {
        this.notificationRecipientList = notificationRecipientList;
    }

    public List<FileAttachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<FileAttachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

}
