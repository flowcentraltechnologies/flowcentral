/*
 * Copyright (c) 2019, 2021, FlowCentral Technologies.
 * All rights reserved.
 * 
 * PROPRIETARY AND CONFIDENTIAL. USE IS SUBJECT TO LICENSE TERMS.
 */

package com.flowcentraltech.flowcentral.dashboard.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.constants.ApplicationPrivilegeConstants;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.common.business.AbstractApplicationArtifactInstaller;
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
 * @author Lateef Ojulari
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
                    dashboard.setConfigType(ConfigType.MUTABLE_INSTALL);
                    populateChildList(dashboardConfig, dashboard, applicationName);
                    environment().create(dashboard);
                } else {
                    if (ConfigUtils.isSetInstall(oldDashboard)) {
                        oldDashboard.setDescription(description);
                        oldDashboard.setSections(dashboardConfig.getSections());
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
