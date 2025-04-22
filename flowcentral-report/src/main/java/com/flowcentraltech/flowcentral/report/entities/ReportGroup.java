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
package com.flowcentraltech.flowcentral.report.entities;

import java.util.List;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;

/**
 * Entity for storing report group information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_REPORTGROUP",
        uniqueConstraints = { @UniqueConstraint({ "name" }), @UniqueConstraint({ "description" }) })
public class ReportGroup extends BaseStatusEntity {

    @Column(name = "REPORTGROUP_NM", length = 64)
    private String name;

    @Column(name = "REPORTGROUP_DESC", length = 96)
    private String description;

    @Column(length = 64, nullable = true)
    private String label;

    @ChildList
    private List<ReportGroupRole> reportGroupRoleList;

    @ChildList
    private List<ReportGroupMember> reportGroupMemberList;

    @Override
    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ReportGroupRole> getReportGroupRoleList() {
        return reportGroupRoleList;
    }

    public void setReportGroupRoleList(List<ReportGroupRole> reportGroupRoleList) {
        this.reportGroupRoleList = reportGroupRoleList;
    }

    public List<ReportGroupMember> getReportGroupMemberList() {
        return reportGroupMemberList;
    }

    public void setReportGroupMemberList(List<ReportGroupMember> reportGroupMemberList) {
        this.reportGroupMemberList = reportGroupMemberList;
    }

}
