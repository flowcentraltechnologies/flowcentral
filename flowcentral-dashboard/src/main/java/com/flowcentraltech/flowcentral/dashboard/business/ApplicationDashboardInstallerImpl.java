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

package com.flowcentraltech.flowcentral.dashboard.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.business.AbstractApplicationArtifactInstaller;
import com.flowcentraltech.flowcentral.application.constants.ApplicationPrivilegeConstants;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationReplicationContext;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.configuration.data.ApplicationInstall;
import com.flowcentraltech.flowcentral.configuration.data.ApplicationRestore;
import com.flowcentraltech.flowcentral.configuration.xml.AppConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppDashboardConfig;
import com.flowcentraltech.flowcentral.configuration.xml.DashboardOptionCategoryBaseConfig;
import com.flowcentraltech.flowcentral.configuration.xml.DashboardOptionConfig;
import com.flowcentraltech.flowcentral.configuration.xml.DashboardSectionConfig;
import com.flowcentraltech.flowcentral.configuration.xml.DashboardTileConfig;
import com.flowcentraltech.flowcentral.dashboard.constants.DashboardModuleNameConstants;
import com.flowcentraltech.flowcentral.dashboard.entities.Dashboard;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardOption;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardOptionCategoryBase;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardOptionQuery;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardQuery;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardSection;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardTile;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardTileQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Application dashboard installer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(DashboardModuleNameConstants.APPLICATION_DASHBOARD_INSTALLER)
public class ApplicationDashboardInstallerImpl extends AbstractApplicationArtifactInstaller {

    @Configurable
    private ApplicationPrivilegeManager applicationPrivilegeManager;

    @Override
    public void installApplicationArtifacts(final TaskMonitor taskMonitor, final ApplicationInstall applicationInstall)
            throws UnifyException {
        final AppConfig applicationConfig = applicationInstall.getApplicationConfig();
        final String applicationName = applicationConfig.getName();
        final Long applicationId = applicationInstall.getApplicationId();

        logDebug(taskMonitor, "Executing dashboard installer...");
        // Install configured dashboards
        environment().updateAll(new DashboardQuery().applicationId(applicationId).isStatic(),
                new Update().add("deprecated", Boolean.TRUE));
        if (applicationConfig.getDashboardsConfig() != null
                && !DataUtils.isBlank(applicationConfig.getDashboardsConfig().getDashboardList())) {
            for (AppDashboardConfig dashboardConfig : applicationConfig.getDashboardsConfig().getDashboardList()) {
                String description = resolveApplicationMessage(dashboardConfig.getDescription());
                logDebug(taskMonitor, "Installing dashboard [{0}]...", description);
                Dashboard oldDashboard = environment()
                        .findLean(new DashboardQuery().applicationId(applicationId).name(dashboardConfig.getName()));
                if (oldDashboard == null) {
                    Dashboard dashboard = new Dashboard();
                    dashboard.setApplicationId(applicationId);
                    dashboard.setName(dashboardConfig.getName());
                    dashboard.setDescription(description);
                    dashboard.setSections(dashboardConfig.getSections());
                    dashboard.setAllowSecondaryTenants(dashboardConfig.getAllowSecondaryTenants());
                    dashboard.setDeprecated(false);
                    dashboard.setConfigType(ConfigType.STATIC);
                    populateChildList(dashboardConfig, dashboard, applicationName, false);
                    environment().create(dashboard);
                } else {
                    oldDashboard.setDescription(description);
                    oldDashboard.setSections(dashboardConfig.getSections());
                    oldDashboard.setAllowSecondaryTenants(dashboardConfig.getAllowSecondaryTenants());
                    oldDashboard.setConfigType(ConfigType.STATIC);
                    oldDashboard.setDeprecated(false);
                    populateChildList(dashboardConfig, oldDashboard, applicationName, false);
                    environment().updateByIdVersion(oldDashboard);
                }

                applicationPrivilegeManager.registerPrivilege(ConfigType.STATIC, applicationId,
                        ApplicationPrivilegeConstants.APPLICATION_DASHBOARD_CATEGORY_CODE,
                        PrivilegeNameUtils.getDashboardPrivilegeName(ApplicationNameUtils
                                .getApplicationEntityLongName(applicationName, dashboardConfig.getName())),
                        description);
            }
        }
    }

