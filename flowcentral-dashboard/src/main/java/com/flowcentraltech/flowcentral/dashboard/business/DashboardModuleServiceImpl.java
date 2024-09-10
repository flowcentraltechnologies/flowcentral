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
package com.flowcentraltech.flowcentral.dashboard.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.chart.business.ChartModuleService;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.dashboard.constants.DashboardModuleErrorConstants;
import com.flowcentraltech.flowcentral.dashboard.constants.DashboardModuleNameConstants;
import com.flowcentraltech.flowcentral.dashboard.data.DashboardDef;
import com.flowcentraltech.flowcentral.dashboard.data.DashboardOptionCatBaseDef;
import com.flowcentraltech.flowcentral.dashboard.data.DashboardOptionDef;
import com.flowcentraltech.flowcentral.dashboard.entities.Dashboard;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardOption;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardOptionCategoryBase;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardQuery;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardSection;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardTile;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Default dashboard business service implementation.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(DashboardModuleNameConstants.DASHBOARD_MODULE_SERVICE)
public class DashboardModuleServiceImpl extends AbstractFlowCentralService implements DashboardModuleService {

    @Configurable
    private ChartModuleService chartModuleService;

    @Configurable
    private AppletUtilities appletUtilities;

    private FactoryMap<String, DashboardDef> dashboardDefFactoryMap;

    public DashboardModuleServiceImpl() {

        this.dashboardDefFactoryMap = new FactoryMap<String, DashboardDef>(true)
            {
                @Override
                protected boolean pause() throws Exception {
                    return isInSystemRestoreMode();
                }

                @Override
                protected boolean stale(String dashboardName, DashboardDef dashboardDef) throws Exception {
                    return isStale(new DashboardQuery(), dashboardDef);
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

                    DashboardDef.Builder ddb = DashboardDef.newBuilder(dashboard.getStatus(), longName,
                            dashboard.getDescription(), dashboard.getId(), dashboard.getVersionNo());

                    DataUtils.sortAscending(dashboard.getOptionsList(), DashboardOption.class, "id");
                    for (DashboardOption dashboardOption : dashboard.getOptionsList()) {
                        List<DashboardOptionCatBaseDef> catBaseList = new ArrayList<DashboardOptionCatBaseDef>();
                        for (DashboardOptionCategoryBase catBase : dashboardOption.getBaseList()) {
                            List<String> dataSourceNames = Arrays
                                    .asList(StringUtils.commaSplit(catBase.getChartDataSource()));
                            catBaseList.add(new DashboardOptionCatBaseDef(dataSourceNames,
                                    InputWidgetUtils.getFilterDef(appletUtilities, null, catBase.getCategoryBase())));
                        }

                        DashboardOptionDef dashboardOptionDef = new DashboardOptionDef(dashboardOption.getName(),
                                dashboardOption.getDescription(), dashboardOption.getLabel(), catBaseList);
                        ddb.addOption(dashboardOptionDef);
                    }

                    DataUtils.sortAscending(dashboard.getSectionList(), DashboardSection.class, "index");
                    for (DashboardSection dashboardSection : dashboard.getSectionList()) {
                        ddb.addSection(dashboardSection.getType(), dashboardSection.getHeight());
                    }

                    for (DashboardTile dashboardTile : dashboard.getTileList()) {
                        ddb.addTile(dashboardTile.getType(), dashboardTile.getName(), dashboardTile.getDescription(),
                                dashboardTile.getChart(), dashboardTile.getSection(), dashboardTile.getIndex());
                    }
                    return ddb.build();
                }

            };

    }
    
    @Override
    public void clearDefinitionsCache() throws UnifyException {
        logDebug("Clearing definitions cache...");
        dashboardDefFactoryMap.clear();
        logDebug("Definitions cache clearing successfully completed.");
    }

    @Override
    public void clearDashboardCache() throws UnifyException {
        chartModuleService.clearChartCache();
    }

    @Override
    public Dashboard findDashboard(Long dashboardId) throws UnifyException {
        return environment().find(Dashboard.class, dashboardId);
    }

    @Override
    public List<Long> findCustomDashboardIdList(String applicationName) throws UnifyException {
        return environment().valueList(Long.class, "id",
                new DashboardQuery().applicationName(applicationName).isCustom());
    }

    @Override
    public DashboardDef getDashboardDef(String dashboardName) throws UnifyException {
        return dashboardDefFactoryMap.get(dashboardName);
    }

    @Override
    public void updateDashboardStatus(DashboardQuery query, RecordStatus status) throws UnifyException {
        int updated = environment().updateAll(query, new Update().add("status", status));
        if (updated > 0) {
            dashboardDefFactoryMap.clear();
        }
    }

    @Override
    public List<? extends Listable> getDashboardOptionList(String dashboard) throws UnifyException {
        return getDashboardDef(dashboard).getOptionList();
    }

    @Override
    public List<? extends Listable> getDashboardOptionChartDataSourceList(String entity) throws UnifyException {
        return chartModuleService.getChartDataSourceListByEntity(entity);
    }

    @Override
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {

    }

}
