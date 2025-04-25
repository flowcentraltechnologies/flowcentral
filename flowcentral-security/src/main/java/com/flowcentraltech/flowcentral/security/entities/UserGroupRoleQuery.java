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
package com.flowcentraltech.flowcentral.security.entities;

import java.util.Collection;
import java.util.Date;

import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntityQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.OrBuilder;
import com.tcdng.unify.core.util.CalendarUtils;

/**
 * Query class for user group roles.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class UserGroupRoleQuery extends BaseAuditEntityQuery<UserGroupRole> {

    public UserGroupRoleQuery() {
        super(UserGroupRole.class);
    }

    public UserGroupRoleQuery userGroupId(Long userGroupId) {
        return (UserGroupRoleQuery) addEquals("userGroupId", userGroupId);
    }

    public UserGroupRoleQuery userGroupIdIn(Collection<Long> userGroupId) {
        return (UserGroupRoleQuery) addAmongst("userGroupId", userGroupId);
    }

    public UserGroupRoleQuery roleId(Long roleId) {
        return (UserGroupRoleQuery) addEquals("roleId", roleId);
    }

    public UserGroupRoleQuery roleIdNot(Long roleId) {
        return (UserGroupRoleQuery) addNotEquals("roleId", roleId);
    }

    public UserGroupRoleQuery roleCode(String roleCode) {
        return (UserGroupRoleQuery) addEquals("roleCode", roleCode);
    }

    public UserGroupRoleQuery roleCodeNot(String roleCode) {
        return (UserGroupRoleQuery) addNotEquals("roleCode", roleCode);
    }

    public UserGroupRoleQuery roleStatus(RecordStatus roleStatus) {
        return (UserGroupRoleQuery) addEquals("roleStatus", roleStatus);
    }

    public UserGroupRoleQuery roleCodeIn(Collection<String> roleCode) {
        return (UserGroupRoleQuery) addAmongst("roleCode", roleCode);
    }

    public UserGroupRoleQuery roleCodeNotIn(Collection<String> roleCode) {
        return (UserGroupRoleQuery) addNotAmongst("roleCode", roleCode);
    }

    public UserGroupRoleQuery roleActiveTime(Date date) throws UnifyException {
        date = CalendarUtils.getTimeOfDay(date);
        return (UserGroupRoleQuery) addRestriction(
                new OrBuilder().less("activeBefore", date).isNull("activeBefore").build())
                        .addRestriction(new OrBuilder().greater("activeAfter", date).isNull("activeAfter").build());
    }
}
