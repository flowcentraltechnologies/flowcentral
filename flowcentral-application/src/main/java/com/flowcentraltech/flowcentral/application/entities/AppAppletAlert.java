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
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowAlertType;
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

    @ForeignKey(name = "ALERT_TY")
    private WorkflowAlertType type;

    @Column(length = 64)
    private String sender;

    @Column
    private boolean alertHeldBy;

    @Column
    private boolean alertWorkflowRoles;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    public Long getAppAppletId() {
        return appAppletId;
    }

    public void setAppAppletId(Long appAppletId) {
        this.appAppletId = appAppletId;
    }

    public WorkflowAlertType getType() {
        return type;
    }

    public void setType(WorkflowAlertType type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public boolean isAlertHeldBy() {
        return alertHeldBy;
    }

    public void setAlertHeldBy(boolean alertHeldBy) {
        this.alertHeldBy = alertHeldBy;
    }

    public boolean isAlertWorkflowRoles() {
        return alertWorkflowRoles;
    }

    public void setAlertWorkflowRoles(boolean alertWorkflowRoles) {
        this.alertWorkflowRoles = alertWorkflowRoles;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

}
