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
package com.flowcentraltech.flowcentral.organization.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.Mapped;
import com.tcdng.unify.core.annotation.TableName;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Mapped role interactive workflow step entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Mapped("default-mappedrolewfstepprovider")
@TableName("FC_MAPPEDROLEWFSTEP")
public class MappedRoleWfStep extends BaseAuditEntity {

    @ForeignKey(Role.class)
    private Long roleId;
    
    @Column
    private Long wfStepId;

    @Column
    private String wfStepName;

    @Column
    private String wfStepDesc;

    @Column
    private String wfStepLabel;

    @Column
    private String workflowName;

    @Column
    private String workflowDesc;

    @Column
    private String entityName;

    @Column
    private String workflowLabel;

    @Column
    private String workflowLoadingTable;

    @Column
    private String applicationName;

    @Column
    private String applicationDesc;

    @Column
    private String roleCode;

    @Column
    private String roleDesc;

    @Override
    public String getDescription() {
        return StringUtils.concatenate(roleDesc, " - ", wfStepDesc);
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getWfStepId() {
        return wfStepId;
    }

    public void setWfStepId(Long wfStepId) {
        this.wfStepId = wfStepId;
    }

    public String getWfStepName() {
        return wfStepName;
    }

    public void setWfStepName(String wfStepName) {
        this.wfStepName = wfStepName;
    }

    public String getWfStepDesc() {
        return wfStepDesc;
    }

    public void setWfStepDesc(String wfStepDesc) {
        this.wfStepDesc = wfStepDesc;
    }

    public String getWfStepLabel() {
        return wfStepLabel;
    }

    public void setWfStepLabel(String wfStepLabel) {
        this.wfStepLabel = wfStepLabel;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public String getWorkflowDesc() {
        return workflowDesc;
    }

    public void setWorkflowDesc(String workflowDesc) {
        this.workflowDesc = workflowDesc;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getWorkflowLabel() {
        return workflowLabel;
    }

    public void setWorkflowLabel(String workflowLabel) {
        this.workflowLabel = workflowLabel;
    }

    public String getWorkflowLoadingTable() {
        return workflowLoadingTable;
    }

    public void setWorkflowLoadingTable(String workflowLoadingTable) {
        this.workflowLoadingTable = workflowLoadingTable;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationDesc() {
        return applicationDesc;
    }

    public void setApplicationDesc(String applicationDesc) {
        this.applicationDesc = applicationDesc;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

}
