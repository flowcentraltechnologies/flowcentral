/*
 * Copyright (c) 2019, 2021, FlowCentral Technologies.
 * All rights reserved.
 * 
 * PROPRIETARY AND CONFIDENTIAL. USE IS SUBJECT TO LICENSE TERMS.
 */
package com.flowcentraltech.flowcentral.dashboard.business;

import java.util.List;

import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.dashboard.constants.DashboardModuleErrorConstants;
import com.flowcentraltech.flowcentral.dashboard.constants.DashboardModuleNameConstants;
import com.flowcentraltech.flowcentral.dashboard.data.DashboardDef;
import com.flowcentraltech.flowcentral.dashboard.entities.Dashboard;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardQuery;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardTile;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.data.FactoryMap;

/**
 * Default dashboard business service implementation.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component(DashboardModuleNameConstants.DASHBOARD_MODULE_SERVICE)
public class DashboardModuleServiceImpl extends AbstractFlowCentralService implements DashboardModuleService {

    private FactoryMap<String, DashboardDef> dashboardDefFactoryMap;

    public DashboardModuleServiceImpl() {

        this.dashboardDefFactoryMap = new FactoryMap<String, DashboardDef>(true)
            {
                @Override
                protected boolean stale(String dashboardName, DashboardDef dashboardDef) throws Exception {
                    return environment().value(long.class, "versionNo",
                            new DashboardQuery().id(dashboardDef.getId())) > dashboardDef.getVersion();
                }

                @Override
                protected DashboardDef create(String longName, Object... arg1) throws Exception {
                    ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
                    Dashboard dashboard = environment().list(new DashboardQuery()
                            .applicationName(nameParts.getApplicationName()).name(nameParts.getEntityName()));
                    if (dashboard == null) {
                        throw new UnifyException(DashboardModuleErrorConstants.CANNOT_FIND_APPLICATION_DASHBOARD,
                                longName);
                    }

                    DashboardDef.Builder ddb = DashboardDef.newBuilder(dashboard.getSections(), longName,
                            dashboard.getDescription(), dashboard.getId(), dashboard.getVersionNo());
                    for (DashboardTile dashboardTile : dashboard.getTileList()) {
                        ddb.addTile(dashboardTile.getType(), dashboardTile.getName(), dashboardTile.getDescription(),
                                dashboardTile.getChart(), dashboardTile.getSection(), dashboardTile.getIndex());
                    }
                    return ddb.build();
                }

            };

    }

    @Override
    public Dashboard findDashboard(Long dashboardId) throws UnifyException {
        return environment().find(Dashboard.class, dashboardId);
    }

    @Override
    public List<Long> findDashboardIdList(String applicationName) throws UnifyException {
        return environment().valueList(Long.class, "id", new DashboardQuery().applicationName(applicationName));
    }

    @Override
    public DashboardDef getDashboardDef(String dashboardName) throws UnifyException {
        return dashboardDefFactoryMap.get(dashboardName);
    }

    @Override
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {

    }

}
