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
package com.flowcentraltech.flowcentral.report.organization.entities;

import java.util.Collection;
import java.util.Date;

import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntityQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.OrBuilder;
import com.tcdng.unify.core.util.CalendarUtils;

/**
 * Query class for report group roles.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ReportGroupRoleQuery extends BaseAuditEntityQuery<ReportGroupRole> {

    public ReportGroupRoleQuery() {
        super(ReportGroupRole.class);
    }

    public ReportGroupRoleQuery reportGroupId(Long reportGroupId) {
        return (ReportGroupRoleQuery) equals("reportGroupId", reportGroupId);
    }

    public ReportGroupRoleQuery reportGroupIdIn(Collection<Long> reportGroupId) {
        return (ReportGroupRoleQuery) amongst("reportGroupId", reportGroupId);
    }

    public ReportGroupRoleQuery roleId(Long roleId) {
        return (ReportGroupRoleQuery) equals("roleId", roleId);
    }

    public ReportGroupRoleQuery roleIdNot(Long roleId) {
        return (ReportGroupRoleQuery) notequals("roleId", roleId);
    }

    public ReportGroupRoleQuery roleCode(String roleCode) {
        return (ReportGroupRoleQuery) equals("roleCode", roleCode);
    }

    public ReportGroupRoleQuery roleCodeNot(String roleCode) {
        return (ReportGroupRoleQuery) notequals("roleCode", roleCode);
    }

    public ReportGroupRoleQuery roleStatus(RecordStatus roleStatus) {
        return (ReportGroupRoleQuery) equals("roleStatus", roleStatus);
    }

    public ReportGroupRoleQuery roleCodeIn(Collection<String> roleCode) {
        return (ReportGroupRoleQuery) amongst("roleCode", roleCode);
    }

    public ReportGroupRoleQuery roleCodeNotIn(Collection<String> roleCode) {
        return (ReportGroupRoleQuery) notamongst("roleCode", roleCode);
    }

    public ReportGroupRoleQuery roleActiveTime(Date date) throws UnifyException {
        date = CalendarUtils.getTimeOfDay(date);
        return (ReportGroupRoleQuery) restriction(
                new OrBuilder().less("activeBefore", date).isnull("activeBefore").build())
                        .restriction(new OrBuilder().greater("activeAfter", date).isnull("activeAfter").build());
    }
}
