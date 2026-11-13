/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.workflow.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseEntity;
import com.tcdng.unify.common.annotation.ColumnType;
import com.tcdng.unify.common.annotation.Index;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Workflow item alert entity.
 * 
 * @author FlowCentral Technologies Limited
 * @version 1.0
 */
@Table(name = "FC_WORKITEMALERT", indexes = {@Index({"wfItemEventId"})})
public class WfItemAlert extends BaseEntity {

    @ForeignKey(type = WfItemEvent.class)
    private Long wfItemEventId;

    @Column(type = ColumnType.TIMESTAMP, nullable = true)
    private Date reminderDt;

    @Column(type = ColumnType.TIMESTAMP, nullable = true)
    private Date expectedDt;

    @Column(type = ColumnType.TIMESTAMP, nullable = true)
    private Date criticalDt;

    @Column
    private boolean reminderSent;

    @Column
    private boolean criticalSent;

    @Column
    private boolean expirationSent;

    @Column
    private boolean disabled;

    @ListOnly(key = "wfItemEventId", property = "actionDt")
    private Date actionDt;

    @Override
    public String getDescription() {
        return null;
    }

    public Long getWfItemEventId() {
        return wfItemEventId;
    }

    public void setWfItemEventId(Long wfItemEventId) {
        this.wfItemEventId = wfItemEventId;
    }

    public Date getReminderDt() {
        return reminderDt;
    }

    public void setReminderDt(Date reminderDt) {
        this.reminderDt = reminderDt;
    }

    public Date getExpectedDt() {
        return expectedDt;
    }

    public void setExpectedDt(Date expectedDt) {
        this.expectedDt = expectedDt;
    }

    public Date getCriticalDt() {
        return criticalDt;
    }

    public void setCriticalDt(Date criticalDt) {
        this.criticalDt = criticalDt;
    }

    public boolean isReminderSent() {
        return reminderSent;
    }

    public void setReminderSent(boolean reminderSent) {
        this.reminderSent = reminderSent;
    }

    public boolean isCriticalSent() {
        return criticalSent;
    }

    public void setCriticalSent(boolean criticalSent) {
        this.criticalSent = criticalSent;
    }

    public boolean isExpirationSent() {
        return expirationSent;
    }

    public void setExpirationSent(boolean expirationSent) {
        this.expirationSent = expirationSent;
    }

    public Date getActionDt() {
        return actionDt;
    }

    public void setActionDt(Date actionDt) {
        this.actionDt = actionDt;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

}
