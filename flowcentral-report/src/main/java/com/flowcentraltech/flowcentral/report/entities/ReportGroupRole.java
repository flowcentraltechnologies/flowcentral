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
package com.flowcentraltech.flowcentral.report.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.flowcentraltech.flowcentral.organization.entities.Role;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.common.data.Describable;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Entity for storing report group role information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_REPORTGROUPROLE", uniqueConstraints = { @UniqueConstraint({ "reportGroupId", "roleId" }) })
public class ReportGroupRole extends BaseAuditEntity implements Describable {

    @ForeignKey(ReportGroup.class)
    private Long reportGroupId;

    @ForeignKey(Role.class)
    private Long roleId;

    @ListOnly(key = "reportGroupId", property = "name")
    private String reportGroupName;

    @ListOnly(key = "reportGroupId", property = "description")
    private String reportGroupDesc;

    @ListOnly(key = "reportGroupId", property = "label")
    private String reportGroupLabel;

    @ListOnly(key = "roleId", property = "code")
    private String roleCode;

    @ListOnly(key = "roleId", property = "description")
    private String roleDesc;

    @ListOnly(key = "roleId", property = "activeBefore")
    private Date activeBefore;

    @ListOnly(key = "roleId", property = "activeAfter")
    private Date activeAfter;

    @ListOnly(key = "roleId", property = "status")
    private RecordStatus roleStatus;

    @ListOnly(key = "roleId", property = "departmentId")
    private Long departmentId;

    @Override
    public String getDescription() {
        return StringUtils.concatenate(reportGroupDesc, " - ", roleDesc);
    }

    public Long getReportGroupId() {
        return reportGroupId;
    }

    public void setReportGroupId(Long reportGroupId) {
        this.reportGroupId = reportGroupId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getReportGroupName() {
        return reportGroupName;
    }

    public void setReportGroupName(String reportGroupName) {
        this.reportGroupName = reportGroupName;
    }

    public String getReportGroupDesc() {
        return reportGroupDesc;
    }

    public void setReportGroupDesc(String reportGroupDesc) {
        this.reportGroupDesc = reportGroupDesc;
    }

    public String getReportGroupLabel() {
        return reportGroupLabel;
    }

    public void setReportGroupLabel(String reportGroupLabel) {
        this.reportGroupLabel = reportGroupLabel;
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

    public Date getActiveBefore() {
        return activeBefore;
    }

    public void setActiveBefore(Date activeBefore) {
        this.activeBefore = activeBefore;
    }

    public Date getActiveAfter() {
        return activeAfter;
    }

    public void setActiveAfter(Date activeAfter) {
        this.activeAfter = activeAfter;
    }

    public RecordStatus getRoleStatus() {
        return roleStatus;
    }

    public void setRoleStatus(RecordStatus roleStatus) {
        this.roleStatus = roleStatus;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

}