    @Override
    public void restoreCustomApplicationArtifacts(TaskMonitor taskMonitor, ApplicationRestore applicationRestore)
            throws UnifyException {
        final AppConfig applicationConfig = applicationRestore.getApplicationConfig();
        final String applicationName = applicationConfig.getName();
        final Long applicationId = applicationRestore.getApplicationId();

        logDebug(taskMonitor, "Executing dashboard restore...");
        if (applicationConfig.getDashboardsConfig() != null
                && !DataUtils.isBlank(applicationConfig.getDashboardsConfig().getDashboardList())) {
            for (AppDashboardConfig dashboardConfig : applicationConfig.getDashboardsConfig().getDashboardList()) {
                String description = resolveApplicationMessage(dashboardConfig.getDescription());
                logDebug(taskMonitor, "Restoring dashboard [{0}]...", description);
                Dashboard dashboard = new Dashboard();
                dashboard.setApplicationId(applicationId);
                dashboard.setName(dashboardConfig.getName());
                dashboard.setDescription(description);
                dashboard.setSections(dashboardConfig.getSections());
                dashboard.setAllowSecondaryTenants(dashboardConfig.getAllowSecondaryTenants());
                dashboard.setDeprecated(false);
                dashboard.setConfigType(ConfigType.CUSTOM);
                populateChildList(dashboardConfig, dashboard, applicationName, true);
                environment().create(dashboard);

                applicationPrivilegeManager.registerPrivilege(ConfigType.CUSTOM, applicationId,
                        ApplicationPrivilegeConstants.APPLICATION_DASHBOARD_CATEGORY_CODE,
                        PrivilegeNameUtils.getDashboardPrivilegeName(ApplicationNameUtils
                                .getApplicationEntityLongName(applicationName, dashboardConfig.getName())),
                        description);
            }
        }
    }

    @Override
    public void replicateApplicationArtifacts(TaskMonitor taskMonitor, Long srcApplicationId, Long destApplicationId,
            ApplicationReplicationContext ctx) throws UnifyException {
        // Dashboards
        logDebug(taskMonitor, "Replicating dashboards...");
        List<Long> dashboardIdList = environment().valueList(Long.class, "id",
                new DashboardQuery().applicationId(srcApplicationId));
        for (Long dashboardId : dashboardIdList) {
            Dashboard srcDashboard = environment().find(Dashboard.class, dashboardId);
            String oldDescription = srcDashboard.getDescription();
            srcDashboard.setId(null);
            srcDashboard.setApplicationId(destApplicationId);
            srcDashboard.setName(ctx.nameSwap(srcDashboard.getName()));
            srcDashboard.setDescription(ctx.messageSwap(srcDashboard.getDescription()));

            // Tiles
            for (DashboardTile dashboardTile : srcDashboard.getTileList()) {
                dashboardTile.setName(ctx.nameSwap(dashboardTile.getName()));
                dashboardTile.setDescription(ctx.messageSwap(dashboardTile.getDescription()));
                dashboardTile.setChart(ctx.entitySwap(dashboardTile.getChart()));
            }

            srcDashboard.setConfigType(ConfigType.CUSTOM);
            environment().create(srcDashboard);
            logDebug(taskMonitor, "Dashboard [{0}] -> [{1}]...", oldDescription, srcDashboard.getDescription());
        }
    }

    @Override
    protected List<DeletionParams> getDeletionParams() throws UnifyException {
        return Arrays.asList(new DeletionParams("dashboards", new DashboardQuery()));
    }

