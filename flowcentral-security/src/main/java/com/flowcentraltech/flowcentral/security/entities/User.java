/*
 * Copyright (c) 2019, 2021, FlowCentral Technologies.
 * All rights reserved.
 * 
 * PROPRIETARY AND CONFIDENTIAL. USE IS SUBJECT TO LICENSE TERMS.
 */
package com.flowcentraltech.flowcentral.security.entities;

import java.util.Date;
import java.util.List;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusWorkEntity;
import com.flowcentraltech.flowcentral.organization.entities.Branch;
import com.flowcentraltech.flowcentral.security.constants.UserWorkflowStatus;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Policy;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Entity for storing user information.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Policy("userpolicy")
@Table(name = "FC_USER", uniqueConstraints = { @UniqueConstraint({ "loginId" }) })
public class User extends BaseStatusWorkEntity {

    @ForeignKey(name = "WORKFLOW_STATUS")
    private UserWorkflowStatus workflowStatus;
    
    @ForeignKey(type = Branch.class, nullable = true)
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

    @ListOnly(key = "branchId", property = "code")
    private String branchCode;

    @ListOnly(key = "branchId", property = "description")
    private String branchDesc;

    @ListOnly(key = "branchId", property = "languageTag")
    private String branchLanguageTag;

    @ListOnly(key = "branchId", property = "timeZone")
    private String branchTimeZone;

    @ListOnly(key = "branchId", property = "zoneId")
    private Long zoneId;

    @ListOnly(key = "branchId", property = "zoneCode")
    private String zoneCode;

    @ListOnly(key = "branchId", property = "zoneDesc")
    private String zoneDesc;

    @ListOnly(key = "workflowStatus", property = "description")
    private String workflowStatusDesc;

    @ChildList
    private List<UserRole> userRoleList;

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

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchDesc() {
        return branchDesc;
    }

    public void setBranchDesc(String branchDesc) {
        this.branchDesc = branchDesc;
    }

    public String getBranchLanguageTag() {
        return branchLanguageTag;
    }

    public void setBranchLanguageTag(String branchLanguageTag) {
        this.branchLanguageTag = branchLanguageTag;
    }

    public String getBranchTimeZone() {
        return branchTimeZone;
    }

    public void setBranchTimeZone(String branchTimeZone) {
        this.branchTimeZone = branchTimeZone;
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

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getZoneDesc() {
        return zoneDesc;
    }

    public void setZoneDesc(String zoneDesc) {
        this.zoneDesc = zoneDesc;
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

}
