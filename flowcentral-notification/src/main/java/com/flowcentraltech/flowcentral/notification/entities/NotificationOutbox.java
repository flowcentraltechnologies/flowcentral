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

import java.util.Date;
import java.util.List;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditTenantEntity;
import com.flowcentraltech.flowcentral.configuration.constants.ImportanceType;
import com.flowcentraltech.flowcentral.configuration.constants.NotifMessageFormat;
import com.flowcentraltech.flowcentral.configuration.constants.NotifType;
import com.flowcentraltech.flowcentral.notification.constants.NotificationOutboxStatus;
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
    private NotifType type;

    @ForeignKey
    private NotifMessageFormat format;

    @ForeignKey
    private ImportanceType importance;
    
    @ForeignKey(name = "REC_ST")
    private NotificationOutboxStatus status;

    @Column(name = "NOTIFACTION_FROM", length = 256, nullable = true)
    private String from;

    @Column(name = "NOTIFACTION_SUBJECT", length = 256)
    private String subject;

    @Column
    private int attempts;

    @Column(type = ColumnType.TIMESTAMP, nullable = true)
    private Date expiryDt;

    @Column(type = ColumnType.TIMESTAMP)
    private Date nextAttemptDt;

    @Column(type = ColumnType.TIMESTAMP, nullable = true)
    private Date sentDt;

    @Column(type = ColumnType.CLOB, nullable = true)
    private String message;
    
    @ChildList
    private List<NotificationRecipient> recipientList;

    @ChildList
    private List<NotificationOutboxAttachment> attachmentList;

    @ChildList
    private List<NotificationOutboxError> errorList;

    @ListOnly(key = "status", property = "description")
    private String statusDesc;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    @ListOnly(key = "format", property = "description")
    private String formatDesc;

    @ListOnly(key = "importance", property = "description")
    private String importanceDesc;

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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
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

    public List<NotificationRecipient> getRecipientList() {
        return recipientList;
    }

    public void setRecipientList(List<NotificationRecipient> recipientList) {
        this.recipientList = recipientList;
    }

    public List<NotificationOutboxAttachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<NotificationOutboxAttachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public List<NotificationOutboxError> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<NotificationOutboxError> errorList) {
        this.errorList = errorList;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public NotifType getType() {
        return type;
    }

    public void setType(NotifType type) {
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

    public NotifMessageFormat getFormat() {
        return format;
    }

    public void setFormat(NotifMessageFormat format) {
        this.format = format;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFormatDesc() {
        return formatDesc;
    }

    public void setFormatDesc(String formatDesc) {
        this.formatDesc = formatDesc;
    }

    public ImportanceType getImportance() {
        return importance;
    }

    public void setImportance(ImportanceType importance) {
        this.importance = importance;
    }

    public String getImportanceDesc() {
        return importanceDesc;
    }

    public void setImportanceDesc(String importanceDesc) {
        this.importanceDesc = importanceDesc;
    }

}
