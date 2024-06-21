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

import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditTenantEntity;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;
import com.tcdng.unify.core.data.Describable;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Entity for storing report group member information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_REPORTGROUPMEMBER", uniqueConstraints = { @UniqueConstraint({ "reportGroupId", "reportConfigurationId" }) })
public class ReportGroupMember extends BaseAuditTenantEntity implements Describable {

    @ForeignKey(ReportGroup.class)
    private Long reportGroupId;

    @ForeignKey(ReportConfiguration.class)
    private Long reportConfigurationId;

    @ListOnly(key = "reportGroupId", property = "name")
    private String reportGroupName;

    @ListOnly(key = "reportGroupId", property = "description")
    private String reportGroupDesc;

    @ListOnly(key = "reportGroupId", property = "label")
    private String reportGroupLabel;

    @ListOnly(key = "reportConfigurationId", property = "applicationId")
    private Long applicationId;

    @ListOnly(key = "reportConfigurationId", property = "applicationName")
    private String applicationName;

    @ListOnly(key = "reportConfigurationId", property = "name")
    private String reportConfigurationName;

    @ListOnly(key = "reportConfigurationId", property = "description")
    private String reportConfigurationDesc;
    
    @ListOnly(key = "reportConfigurationId", property = "configType")
    private ConfigType configType;

    @Override
    public String getDescription() {
        return StringUtils.concatenate(reportGroupDesc, " - ", reportConfigurationDesc);
    }

    public Long getReportGroupId() {
        return reportGroupId;
    }

    public void setReportGroupId(Long reportGroupId) {
        this.reportGroupId = reportGroupId;
    }

    public Long getReportConfigurationId() {
        return reportConfigurationId;
    }

    public void setReportConfigurationId(Long reportConfigurationId) {
        this.reportConfigurationId = reportConfigurationId;
    }

    public String getReportGroupName() {
        return reportGroupName;
    }

    public void setReportGroupName(String reportGroupName) {
        this.reportGroupName = reportGroupName;
    }

    public String getReportGroupDesc() {
        return reportGroupDesc;
    }

    public void setReportGroupDesc(String reportGroupDesc) {
        this.reportGroupDesc = reportGroupDesc;
    }

    public String getReportGroupLabel() {
        return reportGroupLabel;
    }

    public void setReportGroupLabel(String reportGroupLabel) {
        this.reportGroupLabel = reportGroupLabel;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getReportConfigurationName() {
        return reportConfigurationName;
    }

    public void setReportConfigurationName(String reportConfigurationName) {
        this.reportConfigurationName = reportConfigurationName;
    }

    public String getReportConfigurationDesc() {
        return reportConfigurationDesc;
    }

    public void setReportConfigurationDesc(String reportConfigurationDesc) {
        this.reportConfigurationDesc = reportConfigurationDesc;
    }

    public ConfigType getConfigType() {
        return configType;
    }

    public void setConfigType(ConfigType configType) {
        this.configType = configType;
    }

}
