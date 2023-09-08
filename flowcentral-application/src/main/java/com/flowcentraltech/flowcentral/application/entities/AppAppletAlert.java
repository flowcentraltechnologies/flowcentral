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
package com.flowcentraltech.flowcentral.application.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Application applet alert entity;
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_APPLETALERT", uniqueConstraints = { @UniqueConstraint({ "appAppletId", "name" }),
        @UniqueConstraint({ "appAppletId", "description" }) })
public class AppAppletAlert extends BaseConfigNamedEntity {

    @ForeignKey(AppApplet.class)
    private Long appAppletId;

    @Column(length = 64, nullable = true)
    private String recipientPolicy;

    @Column(length = 64, nullable = true)
    private String recipientNameRule;

    @Column(length = 64, nullable = true)
    private String recipientContactRule;

    @Column(length = 64)
    private String sender;

    @ListOnly(key = "appAppletId", property = "applicationName")
    private String applicationName;

    @ListOnly(key = "appAppletId", property = "name")
    private String appletName;

    public Long getAppAppletId() {
        return appAppletId;
    }

    public void setAppAppletId(Long appAppletId) {
        this.appAppletId = appAppletId;
    }

    public String getRecipientPolicy() {
        return recipientPolicy;
    }

    public void setRecipientPolicy(String recipientPolicy) {
        this.recipientPolicy = recipientPolicy;
    }

    public String getRecipientNameRule() {
        return recipientNameRule;
    }

    public void setRecipientNameRule(String recipientNameRule) {
        this.recipientNameRule = recipientNameRule;
    }

    public String getRecipientContactRule() {
        return recipientContactRule;
    }

    public void setRecipientContactRule(String recipientContactRule) {
        this.recipientContactRule = recipientContactRule;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getAppletName() {
        return appletName;
    }

    public void setAppletName(String appletName) {
        this.appletName = appletName;
    }

}
