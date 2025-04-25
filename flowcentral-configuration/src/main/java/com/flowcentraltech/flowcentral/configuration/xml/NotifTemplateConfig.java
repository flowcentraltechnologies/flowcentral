/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.configuration.xml;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.flowcentraltech.flowcentral.configuration.constants.NotifMessageFormat;
import com.flowcentraltech.flowcentral.configuration.constants.NotifType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.NotificationMessageFormatXmlAdapter;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.NotificationTypeXmlAdapter;
import com.tcdng.unify.core.util.xml.adapter.CDataXmlAdapter;

/**
 * Notification template configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "notifTemplate")
public class NotifTemplateConfig extends BaseRootConfig {

    @JsonSerialize(using = NotificationTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = NotificationTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName="type")
    private NotifType notifType;

    @JsonSerialize(using = NotificationMessageFormatXmlAdapter.Serializer.class)
    @JsonDeserialize(using = NotificationMessageFormatXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName="format")
    private NotifMessageFormat messageFormat;

    @JacksonXmlProperty(isAttribute = true)
    private String entity;

    @JsonSerialize(using = CDataXmlAdapter.Serializer.class)
    @JsonDeserialize(using = CDataXmlAdapter.Deserializer.class)
    @JacksonXmlProperty
    private String subject;

    @JsonSerialize(using = CDataXmlAdapter.Serializer.class)
    @JsonDeserialize(using = CDataXmlAdapter.Deserializer.class)
    @JacksonXmlProperty
    private String body;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "parameter")
    private List<NotifTemplateParamConfig> paramList;
    
    public NotifTemplateConfig() {
        super("flowcentral-notificationtemplate-4.0.0.xsd");
        messageFormat = NotifMessageFormat.PLAIN_TEXT;
    }

    public NotifType getNotifType() {
        return notifType;
    }

    public void setNotifType(NotifType notifType) {
        this.notifType = notifType;
    }

    public NotifMessageFormat getMessageFormat() {
        return messageFormat;
    }

    public void setMessageFormat(NotifMessageFormat messageFormat) {
        this.messageFormat = messageFormat;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<NotifTemplateParamConfig> getParamList() {
        return paramList;
    }

    public void setParamList(List<NotifTemplateParamConfig> paramList) {
        this.paramList = paramList;
    }

}
