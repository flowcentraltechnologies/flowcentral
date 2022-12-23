/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.security.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.flowcentraltech.flowcentral.organization.entities.Role;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;
import com.tcdng.unify.core.data.Describable;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Entity for storing user group role information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_USERGROUPROLE", uniqueConstraints = { @UniqueConstraint({ "userGroupId", "roleId" }) })
public class UserGroupRole extends BaseAuditEntity implements Describable {

    @ForeignKey(UserGroup.class)
    private Long userGroupId;

    @ForeignKey(Role.class)
    private Long roleId;

    @ListOnly(key = "userGroupId", property = "name")
    private String userGroupName;

    @ListOnly(key = "userGroupId", property = "description")
    private String userGroupDesc;

    @ListOnly(key = "userGroupId", property = "email")
    private String userGroupEmail;

    @ListOnly(key = "userGroupId", property = "tenantId")
    private Long tenantId;

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

    @ListOnly(key = "roleId", property = "departmentCode")
    private String departmentCode;

    @ListOnly(key = "roleId", property = "departmentDesc")
    private String departmentDesc;

    @Override
    public String getDescription() {
        return StringUtils.concatenate(userGroupDesc, " - ", roleDesc);
    }

    public Long getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Long userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }

    public String getUserGroupDesc() {
        return userGroupDesc;
    }

    public void setUserGroupDesc(String userGroupDesc) {
        this.userGroupDesc = userGroupDesc;
    }

    public String getUserGroupEmail() {
        return userGroupEmail;
    }

    public void setUserGroupEmail(String userGroupEmail) {
        this.userGroupEmail = userGroupEmail;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
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

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentDesc() {
        return departmentDesc;
    }

    public void setDepartmentDesc(String departmentDesc) {
        this.departmentDesc = departmentDesc;
    }

}
