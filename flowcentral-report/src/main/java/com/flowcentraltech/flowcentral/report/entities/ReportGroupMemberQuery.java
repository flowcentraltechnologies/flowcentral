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
package com.flowcentraltech.flowcentral.report.entities;

import java.util.Collection;

import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntityQuery;

/**
 * Query class for report group members.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ReportGroupMemberQuery extends BaseAuditEntityQuery<ReportGroupMember> {

    public ReportGroupMemberQuery() {
        super(ReportGroupMember.class);
    }
    
    public ReportGroupMemberQuery reportGroupId(Long reportGroupId) {
        return (ReportGroupMemberQuery) addEquals("reportGroupId", reportGroupId);
    }

    public ReportGroupMemberQuery reportGroupName(String reportGroupName) {
        return (ReportGroupMemberQuery) addEquals("reportGroupName", reportGroupName);
    }

    public ReportGroupMemberQuery ueportGroupIdIn(Collection<Long> reportGroupId) {
        return (ReportGroupMemberQuery) addAmongst("reportGroupId", reportGroupId);
    }

    public ReportGroupMemberQuery applicationId(Long applicationId) {
        return (ReportGroupMemberQuery) addEquals("applicationId", applicationId);
    }

    public ReportGroupMemberQuery reportConfigurationId(Long reportConfigurationId) {
        return (ReportGroupMemberQuery) addEquals("reportConfigurationId", reportConfigurationId);
    }

    public ReportGroupMemberQuery configType(ConfigType configType) {
        return (ReportGroupMemberQuery) addEquals("configType", configType);
    }

    public ReportGroupMemberQuery isCustom() {
        return (ReportGroupMemberQuery) addEquals("configType", ConfigType.CUSTOM);
    }
}
