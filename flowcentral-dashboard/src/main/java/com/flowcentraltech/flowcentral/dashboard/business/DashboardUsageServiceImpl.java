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
package com.flowcentraltech.flowcentral.dashboard.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.UsageProvider;
import com.flowcentraltech.flowcentral.application.data.Usage;
import com.flowcentraltech.flowcentral.application.data.UsageType;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.dashboard.constants.DashboardModuleNameConstants;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardTile;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardTileQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Transactional;

/**
 * Default implementation of dashboard usage service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(DashboardModuleNameConstants.DASHBOARD_USAGE_SERVICE)
public class DashboardUsageServiceImpl extends AbstractFlowCentralService implements UsageProvider {

    @Override
    public List<Usage> findApplicationUsagesByOtherApplications(String applicationName, UsageType usageType)
            throws UnifyException {
        final String applicationNameBase = applicationName + '.';
        List<Usage> usageList = new ArrayList<Usage>();
        // Dashboard tile
        if (UsageType.isQualifiesEntity(usageType)) {
            List<DashboardTile> dashboardTileList = environment().listAll(
                    new DashboardTileQuery().applicationNameNot(applicationName).chartBeginsWith(applicationNameBase)
                            .addSelect("applicationName", "dashboardName", "name", "chart"));
            for (DashboardTile dashboardTile : dashboardTileList) {
                Usage usage = new Usage(
                        UsageType.ENTITY, "DashboardTile", dashboardTile.getApplicationName() + "."
                                + dashboardTile.getDashboardName() + "." + dashboardTile.getName(),
                        "chart", dashboardTile.getChart());
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
        // Dashboard tile
        if (UsageType.isQualifiesEntity(usageType)) {
            usages += environment().countAll(
                    new DashboardTileQuery().applicationNameNot(applicationName).chartBeginsWith(applicationNameBase)
                            .addSelect("applicationName", "dashboardName", "name", "chart"));
        }

        return usages;
    }

    @Override
    public List<Usage> findEntityUsages(String entity, UsageType usageType) throws UnifyException {
        return Collections.emptyList();
    }

    @Override
    public long countEntityUsages(String entity, UsageType usageType) throws UnifyException {
        return 0;
    }

    @Override
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {

    }
}
