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

package com.flowcentraltech.flowcentral.codegeneration.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.configuration.xml.AppDashboardConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppDashboardsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.DashboardOptionCategoryBaseConfig;
import com.flowcentraltech.flowcentral.configuration.xml.DashboardOptionConfig;
import com.flowcentraltech.flowcentral.configuration.xml.DashboardOptionsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.DashboardSectionConfig;
import com.flowcentraltech.flowcentral.configuration.xml.DashboardSectionsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.DashboardTileConfig;
import com.flowcentraltech.flowcentral.configuration.xml.DashboardTilesConfig;
import com.flowcentraltech.flowcentral.dashboard.business.DashboardModuleService;
import com.flowcentraltech.flowcentral.dashboard.entities.Dashboard;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardOption;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardOptionCategoryBase;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardSection;
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
public class DashboardsXmlGenerator extends AbstractStaticModuleArtifactGenerator {

    @Configurable
    private DashboardModuleService dashboardModuleService;

    @Override
    protected void doGenerate(ExtensionModuleStaticFileBuilderContext ctx, String applicationName, ZipOutputStream out)
            throws UnifyException {
        List<Long> dashboardIdList = dashboardModuleService.findCustomDashboardIdList(applicationName);
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

                // Sections
                if (!DataUtils.isBlank(dashboard.getSectionList())) {
                    List<DashboardSectionConfig> sectionList = new ArrayList<DashboardSectionConfig>();
                    for (DashboardSection dashboardSection : dashboard.getSectionList()) {
                        DashboardSectionConfig dashboardSectionConfig = new DashboardSectionConfig();
                        dashboardSectionConfig.setType(dashboardSection.getType());
                        dashboardSectionConfig.setIndex(dashboardSection.getIndex());
                        dashboardSectionConfig.setHeight(dashboardSection.getHeight());
                        sectionList.add(dashboardSectionConfig);
                    }

                    appDashboardConfig.setSectionList(new DashboardSectionsConfig(sectionList));
                }

                // Tiles
                if (!DataUtils.isBlank(dashboard.getTileList())) {
                    List<DashboardTileConfig> tileList = new ArrayList<DashboardTileConfig>();
                    for (DashboardTile dashboardTile : dashboard.getTileList()) {
                        DashboardTileConfig dashboardTileConfig = new DashboardTileConfig();
                        descKey = getDescriptionKey(lowerCaseApplicationName, "dashboardtile",
                                dashboardTile.getDescription());
                        ctx.addMessage(StaticMessageCategoryType.DASHBOARD, descKey, dashboardTile.getDescription());

                        dashboardTileConfig.setType(dashboardTile.getType());
                        dashboardTileConfig.setChart(dashboardTile.getChart());
                        dashboardTileConfig.setName(dashboardTile.getName());
                        dashboardTileConfig.setDescription("$m{" + descKey + "}");
                        dashboardTileConfig.setSection(dashboardTile.getSection());
                        dashboardTileConfig.setIndex(dashboardTile.getIndex());
                        tileList.add(dashboardTileConfig);
                    }

                    appDashboardConfig.setTiles(new DashboardTilesConfig(tileList));
                }

                // Options
                if (!DataUtils.isBlank(dashboard.getOptionsList())) {
                    List<DashboardOptionConfig> optionsList = new ArrayList<DashboardOptionConfig>();
                    for (DashboardOption dashboardOption : dashboard.getOptionsList()) {
                        DashboardOptionConfig dashboardOptionConfig = new DashboardOptionConfig();
                        descKey = getDescriptionKey(lowerCaseApplicationName, "dashboardoption",
                                dashboardOption.getDescription());
                        String labelKey = getDescriptionKey(lowerCaseApplicationName, "dashboardoption",
                                dashboardOption.getLabel());
                        ctx.addMessage(StaticMessageCategoryType.DASHBOARD, descKey, dashboardOption.getDescription());
                        ctx.addMessage(StaticMessageCategoryType.DASHBOARD, labelKey, dashboardOption.getLabel());

                        dashboardOptionConfig.setName(dashboardOption.getName());
                        dashboardOptionConfig.setDescription("$m{" + descKey + "}");
                        dashboardOptionConfig.setLabel("$m{" + labelKey + "}");

                        if (!DataUtils.isBlank(dashboardOption.getBaseList())) {
                            List<DashboardOptionCategoryBaseConfig> baseList = new ArrayList<DashboardOptionCategoryBaseConfig>();
                            for (DashboardOptionCategoryBase base : dashboardOption.getBaseList()) {
                                DashboardOptionCategoryBaseConfig dashboardOptionCategoryBaseConfig = new DashboardOptionCategoryBaseConfig();
                                dashboardOptionCategoryBaseConfig.setChartDataSource(base.getChartDataSource());
                                dashboardOptionCategoryBaseConfig.setEntity(base.getEntity());
                                dashboardOptionCategoryBaseConfig.setCategoryBase(
                                        InputWidgetUtils.getFilterConfig(au(), base.getCategoryBase()));
                                baseList.add(dashboardOptionCategoryBaseConfig);
                            }

                            dashboardOptionConfig.setBaseList(baseList);
                        }

                        optionsList.add(dashboardOptionConfig);
                    }

                    appDashboardConfig.setOptions(new DashboardOptionsConfig(optionsList));
                }

                dashboardConfigList.add(appDashboardConfig);
            }

            appDashboardsConfig.setDashboardList(dashboardConfigList);
            ctx.setDashboardsConfig(appDashboardsConfig);
        }

    }

}
