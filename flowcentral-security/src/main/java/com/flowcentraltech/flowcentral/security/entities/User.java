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
import java.util.List;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusWorkTenantEntity;
import com.flowcentraltech.flowcentral.security.constants.UserWorkflowStatus;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Mapped;
import com.tcdng.unify.core.annotation.Policy;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Entity for storing user information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Policy("userpolicy")
@Table(name = "FC_USER", uniqueConstraints = { @UniqueConstraint({ "loginId" }) })
public class User extends BaseStatusWorkTenantEntity {

    @ForeignKey(name = "WORKFLOW_STATUS")
    private UserWorkflowStatus workflowStatus;
    
    @Mapped("organization.mappedBranch")
    @Column(nullable = true)
    private Long branchId;

    @Column(length = 96)
    private String fullName;

    @Column(length = 64, transformer = "uppercase-transformer")
    private String loginId;

    @Column(length = 512, nullable = true)
    private String password;

    @Column(length = 64)
    private String email;

    @Column(length = 24, nullable = true)
    private String mobileNo;

    @Column
    private Integer loginAttempts;

    @Column(name = "LOGIN_LOCKED_FG")
    private Boolean loginLocked;

    @Column(name = "CHANGE_PASSWORD_FG")
    private Boolean changePassword;

    @Column(name = "PASSWORD_EXPIRES_FG")
    private Boolean passwordExpires;

    @Column(name = "ALLOW_MULTI_LOGIN_FG")
    private Boolean allowMultipleLogin;

    @Column(nullable = true)
    private Date passwordExpiryDt;

    @Column(type = ColumnType.TIMESTAMP_UTC, nullable = true)
    private Date lastLoginDt;

    @Column(name = "SUPERVISOR_FG")
    private Boolean supervisor;

    @ListOnly(key = "workflowStatus", property = "description")
    private String workflowStatusDesc;

    @ChildList
    private List<UserRole> userRoleList;

    @ChildList
    private List<UserLoginEvent> userLoginEventList;
    
    public User(Long id, String fullName, String loginId, String email, Boolean passwordExpires) {
        this.setId(id);
        this.fullName = fullName;
        this.loginId = loginId;
        this.email = email;
        this.passwordExpires = passwordExpires;
        this.supervisor = Boolean.FALSE;
    }

    public User() {

    }

    public UserWorkflowStatus getWorkflowStatus() {
        return workflowStatus;
    }

    public void setWorkflowStatus(UserWorkflowStatus workflowStatus) {
        this.workflowStatus = workflowStatus;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    @Override
    public String getDescription() {
        return fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Boolean getLoginLocked() {
        return loginLocked;
    }

    public void setLoginLocked(Boolean loginLocked) {
        this.loginLocked = loginLocked;
    }

    public Boolean getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(Boolean changePassword) {
        this.changePassword = changePassword;
    }

    public Integer getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(Integer loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public Boolean getPasswordExpires() {
        return passwordExpires;
    }

    public void setPasswordExpires(Boolean passwordExpires) {
        this.passwordExpires = passwordExpires;
    }

    public Date getPasswordExpiryDt() {
        return passwordExpiryDt;
    }

    public void setPasswordExpiryDt(Date passwordExpiryDt) {
        this.passwordExpiryDt = passwordExpiryDt;
    }

    public Date getLastLoginDt() {
        return lastLoginDt;
    }

    public void setLastLoginDt(Date lastLoginDt) {
        this.lastLoginDt = lastLoginDt;
    }

    public boolean isChangeUserPassword() {
        return Boolean.TRUE.equals(this.changePassword);
    }

    public Boolean getAllowMultipleLogin() {
        return allowMultipleLogin;
    }

    public void setAllowMultipleLogin(Boolean allowMultipleLogin) {
        this.allowMultipleLogin = allowMultipleLogin;
    }

    public Boolean getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Boolean supervisor) {
        this.supervisor = supervisor;
    }

    public String getWorkflowStatusDesc() {
        return workflowStatusDesc;
    }

    public void setWorkflowStatusDesc(String workflowStatusDesc) {
        this.workflowStatusDesc = workflowStatusDesc;
    }

    public List<UserRole> getUserRoleList() {
        return userRoleList;
    }

    public void setUserRoleList(List<UserRole> userRoleList) {
        this.userRoleList = userRoleList;
    }

    public List<UserLoginEvent> getUserLoginEventList() {
        return userLoginEventList;
    }

    public void setUserLoginEventList(List<UserLoginEvent> userLoginEventList) {
        this.userLoginEventList = userLoginEventList;
    }

}
