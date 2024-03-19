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
package com.flowcentraltech.flowcentral.security.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditTenantEntity;
import com.flowcentraltech.flowcentral.organization.entities.Role;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;
import com.tcdng.unify.core.data.Describable;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Entity for storing user role information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_USERROLE", uniqueConstraints = { @UniqueConstraint({ "userId", "roleId" }) })
public class UserRole extends BaseAuditTenantEntity implements Describable {

    @ForeignKey(User.class)
    private Long userId;

    @ForeignKey(Role.class)
    private Long roleId;

    @ListOnly(key = "userId", property = "loginId")
    private String userLoginId;

    @ListOnly(key = "userId", property = "fullName")
    private String userName;

    @ListOnly(key = "userId", property = "email")
    private String userEmail;

    @ListOnly(key = "userId", property = "mobileNo")
    private String userMobileNo;

    @ListOnly(key = "userId", property = "status")
    private RecordStatus userStatus;

    @ListOnly(key = "userId", property = "branchId")
    private Long branchId;
    
    @ListOnly(key = "userId", property = "originalCopyId")
    private Long userOriginalCopyId;

    @ListOnly(name = "SUPERVISOR_FG", key = "userId", property = "supervisor")
    private Boolean supervisor;

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
    
    @ListOnly(key = "roleId", property = "originalCopyId")
    private Long roleOriginalCopyId;

    @Override
    public String getDescription() {
        return StringUtils.concatenate(userName, " - ", roleDesc);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobileNo() {
        return userMobileNo;
    }

    public void setUserMobileNo(String userMobileNo) {
        this.userMobileNo = userMobileNo;
    }

    public RecordStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(RecordStatus userStatus) {
        this.userStatus = userStatus;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Boolean getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Boolean supervisor) {
        this.supervisor = supervisor;
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

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Long getUserOriginalCopyId() {
        return userOriginalCopyId;
    }

    public void setUserOriginalCopyId(Long userOriginalCopyId) {
        this.userOriginalCopyId = userOriginalCopyId;
    }

    public Long getRoleOriginalCopyId() {
        return roleOriginalCopyId;
    }

    public void setRoleOriginalCopyId(Long roleOriginalCopyId) {
        this.roleOriginalCopyId = roleOriginalCopyId;
    }
}
