/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.util.ConfigUtils;
import com.flowcentraltech.flowcentral.configuration.data.ApplicationInstall;
import com.flowcentraltech.flowcentral.configuration.xml.AppConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppDashboardConfig;
import com.flowcentraltech.flowcentral.configuration.xml.DashboardTileConfig;
import com.flowcentraltech.flowcentral.dashboard.constants.DashboardModuleNameConstants;
import com.flowcentraltech.flowcentral.dashboard.entities.Dashboard;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardQuery;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardTile;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardTileQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
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

    public void setApplicationPrivilegeManager(ApplicationPrivilegeManager applicationPrivilegeManager) {
        this.applicationPrivilegeManager = applicationPrivilegeManager;
    }

    @Override
    public void installApplicationArtifacts(final TaskMonitor taskMonitor, final ApplicationInstall applicationInstall)
            throws UnifyException {
        final AppConfig applicationConfig = applicationInstall.getApplicationConfig();
        final String applicationName = applicationConfig.getName();
        final Long applicationId = applicationInstall.getApplicationId();

        logDebug(taskMonitor, "Executing dashboard installer...");
        // Install configured dashboards
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
                    dashboard.setConfigType(ConfigType.MUTABLE_INSTALL);
                    populateChildList(dashboardConfig, dashboard, applicationName);
                    environment().create(dashboard);
                } else {
                    if (ConfigUtils.isSetInstall(oldDashboard)) {
                        oldDashboard.setDescription(description);
                        oldDashboard.setSections(dashboardConfig.getSections());
                        oldDashboard.setAllowSecondaryTenants(dashboardConfig.getAllowSecondaryTenants());
                    }

                    populateChildList(dashboardConfig, oldDashboard, applicationName);
                    environment().updateByIdVersion(oldDashboard);
                }

                applicationPrivilegeManager.registerPrivilege(applicationId,
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
            srcDashboard.setApplicationId(destApplicationId);
            srcDashboard.setName(ctx.nameSwap(srcDashboard.getName()));
            srcDashboard.setDescription(ctx.messageSwap(srcDashboard.getDescription()));

            // Tiles
            for (DashboardTile dashboardTile : srcDashboard.getTileList()) {
                dashboardTile.setName(ctx.nameSwap(dashboardTile.getName()));
                dashboardTile.setDescription(ctx.messageSwap(dashboardTile.getDescription()));
                dashboardTile.setChart(ctx.entitySwap(dashboardTile.getChart()));
            }

            environment().create(srcDashboard);
            logDebug(taskMonitor, "Dashboard [{0}] -> [{1}]...", oldDescription, srcDashboard.getDescription());
        }
    }

    @Override
    protected List<DeletionParams> getDeletionParams() throws UnifyException {
        return Arrays.asList(new DeletionParams("dashboards", new DashboardQuery()));
    }

    private void populateChildList(AppDashboardConfig dashboardConfig, Dashboard dashboard, String applicationName)
            throws UnifyException {
        List<DashboardTile> tileList = null;
        if (!DataUtils.isBlank(dashboardConfig.getTileList())) {
            tileList = new ArrayList<DashboardTile>();
            Map<String, DashboardTile> map = dashboard.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new DashboardTileQuery().dashboardId(dashboard.getId()));
            for (DashboardTileConfig dashboardTileConfig : dashboardConfig.getTileList()) {
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
                    dashboardTile.setConfigType(ConfigType.MUTABLE_INSTALL);
                    tileList.add(dashboardTile);
                } else {
                    if (ConfigUtils.isSetInstall(oldDashboardTile)) {
                        oldDashboardTile.setType(dashboardTileConfig.getType());
                        oldDashboardTile.setChart(ApplicationNameUtils.ensureLongNameReference(applicationName,
                                dashboardTileConfig.getChart()));
                        oldDashboardTile
                                .setDescription(resolveApplicationMessage(dashboardTileConfig.getDescription()));
                        oldDashboardTile.setSection(dashboardTileConfig.getSection());
                        oldDashboardTile.setIndex(dashboardTileConfig.getIndex());
                    }

                    tileList.add(oldDashboardTile);
                }

            }
        }

        dashboard.setTileList(tileList);
    }

}
