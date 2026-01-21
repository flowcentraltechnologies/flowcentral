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

package com.flowcentraltech.flowcentral.workflow.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.common.annotation.Index;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Column;

/**
 * Workflow item history entity.
 * 
 * @author FlowCentral Technologies Limited
 * @version 1.0
 */
@Table(name = "FC_WORKITEMHISTORY",
        uniqueConstraints = {@UniqueConstraint("caseNo")},
        indexes = {
                @Index("caseNo"), @Index("applicationName"), @Index("workflowName"),
                @Index("entity"), @Index("branchCode") })
public class WfItemHist extends BaseAuditEntity {

    @Column(name = "APPLICATION_NM", length = 64)
    private String applicationName;

    @Column(name = "WORKFLOW_NM", length = 128)
    private String workflowName;

    @Column(name = "ENTITY_NM", length = 128)
    private String entity;

    @Column(nullable = true)
    private Long originWorkRecId;

    @Column(name = "CASE_NO", length = 16, nullable = true)
    private String caseNo;

    @Column(name = "BRANCH_CD", nullable = true)
    private String branchCode;

    @Column(name = "DEPARTMENT_CD", nullable = true)
    private String departmentCode;

    @Column(length = 196)
    private String itemDesc;

    @Column(length = 96)
    private String initiatedBy;

    @Override
    public String getDescription() {
        return itemDesc;
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

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
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

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public void setInitiatedBy(String initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

}
