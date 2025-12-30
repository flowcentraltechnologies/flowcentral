/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.workflow.data;

import java.util.Date;

/**
 * Workflow item accessible definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class WfItemAccessible {

    private Long wfItemId;

    private Long workRecId;
    
    private String branchCode;
    
    private String departmentCode;
    
    private String wfItemCaseNo;
    
    private String wfItemDesc;
    
    private String workflowName;
    
    private String workflowLabel;
    
    private String stepName;
    
    private String entityName;
    
    private Date stepOn;

    private Date reminderOn;

    private Date expectedOn;

    private Date criticalOn;

    public WfItemAccessible(Long wfItemId, Long workRecId, String branchCode, String departmentCode,
            String wfItemCaseNo, String wfItemDesc, String workflowName, String workflowLabel, String stepName,
            String entityName, Date stepOn, Date reminderOn, Date expectedOn, Date criticalOn) {
        this.wfItemId = wfItemId;
        this.workRecId = workRecId;
        this.branchCode = branchCode;
        this.departmentCode = departmentCode;
        this.wfItemCaseNo = wfItemCaseNo;
        this.wfItemDesc = wfItemDesc;
        this.workflowName = workflowName;
        this.workflowLabel = workflowLabel;
        this.stepName = stepName;
        this.entityName = entityName;
        this.stepOn = stepOn;
        this.reminderOn = reminderOn;
        this.expectedOn = expectedOn;
        this.criticalOn = criticalOn;
    }

    public WfItemAccessible() {
        
    }
    
    public Long getWfItemId() {
        return wfItemId;
    }

    public void setWfItemId(Long wfItemId) {
        this.wfItemId = wfItemId;
    }

    public Long getWorkRecId() {
        return workRecId;
    }

    public void setWorkRecId(Long workRecId) {
        this.workRecId = workRecId;
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

    public String getWfItemCaseNo() {
        return wfItemCaseNo;
    }

    public void setWfItemCaseNo(String wfItemCaseNo) {
        this.wfItemCaseNo = wfItemCaseNo;
    }

    public String getWfItemDesc() {
        return wfItemDesc;
    }

    public void setWfItemDesc(String wfItemDesc) {
        this.wfItemDesc = wfItemDesc;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public String getWorkflowLabel() {
        return workflowLabel;
    }

    public void setWorkflowLabel(String workflowLabel) {
        this.workflowLabel = workflowLabel;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Date getStepOn() {
        return stepOn;
    }

    public void setStepOn(Date stepOn) {
        this.stepOn = stepOn;
    }

    public Date getReminderOn() {
        return reminderOn;
    }

    public void setReminderOn(Date reminderOn) {
        this.reminderOn = reminderOn;
    }

    public Date getExpectedOn() {
        return expectedOn;
    }

    public void setExpectedOn(Date expectedOn) {
        this.expectedOn = expectedOn;
    }

    public Date getCriticalOn() {
        return criticalOn;
    }

    public void setCriticalOn(Date criticalOn) {
        this.criticalOn = criticalOn;
    }
    
}
