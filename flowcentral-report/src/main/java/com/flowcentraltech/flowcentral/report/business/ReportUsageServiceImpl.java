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
package com.flowcentraltech.flowcentral.report.business;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.UsageProvider;
import com.flowcentraltech.flowcentral.application.data.Usage;
import com.flowcentraltech.flowcentral.application.data.UsageType;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.report.constants.ReportModuleNameConstants;
import com.flowcentraltech.flowcentral.report.entities.ReportConfiguration;
import com.flowcentraltech.flowcentral.report.entities.ReportConfigurationQuery;
import com.flowcentraltech.flowcentral.report.entities.ReportableDefinition;
import com.flowcentraltech.flowcentral.report.entities.ReportableDefinitionQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Transactional;

/**
 * Default implementation of report usage service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(ReportModuleNameConstants.REPORT_USAGE_SERVICE)
public class ReportUsageServiceImpl extends AbstractFlowCentralService implements UsageProvider {

    @Override
    public List<Usage> findApplicationUsagesByOtherApplications(String applicationName, UsageType usageType)
            throws UnifyException {
        final String applicationNameBase = applicationName + '.';
        List<Usage> usageList = new ArrayList<Usage>();
        if (UsageType.isQualifiesEntity(usageType)) {
            List<ReportableDefinition> reportableDefinitionList = environment()
                    .listAll(new ReportableDefinitionQuery().applicationNameNot(applicationName)
                            .entityBeginsWith(applicationNameBase).addSelect("applicationName", "name", "entity"));
            for (ReportableDefinition reportableDefinition : reportableDefinitionList) {
                Usage usage = new Usage(UsageType.ENTITY, "ReportableDefinition",
                        reportableDefinition.getApplicationName() + "." + reportableDefinition.getName(), "entity",
                        reportableDefinition.getEntity());
                usageList.add(usage);
            }

            List<ReportConfiguration> reportConfigurationList = environment().listAll(new ReportConfigurationQuery()
                    .applicationNameNot(applicationName).reportableBeginsWith(applicationNameBase)
                    .addSelect("applicationName", "name", "reportable"));
            for (ReportConfiguration reportConfiguration : reportConfigurationList) {
                Usage usage = new Usage(UsageType.ENTITY, "ReportConfiguration",
                        reportConfiguration.getApplicationName() + "." + reportConfiguration.getName(), "reportable",
                        reportConfiguration.getReportable());
                usageList.add(usage);
            }

        }

        return usageList;
    }

    @Override
    public long countApplicationUsagesByOtherApplications(String applicationName, UsageType usageType)
            throws UnifyException {
        final String applicationNameBase = applicationName + '.';
        long usages = 0L;
        if (UsageType.isQualifiesEntity(usageType)) {
            usages += environment().countAll(new ReportableDefinitionQuery().applicationNameNot(applicationName)
                    .entityBeginsWith(applicationNameBase).addSelect("applicationName", "name", "entity"));

            usages += environment().countAll(new ReportConfigurationQuery().applicationNameNot(applicationName)
                    .reportableBeginsWith(applicationNameBase).addSelect("applicationName", "name", "reportable"));
        }

        return usages;
    }

    @Override
    public List<Usage> findEntityUsages(String entity, UsageType usageType) throws UnifyException {
        List<Usage> usageList = new ArrayList<Usage>();
        if (UsageType.isQualifiesEntity(usageType)) {
            List<ReportableDefinition> reportableDefinitionList = environment().listAll(
                    new ReportableDefinitionQuery().entity(entity).addSelect("applicationName", "name", "entity"));
            for (ReportableDefinition reportableDefinition : reportableDefinitionList) {
                Usage usage = new Usage(UsageType.ENTITY, "ReportableDefinition",
                        reportableDefinition.getApplicationName() + "." + reportableDefinition.getName(), "entity",
                        reportableDefinition.getEntity());
                usageList.add(usage);
            }
        }

        return usageList;
    }

    @Override
    public long countEntityUsages(String entity, UsageType usageType) throws UnifyException {
        long usages = 0L;
        if (UsageType.isQualifiesEntity(usageType)) {
            usages += environment().countAll(
                    new ReportableDefinitionQuery().entity(entity).addSelect("applicationName", "name", "entity"));
        }

        return usages;
    }

    @Override
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {

    }
}
