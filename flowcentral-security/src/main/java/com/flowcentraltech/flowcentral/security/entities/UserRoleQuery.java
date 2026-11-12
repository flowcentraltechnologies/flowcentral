/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntityQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.OrBuilder;
import com.tcdng.unify.core.util.CalendarUtils;

/**
 * Query class for user roles.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class UserRoleQuery extends BaseAuditEntityQuery<UserRole> {

    public UserRoleQuery() {
        super(UserRole.class);
    }

    public UserRoleQuery userId(Long userId) {
        return (UserRoleQuery) equals("userId", userId);
    }

    public UserRoleQuery userIdIn(Collection<Long> userId) {
        return (UserRoleQuery) amongst("userId", userId);
    }

    public UserRoleQuery userLoginId(String userLoginId) {
        return (UserRoleQuery) equals("userLoginId", userLoginId);
    }

    public UserRoleQuery branchId(Long branchId) {
        return (UserRoleQuery) equals("branchId", branchId);
    }

    public UserRoleQuery userStatus(RecordStatus userStatus) {
        return (UserRoleQuery) equals("userStatus", userStatus);
    }

    public UserRoleQuery userIsOriginal() {
        return (UserRoleQuery) isnull("userOriginalCopyId");
    }

    public UserRoleQuery isSupervisor() {
        return (UserRoleQuery) equals("supervisor", Boolean.TRUE);
    }

    public UserRoleQuery isNotSupervisor() {
        return (UserRoleQuery) equals("supervisor", Boolean.FALSE);
    }

    public UserRoleQuery roleId(Long roleId) {
        return (UserRoleQuery) equals("roleId", roleId);
    }

    public UserRoleQuery roleIdNot(Long roleId) {
        return (UserRoleQuery) notequals("roleId", roleId);
    }

    public UserRoleQuery roleCode(String roleCode) {
        return (UserRoleQuery) equals("roleCode", roleCode);
    }

    public UserRoleQuery roleCodeNot(String roleCode) {
        return (UserRoleQuery) notequals("roleCode", roleCode);
    }

    public UserRoleQuery roleCodeIn(Collection<String> roleCode) {
        return (UserRoleQuery) amongst("roleCode", roleCode);
    }

    public UserRoleQuery roleIdIn(Collection<Long> roleId) {
        return (UserRoleQuery) amongst("roleId", roleId);
    }

    public UserRoleQuery roleStatus(RecordStatus roleStatus) {
        return (UserRoleQuery) equals("roleStatus", roleStatus);
    }

    public UserRoleQuery roleIsOriginal() {
        return (UserRoleQuery) isnull("roleOriginalCopyId");
    }

    public UserRoleQuery departmentId(Long departmentId) {
        return (UserRoleQuery) equals("departmentId", departmentId);
    }

    public UserRoleQuery roleActiveTime(Date date) throws UnifyException {
        date = CalendarUtils.getTimeOfDay(date);
        return (UserRoleQuery) restriction(
                new OrBuilder().greatereq("activeBefore", date).isnull("activeBefore").build())
                        .restriction(new OrBuilder().lesseq("activeAfter", date).isnull("activeAfter").build());
    }
}
