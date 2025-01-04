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

import java.util.List;

import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.configuration.constants.NotifMessageFormat;
import com.flowcentraltech.flowcentral.configuration.constants.NotifType;
import com.tcdng.unify.common.annotation.ColumnType;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Entity for storing notification template information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_NOTIFTEMPLATE")
public class NotificationTemplate extends BaseApplicationEntity {

    @ForeignKey
    private NotifType notificationType;

    @ForeignKey
    private NotifMessageFormat messageFormat;

    @Column(length = 128, nullable = true)
    private String entity;

    @Column(length = 128)
    private String subject;

    @Column(type = ColumnType.CLOB)
    private String template;

    @ChildList
    private List<NotificationTemplateParam> paramList;

    @ListOnly(key = "notificationType", property = "description")
    private String notificationTypeDesc;

    @ListOnly(key = "messageFormat", property = "description")
    private String messageFormatDesc;

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

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public NotifMessageFormat getMessageFormat() {
        return messageFormat;
    }

    public void setMessageFormat(NotifMessageFormat messageFormat) {
        this.messageFormat = messageFormat;
    }

    public String getMessageFormatDesc() {
        return messageFormatDesc;
    }

    public void setMessageFormatDesc(String messageFormatDesc) {
        this.messageFormatDesc = messageFormatDesc;
    }

    public List<NotificationTemplateParam> getParamList() {
        return paramList;
    }

    public void setParamList(List<NotificationTemplateParam> paramList) {
        this.paramList = paramList;
    }

}
