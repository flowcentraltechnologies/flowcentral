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

package com.flowcentraltech.flowcentral.workflow.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseEntity;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepPriority;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Workflow item event entity.
 * 
 * @author FlowCentral Technologies Limited
 * @version 1.0
 */
@Table(name = "FC_WORKITEMEVENT")
public class WfItemEvent extends BaseEntity {

    @ForeignKey(WfItemHist.class)
    private Long wfItemHistId;

    @ForeignKey
    private WorkflowStepPriority priority;

    @Column(length = 64)
    private String wfStepName;

    @Column(type = ColumnType.TIMESTAMP)
    private Date stepDt;

    @Column(type = ColumnType.TIMESTAMP, nullable = true)
    private Date reminderDt;

    @Column(type = ColumnType.TIMESTAMP, nullable = true)
    private Date expectedDt;

    @Column(type = ColumnType.TIMESTAMP, nullable = true)
    private Date criticalDt;

    @Column(type = ColumnType.TIMESTAMP, nullable = true)
    private Date actionDt;

    @Column(length = 64, nullable = true)
    private String actor;

    @Column(length = 32, nullable = true)
    private String wfAction;

    @Column(name = "ACTOR_COMMENT", length = 512, nullable = true)
    private String comment;

    @Column(length = 64, nullable = true)
    private String prevWfStepName;

    @Column(name = "ERROR_CD", length = 32, nullable = true)
    private String errorCode;

    @Column(nullable = true)
    private Boolean reminderAlertSent;

    @Column(nullable = true)
    private Boolean criticalAlertSent;

    @Column(nullable = true)
    private Boolean expirationAlertSent;

    @Column(type = ColumnType.CLOB, name = "ERROR_MSG", nullable = true)
    private String errorMsg;

    @Column(type = ColumnType.CLOB, name = "ERROR_TRACE", nullable = true)
    private String errorTrace;

    @Column(type = ColumnType.CLOB, name = "ERROR_DOC", nullable = true)
    private String errorDoc;

    @ListOnly(key = "wfItemHistId", property = "applicationName")
    private String applicationName;

    @ListOnly(key = "wfItemHistId", property = "workflowName")
    private String workflowName;

    @ListOnly(key = "wfItemHistId", property = "entity")
    private String entity;

    @ListOnly(key = "wfItemHistId", property = "originWorkRecId")
    private Long originWorkRecId;

    @ListOnly(key = "wfItemHistId", property = "itemDesc")
    private String wfItemDesc;

    @ListOnly(key = "wfItemHistId", property = "branchCode")
    private String branchCode;

    @ListOnly(key = "wfItemHistId", property = "departmentCode")
    private String departmentCode;

    @ListOnly(key = "priority", property = "description")
    private String priorityDesc;

    @Override
    public String getDescription() {
        return wfItemDesc;
    }

    public Long getWfItemHistId() {
        return wfItemHistId;
    }

    public void setWfItemHistId(Long wfItemHistId) {
        this.wfItemHistId = wfItemHistId;
    }

    public String getWfStepName() {
        return wfStepName;
    }

    public void setWfStepName(String wfStepName) {
        this.wfStepName = wfStepName;
    }

    public Date getStepDt() {
        return stepDt;
    }

    public void setStepDt(Date stepDt) {
        this.stepDt = stepDt;
    }

    public Date getCriticalDt() {
        return criticalDt;
    }

    public void setCriticalDt(Date criticalDt) {
        this.criticalDt = criticalDt;
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

    public Date getActionDt() {
        return actionDt;
    }

    public void setActionDt(Date actionDt) {
        this.actionDt = actionDt;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getWfAction() {
        return wfAction;
    }

    public void setWfAction(String wfAction) {
        this.wfAction = wfAction;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPrevWfStepName() {
        return prevWfStepName;
    }

    public void setPrevWfStepName(String prevWfStepName) {
        this.prevWfStepName = prevWfStepName;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Boolean getReminderAlertSent() {
        return reminderAlertSent;
    }

    public void setReminderAlertSent(Boolean reminderAlertSent) {
        this.reminderAlertSent = reminderAlertSent;
    }

    public Boolean getCriticalAlertSent() {
        return criticalAlertSent;
    }

    public void setCriticalAlertSent(Boolean criticalAlertSent) {
        this.criticalAlertSent = criticalAlertSent;
    }

    public Boolean getExpirationAlertSent() {
        return expirationAlertSent;
    }

    public void setExpirationAlertSent(Boolean expirationAlertSent) {
        this.expirationAlertSent = expirationAlertSent;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorTrace() {
        return errorTrace;
    }

    public void setErrorTrace(String errorTrace) {
        this.errorTrace = errorTrace;
    }

    public String getErrorDoc() {
        return errorDoc;
    }

    public void setErrorDoc(String errorDoc) {
        this.errorDoc = errorDoc;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public Long getOriginWorkRecId() {
        return originWorkRecId;
    }

    public void setOriginWorkRecId(Long originWorkRecId) {
        this.originWorkRecId = originWorkRecId;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getWfItemDesc() {
        return wfItemDesc;
    }

    public void setWfItemDesc(String wfItemDesc) {
        this.wfItemDesc = wfItemDesc;
    }

    public WorkflowStepPriority getPriority() {
        return priority;
    }

    public void setPriority(WorkflowStepPriority priority) {
        this.priority = priority;
    }

    public String getPriorityDesc() {
        return priorityDesc;
    }

    public void setPriorityDesc(String priorityDesc) {
        this.priorityDesc = priorityDesc;
    }
}
