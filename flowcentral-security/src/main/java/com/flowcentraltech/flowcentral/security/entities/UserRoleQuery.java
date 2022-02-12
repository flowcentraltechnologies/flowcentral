/*
 * Copyright (c) 2019, 2021, FlowCentral Technologies.
 * All rights reserved.
 * 
 * PROPRIETARY AND CONFIDENTIAL. USE IS SUBJECT TO LICENSE TERMS.
 */
package com.flowcentraltech.flowcentral.security.entities;

import java.util.Collection;
import java.util.Date;

import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntityQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.OrBuilder;
import com.tcdng.unify.core.util.CalendarUtils;

/**
 * Query class for user roles.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class UserRoleQuery extends BaseAuditEntityQuery<UserRole> {

    public UserRoleQuery() {
        super(UserRole.class);
    }

    public UserRoleQuery userId(Long userId) {
        return (UserRoleQuery) addEquals("userId", userId);
    }

    public UserRoleQuery userIdIn(Collection<Long> userId) {
        return (UserRoleQuery) addAmongst("userId", userId);
    }

    public UserRoleQuery userLoginId(String userLoginId) {
        return (UserRoleQuery) addEquals("userLoginId", userLoginId);
    }

    public UserRoleQuery branchCode(String branchCode) {
        return (UserRoleQuery) addEquals("branchCode", branchCode);
    }

    public UserRoleQuery userStatus(RecordStatus userStatus) {
        return (UserRoleQuery) addEquals("userStatus", userStatus);
    }

    public UserRoleQuery isSupervisor() {
        return (UserRoleQuery) addEquals("supervisor", Boolean.TRUE);
    }

    public UserRoleQuery isNotSupervisor() {
        return (UserRoleQuery) addEquals("supervisor", Boolean.FALSE);
    }

    public UserRoleQuery roleId(Long roleId) {
        return (UserRoleQuery) addEquals("roleId", roleId);
    }

    public UserRoleQuery roleIdNot(Long roleId) {
        return (UserRoleQuery) addNotEquals("roleId", roleId);
    }

    public UserRoleQuery roleCode(String roleCode) {
        return (UserRoleQuery) addEquals("roleCode", roleCode);
    }

    public UserRoleQuery roleCodeNot(String roleCode) {
        return (UserRoleQuery) addNotEquals("roleCode", roleCode);
    }

    public UserRoleQuery roleCodeIn(Collection<String> roleCode) {
        return (UserRoleQuery) addAmongst("roleCode", roleCode);
    }

    public UserRoleQuery roleIdIn(Collection<Long> roleId) {
        return (UserRoleQuery) addAmongst("roleId", roleId);
    }

    public UserRoleQuery roleStatus(RecordStatus roleStatus) {
        return (UserRoleQuery) addEquals("roleStatus", roleStatus);
    }

    public UserRoleQuery departmentName(String departmentName) {
        return (UserRoleQuery) addEquals("departmentName", departmentName);
    }

    public UserRoleQuery roleActiveTime(Date date) throws UnifyException {
        date = CalendarUtils.getTimeOfDay(date);
        return (UserRoleQuery) addRestriction(new OrBuilder().less("activeBefore", date).isNull("activeBefore").build())
                .addRestriction(new OrBuilder().greater("activeAfter", date).isNull("activeAfter").build());
    }
}
