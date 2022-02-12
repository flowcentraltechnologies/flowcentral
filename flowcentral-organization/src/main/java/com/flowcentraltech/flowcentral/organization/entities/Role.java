/*
 * Copyright (c) 2019, 2021, FlowCentral Technologies.
 * All rights reserved.
 * 
 * PROPRIETARY AND CONFIDENTIAL. USE IS SUBJECT TO LICENSE TERMS.
 */
package com.flowcentraltech.flowcentral.organization.entities;

import java.util.Date;
import java.util.List;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntity;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Entity for storing role information.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "FC_ROLE", uniqueConstraints = { @UniqueConstraint({ "code" }), @UniqueConstraint({ "description" }) })
public class Role extends BaseStatusEntity {

    @ForeignKey(Department.class)
    private Long departmentId;

    @Column(name = "ROLE_CD", length = 16)
    private String code;

    @Column(name = "ROLE_DESC", length = 64)
    private String description;

    @Column(type = ColumnType.TIMESTAMP_UTC, transformer = "timeofday-transformer", nullable = true)
    private Date activeAfter;

    @Column(type = ColumnType.TIMESTAMP_UTC, transformer = "timeofday-transformer", nullable = true)
    private Date activeBefore;

    @Column(length = 64, nullable = true)
    private String email;

    @Column(name = "DASHBOARD_CD", length = 64, nullable = true)
    private String dashboardCode;

    @ListOnly(key = "departmentId", property = "code")
    private String departmentCode;

    @ListOnly(key = "departmentId", property = "description")
    private String departmentDesc;

    @ChildList
    private List<RolePrivilege> privilegeList;

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getActiveAfter() {
        return activeAfter;
    }

    public void setActiveAfter(Date activeAfter) {
        this.activeAfter = activeAfter;
    }

    public Date getActiveBefore() {
        return activeBefore;
    }

    public void setActiveBefore(Date activeBefore) {
        this.activeBefore = activeBefore;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getDashboardCode() {
        return dashboardCode;
    }

    public void setDashboardCode(String dashboardCode) {
        this.dashboardCode = dashboardCode;
    }

    public List<RolePrivilege> getPrivilegeList() {
        return privilegeList;
    }

    public void setPrivilegeList(List<RolePrivilege> privilegeList) {
        this.privilegeList = privilegeList;
    }
}
