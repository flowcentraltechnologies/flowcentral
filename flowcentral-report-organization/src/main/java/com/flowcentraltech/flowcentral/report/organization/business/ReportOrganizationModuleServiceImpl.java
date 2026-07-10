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
package com.flowcentraltech.flowcentral.report.organization.business;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.flowcentraltech.flowcentral.application.business.AbstractApplicationArtifactInstaller.DeletionParams;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.business.RolePrivilegeBackupAgent;
import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.report.business.ReportModuleExtProvider;
import com.flowcentraltech.flowcentral.report.entities.ReportConfiguration;
import com.flowcentraltech.flowcentral.report.entities.ReportConfigurationQuery;
import com.flowcentraltech.flowcentral.report.organization.constants.ReportOrganizationModuleNameConstants;
import com.flowcentraltech.flowcentral.report.organization.entities.ReportGroup;
import com.flowcentraltech.flowcentral.report.organization.entities.ReportGroupMember;
import com.flowcentraltech.flowcentral.report.organization.entities.ReportGroupMemberQuery;
import com.flowcentraltech.flowcentral.report.organization.entities.ReportGroupQuery;
import com.flowcentraltech.flowcentral.report.organization.entities.ReportGroupRoleQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.application.InstallationContext;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Implementation of report organization module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Transactional
@Component(ReportOrganizationModuleNameConstants.REPORT_ORGANIZATION_MODULE_SERVICE)
public class ReportOrganizationModuleServiceImpl extends AbstractFlowCentralService
        implements ReportOrganizationModuleService, RolePrivilegeBackupAgent, ReportModuleExtProvider {

    private final Map<String, Set<ReportInfo>> groupMemberBackup;

    public ReportOrganizationModuleServiceImpl() {
        this.groupMemberBackup = new ConcurrentHashMap<String, Set<ReportInfo>>();
    }

    @Override
    public void deleteApplicationArtifacts(TaskMonitor taskMonitor, DeletionParams deletionParams, Long applicationId,
            boolean customOnly) throws UnifyException {
        environment().deleteAll(customOnly ? new ReportGroupMemberQuery().applicationId(applicationId).isCustom()
                : new ReportGroupMemberQuery().applicationId(applicationId));
    }

    @Override
    public void clearDefinitionsCache() throws UnifyException {

    }

    @Override
    public void unregisterApplicationRolePrivileges(Long applicationId) throws UnifyException {
        environment().deleteAll(new ReportGroupMemberQuery().applicationId(applicationId));
    }

    @Override
    public void unregisterCustomApplicationRolePrivileges(Long applicationId) throws UnifyException {
        environment().deleteAll(new ReportGroupMemberQuery().applicationId(applicationId).isCustom());
    }

    @Override
    public void backupApplicationRolePrivileges(Long applicationId) throws UnifyException {
        List<ReportGroupMember> groupMemberList = environment()
                .listAll(new ReportGroupMemberQuery().applicationId(applicationId).addSelect("reportGroupName",
                        "reportConfigurationName", "applicationName"));
        for (ReportGroupMember reportGroupMember : groupMemberList) {
            Set<ReportInfo> reportConfigs = groupMemberBackup.get(reportGroupMember.getReportGroupName());
            if (reportConfigs == null) {
                reportConfigs = new HashSet<ReportInfo>();
                groupMemberBackup.put(reportGroupMember.getReportGroupName(), reportConfigs);
            }

            reportConfigs.add(new ReportInfo(reportGroupMember.getApplicationName(),
                    reportGroupMember.getReportConfigurationName()));
        }
    }

    @Override
    public void restoreApplicationRolePrivileges() throws UnifyException {
        ReportGroupMember reportGroupMember = new ReportGroupMember();
        for (Map.Entry<String, Set<ReportInfo>> entry : groupMemberBackup.entrySet()) {
            final Long reportGroupId = environment().value(Long.class, "id",
                    new ReportGroupQuery().name(entry.getKey()));
            for (ReportInfo reportInfo : entry.getValue()) {
                Optional<Long> reportConfigurationId = environment().valueOptional(Long.class, "id",
                        new ReportConfigurationQuery().applicationName(reportInfo.getApplicationName())
                                .name(reportInfo.getReportName()));
                if (reportConfigurationId.isPresent() && environment().countAll(new ReportGroupMemberQuery()
                        .reportGroupId(reportGroupId).reportConfigurationId(reportConfigurationId.get())) == 0) {
                    reportGroupMember.setId(null);
                    reportGroupMember.setReportGroupId(reportGroupId);
                    reportGroupMember.setReportConfigurationId(reportConfigurationId.get());
                    environment().create(reportGroupMember);
                }
            }
        }
    }

    @Override
    public List<ReportGroup> findReportGroupsByRole(String roleCode) throws UnifyException {
        if (StringUtils.isBlank(roleCode)) {
            return environment().findAll(new ReportGroupQuery().status(RecordStatus.ACTIVE).addOrder("label"));
        }

        List<Long> reportGroupIdList = environment().valueList(Long.class, "reportGroupId",
                new ReportGroupRoleQuery().roleCode(roleCode));
        if (!DataUtils.isBlank(reportGroupIdList)) {
            return environment().findAll(
                    new ReportGroupQuery().status(RecordStatus.ACTIVE).idIn(reportGroupIdList).addOrder("label"));
        }

        return Collections.emptyList();
    }

    @Override
    public List<ReportConfiguration> findReportConfigurationsByGroup(Long reportGroupId) throws UnifyException {
        List<Long> reportConfigurationIdList = environment().valueList(Long.class, "reportConfigurationId",
                new ReportGroupMemberQuery().reportGroupId(reportGroupId));
        if (!DataUtils.isBlank(reportConfigurationIdList)) {
            return environment().listAll(new ReportConfigurationQuery().isNotDeprecated()
                    .idIn(reportConfigurationIdList).addOrder("description"));
        }

        return Collections.emptyList();
    }


    @Override
    protected void doInstallModuleFeatures(final InstallationContext ctx, ModuleInstall moduleInstall)
            throws UnifyException {

    }

    private class ReportInfo {

        private String applicationName;

        private String reportName;

        public ReportInfo(String applicationName, String reportName) {
            this.applicationName = applicationName;
            this.reportName = reportName;
        }

        public String getApplicationName() {
            return applicationName;
        }

        public String getReportName() {
            return reportName;
        }
    }

}