    private void populateChildList(AppDashboardConfig dashboardConfig, Dashboard dashboard, String applicationName, boolean restore)
            throws UnifyException {
        List<DashboardSection> sectionList = null;
        if (dashboardConfig.getSectionList() != null && !DataUtils.isBlank(dashboardConfig.getSectionList().getSectionList())) {
            sectionList = new ArrayList<DashboardSection>();
            for (DashboardSectionConfig dashboardSectionConfig : dashboardConfig.getSectionList().getSectionList()) {
                DashboardSection dashboardSection = new DashboardSection();
                dashboardSection.setType(dashboardSectionConfig.getType());
                dashboardSection.setIndex(dashboardSectionConfig.getIndex());
                dashboardSection.setHeight(dashboardSectionConfig.getHeight());
                sectionList.add(dashboardSection);
            }
        }

        dashboard.setSectionList(sectionList);

        List<DashboardTile> tileList = null;
        if (dashboardConfig.getTiles() != null && !DataUtils.isBlank(dashboardConfig.getTiles().getTileList())) {
            tileList = new ArrayList<DashboardTile>();
            Map<String, DashboardTile> map = restore || dashboard.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new DashboardTileQuery().dashboardId(dashboard.getId()));
            for (DashboardTileConfig dashboardTileConfig : dashboardConfig.getTiles().getTileList()) {
                DashboardTile oldDashboardTile = map.get(dashboardTileConfig.getName());
                if (oldDashboardTile == null) {
                    DashboardTile dashboardTile = new DashboardTile();
                    dashboardTile.setType(dashboardTileConfig.getType());
                    dashboardTile.setChart(ApplicationNameUtils.ensureLongNameReference(applicationName,
                            dashboardTileConfig.getChart()));
                    dashboardTile.setName(dashboardTileConfig.getName());
                    dashboardTile.setDescription(resolveApplicationMessage(dashboardTileConfig.getDescription()));
                    dashboardTile.setSection(dashboardTileConfig.getSection());
                    dashboardTile.setIndex(dashboardTileConfig.getIndex());
                    dashboardTile.setConfigType(restore ? ConfigType.CUSTOM: ConfigType.STATIC);
                    tileList.add(dashboardTile);
                } else {
                    oldDashboardTile.setType(dashboardTileConfig.getType());
                    oldDashboardTile.setChart(ApplicationNameUtils.ensureLongNameReference(applicationName,
                            dashboardTileConfig.getChart()));
                    oldDashboardTile
                            .setDescription(resolveApplicationMessage(dashboardTileConfig.getDescription()));
                    oldDashboardTile.setSection(dashboardTileConfig.getSection());
                    oldDashboardTile.setIndex(dashboardTileConfig.getIndex());
                    oldDashboardTile.setConfigType(ConfigType.STATIC);
                    tileList.add(oldDashboardTile);
                }

            }
        }

        dashboard.setTileList(tileList);

        List<DashboardOption> optionList = null;
        if (dashboardConfig.getOptions() != null && !DataUtils.isBlank(dashboardConfig.getOptions().getOptionsList())) {
            optionList = new ArrayList<DashboardOption>();
            Map<String, DashboardOption> map = restore || dashboard.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new DashboardOptionQuery().dashboardId(dashboard.getId()));
            for (DashboardOptionConfig dashboardOptionConfig : dashboardConfig.getOptions().getOptionsList()) {
                DashboardOption oldDashboardOption = map.get(dashboardOptionConfig.getName());
                if (oldDashboardOption == null) {
                    DashboardOption dashboardOption = new DashboardOption();
                    dashboardOption.setName(dashboardOptionConfig.getName());
                    dashboardOption.setDescription(resolveApplicationMessage(dashboardOptionConfig.getDescription()));
                    dashboardOption.setLabel(resolveApplicationMessage(dashboardOptionConfig.getLabel()));
                    populateChildList(dashboardOptionConfig, dashboardOption);
                    optionList.add(dashboardOption);
                } else {
                    oldDashboardOption
                            .setDescription(resolveApplicationMessage(dashboardOptionConfig.getDescription()));
                    oldDashboardOption.setLabel(resolveApplicationMessage(dashboardOptionConfig.getLabel()));
                    populateChildList(dashboardOptionConfig, oldDashboardOption);
                    optionList.add(oldDashboardOption);
                }

            }
        }

        dashboard.setOptionsList(optionList);
    }

    private void populateChildList(DashboardOptionConfig dashboardOptionConfig, DashboardOption dashboardOption)
            throws UnifyException {
        List<DashboardOptionCategoryBase> baseList = null;
        if (!DataUtils.isBlank(dashboardOptionConfig.getBaseList())) {
            baseList = new ArrayList<DashboardOptionCategoryBase>();
            for (DashboardOptionCategoryBaseConfig baseConfig : dashboardOptionConfig.getBaseList()) {
                DashboardOptionCategoryBase base = new DashboardOptionCategoryBase();
                base.setChartDataSource(baseConfig.getChartDataSource());
                base.setEntity(baseConfig.getEntity());
                base.setCategoryBase(InputWidgetUtils.newAppFilter(baseConfig.getCategoryBase()));
                baseList.add(base);
            }
        }

        dashboardOption.setBaseList(baseList);
    }

}
