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

import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.constants.WfItemVersionType;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepType;
import com.flowcentraltech.flowcentral.organization.entities.Role;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Workflow step role entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_WORKSTEPROLE", uniqueConstraints = { @UniqueConstraint({ "wfStepId", "roleId" }) })
public class WfStepRole extends BaseAuditEntity {

    @ForeignKey(WfStep.class)
    private Long wfStepId;

    @ForeignKey(Role.class)
    private Long roleId;
    
    @ListOnly(key = "wfStepId", property = "type")
    private WorkflowStepType wfStepType;

    @ListOnly(key = "wfStepId", property = "name")
    private String wfStepName;

    @ListOnly(key = "wfStepId", property = "description")
    private String wfStepDesc;

    @ListOnly(key = "wfStepId", property = "label")
    private String wfStepLabel;

    @ListOnly(key = "wfStepId", property = "workflowName")
    private String workflowName;

    @ListOnly(key = "wfStepId", property = "workflowDesc")
    private String workflowDesc;

    @ListOnly(key = "wfStepId", property = "entityName")
    private String entityName;

    @ListOnly(key = "wfStepId", property = "workflowLabel")
    private String workflowLabel;

    @ListOnly(key = "wfStepId", property = "workflowLoadingTable")
    private String workflowLoadingTable;

    @ListOnly(key = "wfStepId", property = "applicationId")
    private Long applicationId;

    @ListOnly(key = "wfStepId", property = "applicationName")
    private String applicationName;

    @ListOnly(key = "wfStepId", property = "applicationDesc")
    private String applicationDesc;

    @ListOnly(key = "wfStepId", property = "configType")
    private ConfigType configType;

    @ListOnly(key = "wfStepId", property = "branchOnly")
    private boolean branchOnly;

    @ListOnly(key = "wfStepId", property = "departmentOnly")
    private boolean departmentOnly;

    @ListOnly(key = "roleId", property = "code")
    private String roleCode;

    @ListOnly(key = "roleId", property = "description")
    private String roleDesc;

    @ListOnly(key = "roleId", property = "wfItemVersionType")
    private WfItemVersionType wfItemVersionType;

    @Override
    public String getDescription() {
        return roleCode;
    }

    public Long getWfStepId() {
        return wfStepId;
    }

    public void setWfStepId(Long wfStepId) {
        this.wfStepId = wfStepId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public WorkflowStepType getWfStepType() {
        return wfStepType;
    }

    public void setWfStepType(WorkflowStepType wfStepType) {
        this.wfStepType = wfStepType;
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

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
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

    public ConfigType getConfigType() {
        return configType;
    }

    public void setConfigType(ConfigType configType) {
        this.configType = configType;
    }

    public boolean isBranchOnly() {
        return branchOnly;
    }

    public void setBranchOnly(boolean branchOnly) {
        this.branchOnly = branchOnly;
    }

    public boolean isDepartmentOnly() {
        return departmentOnly;
    }

    public void setDepartmentOnly(boolean departmentOnly) {
        this.departmentOnly = departmentOnly;
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

    public WfItemVersionType getWfItemVersionType() {
        return wfItemVersionType;
    }

    public void setWfItemVersionType(WfItemVersionType wfItemVersionType) {
        this.wfItemVersionType = wfItemVersionType;
    }

}
