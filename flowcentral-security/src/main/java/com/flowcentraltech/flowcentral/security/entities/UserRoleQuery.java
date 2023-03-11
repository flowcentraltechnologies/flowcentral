/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

import java.util.Collection;
import java.util.Date;

import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditTenantEntityQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.OrBuilder;
import com.tcdng.unify.core.util.CalendarUtils;

/**
 * Query class for user roles.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class UserRoleQuery extends BaseAuditTenantEntityQuery<UserRole> {

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

    public UserRoleQuery branchId(Long branchId) {
        return (UserRoleQuery) addEquals("branchId", branchId);
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

    public UserRoleQuery departmentId(Long departmentId) {
        return (UserRoleQuery) addEquals("departmentId", departmentId);
    }

    public UserRoleQuery roleActiveTime(Date date) throws UnifyException {
        date = CalendarUtils.getTimeOfDay(date);
        return (UserRoleQuery) addRestriction(new OrBuilder().less("activeBefore", date).isNull("activeBefore").build())
                .addRestriction(new OrBuilder().greater("activeAfter", date).isNull("activeAfter").build());
    }
}
