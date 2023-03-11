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

package com.flowcentraltech.flowcentral.workflow.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseTenantEntity;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepPriority;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Workflow item entity.
 * 
 * @author FlowCentral Technologies Limited
 * @version 1.0
 */
@Table(name = "FC_WORKITEM")
public class WfItem extends BaseTenantEntity {

    @ForeignKey(type = WfItemEvent.class)
    private Long wfItemEventId;

    @Column(length = 64, nullable = true)
    private String heldBy;

    @Column(length = 64, nullable = true)
    private String forwardedBy;

    @Column(length = 64, nullable = true)
    private String forwardTo;

    @Column
    private Long workRecId;

    @Column
    private boolean criticalAlertSent;

    @Column
    private boolean expirationAlertSent;

    @ListOnly(key = "wfItemEventId", property = "wfItemHistId")
    private Long wfItemHistId;

    @ListOnly(key = "wfItemEventId", property = "wfStepName")
    private String wfStepName;

    @ListOnly(key = "wfItemEventId", property = "stepDt")
    private Date stepDt;

    @ListOnly(key = "wfItemEventId", property = "expectedDt")
    private Date expectedDt;

    @ListOnly(key = "wfItemEventId", property = "criticalDt")
    private Date criticalDt;

    @ListOnly(key = "wfItemEventId", property = "applicationName")
    private String applicationName;

    @ListOnly(key = "wfItemEventId", property = "workflowName")
    private String workflowName;

    @ListOnly(key = "wfItemEventId", property = "entity")
    private String entity;

    @ListOnly(key = "wfItemEventId", property = "originWorkRecId")
    private Long originWorkRecId;

    @ListOnly(key = "wfItemEventId", property = "wfItemDesc")
    private String wfItemDesc;

    @ListOnly(key = "wfItemEventId", property = "branchCode")
    private String branchCode;

    @ListOnly(key = "wfItemEventId", property = "departmentCode")
    private String departmentCode;

    @ListOnly(key = "wfItemEventId", property = "prevWfStepName")
    private String prevWfStepName;

    @ListOnly(key = "wfItemEventId", property = "errorCode")
    private String errorCode;

    @ListOnly(key = "wfItemEventId", property = "errorMsg")
    private String errorMsg;

    @ListOnly(key = "wfItemEventId", property = "errorTrace")
    private String errorTrace;

    @ListOnly(key = "wfItemEventId", property = "errorDoc")
    private String errorDoc;

    @ListOnly(key = "wfItemEventId", property = "priority")
    private WorkflowStepPriority priority;

    @ListOnly(key = "wfItemEventId", property = "priorityDesc")
    private String priorityDesc;

    @Override
    public String getDescription() {
        return wfItemDesc;
    }

    public Long getWfItemEventId() {
        return wfItemEventId;
    }

    public void setWfItemEventId(Long wfItemEventId) {
        this.wfItemEventId = wfItemEventId;
    }

    public String getHeldBy() {
        return heldBy;
    }

    public void setHeldBy(String heldBy) {
        this.heldBy = heldBy;
    }

    public String getForwardedBy() {
        return forwardedBy;
    }

    public void setForwardedBy(String forwardedBy) {
        this.forwardedBy = forwardedBy;
    }

    public String getForwardTo() {
        return forwardTo;
    }

    public void setForwardTo(String forwardTo) {
        this.forwardTo = forwardTo;
    }

    public Long getWorkRecId() {
        return workRecId;
    }

    public void setWorkRecId(Long workRecId) {
        this.workRecId = workRecId;
    }

    public boolean isCriticalAlertSent() {
        return criticalAlertSent;
    }

    public void setCriticalAlertSent(boolean criticalAlertSent) {
        this.criticalAlertSent = criticalAlertSent;
    }

    public boolean isExpirationAlertSent() {
        return expirationAlertSent;
    }

    public void setExpirationAlertSent(boolean expirationAlertSent) {
        this.expirationAlertSent = expirationAlertSent;
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

    public String getWfItemDesc() {
        return wfItemDesc;
    }

    public void setWfItemDesc(String wfItemDesc) {
        this.wfItemDesc = wfItemDesc;
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
