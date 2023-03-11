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

package com.flowcentraltech.flowcentral.codegeneration.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import com.flowcentraltech.flowcentral.configuration.xml.AppDashboardConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppDashboardsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.DashboardTileConfig;
import com.flowcentraltech.flowcentral.dashboard.business.DashboardModuleService;
import com.flowcentraltech.flowcentral.dashboard.entities.Dashboard;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardTile;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Dashboards XML Generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("dashboards-xml-generator")
public class DashboardsXmlGenerator extends AbstractStaticArtifactGenerator {

    @Configurable
    private DashboardModuleService dashboardModuleService;

    public void setDashboardModuleService(DashboardModuleService dashboardModuleService) {
        this.dashboardModuleService = dashboardModuleService;
    }

    @Override
    protected void doGenerate(ExtensionModuleStaticFileBuilderContext ctx, String applicationName, ZipOutputStream out)
            throws UnifyException {
        List<Long> dashboardIdList = dashboardModuleService.findDashboardIdList(applicationName);
        if (!DataUtils.isBlank(dashboardIdList)) {
            final String lowerCaseApplicationName = applicationName.toLowerCase();

            AppDashboardsConfig appDashboardsConfig = new AppDashboardsConfig();
            List<AppDashboardConfig> dashboardConfigList = new ArrayList<AppDashboardConfig>();
            for (Long dashboardId : dashboardIdList) {
                AppDashboardConfig appDashboardConfig = new AppDashboardConfig();
                Dashboard dashboard = dashboardModuleService.findDashboard(dashboardId);
                String descKey = getDescriptionKey(lowerCaseApplicationName, "dashboard", dashboard.getName());
                ctx.addMessage(StaticMessageCategoryType.DASHBOARD, descKey, dashboard.getDescription());

                appDashboardConfig.setName(dashboard.getName());
                appDashboardConfig.setDescription("$m{" + descKey + "}");
                appDashboardConfig.setSections(dashboard.getSections());
                appDashboardConfig.setAllowSecondaryTenants(dashboard.isAllowSecondaryTenants());

                // Tiles
                if (!DataUtils.isBlank(dashboard.getTileList())) {
                    List<DashboardTileConfig> tileList = new ArrayList<DashboardTileConfig>();
                    for (DashboardTile dashboardTile : dashboard.getTileList()) {
                        DashboardTileConfig dashboardTileConfig = new DashboardTileConfig();
                        descKey = getDescriptionKey(lowerCaseApplicationName, "dashboardtile", dashboardTile.getName());
                        ctx.addMessage(StaticMessageCategoryType.DASHBOARD, descKey, dashboardTile.getDescription());
                        
                        dashboardTileConfig.setType(dashboardTile.getType());
                        dashboardTileConfig.setChart(dashboardTile.getChart());
                        dashboardTileConfig.setName(dashboardTile.getName());
                        dashboardTileConfig.setDescription("$m{" + descKey + "}");
                        dashboardTileConfig.setSection(dashboardTile.getSection());
                        dashboardTileConfig.setIndex(dashboardTile.getIndex());
                        tileList.add(dashboardTileConfig);
                    }
                    
                    appDashboardConfig.setTileList(tileList);
                }

                dashboardConfigList.add(appDashboardConfig);
            }

            appDashboardsConfig.setDashboardList(dashboardConfigList);
            ctx.setDashboardsConfig(appDashboardsConfig);
        }

    }

}
