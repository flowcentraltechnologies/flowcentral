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

package com.flowcentraltech.flowcentral.codegeneration.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.entities.AppEntity;
import com.flowcentraltech.flowcentral.application.util.ApplicationCodeGenUtils;
import com.flowcentraltech.flowcentral.codegeneration.data.DynamicModuleInfo;
import com.flowcentraltech.flowcentral.configuration.xml.AppChartDataSourcesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppChartsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppDashboardsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppHelpSheetsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppNotifLargeTextsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppNotifTemplatesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppReportsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppWorkflowWizardsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppWorkflowsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ModuleAppConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ModuleAppsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WfChannelsConfig;

/**
 * Extension module static file builder context.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ExtensionModuleStaticFileBuilderContext {

    private ExtensionStaticFileBuilderContext mainCtx;

    private List<ApplicationConfig> applicationConfigs;

    private ApplicationConfig nextApplicationConfig;

    private ModuleAppsConfig moduleAppsConfig;

    private List<String> entityList;

    private Map<String, String> messageReplacements;

    private String moduleName;

    private DynamicModuleInfo dynamicModuleInfo;

    private boolean snapshotMode;
    
    public ExtensionModuleStaticFileBuilderContext(ExtensionStaticFileBuilderContext mainCtx, String moduleName,
            Map<String, String> messageReplacements, boolean snapshotMode) {
        this.applicationConfigs = new ArrayList<ApplicationConfig>();
        this.moduleAppsConfig = new ModuleAppsConfig();
        this.moduleAppsConfig.setModuleAppList(new ArrayList<ModuleAppConfig>());
        this.entityList = new ArrayList<String>();
        this.mainCtx = mainCtx;
        this.moduleName = moduleName;
        this.messageReplacements = messageReplacements;
        this.snapshotMode = snapshotMode;
    }

    public String getBasePackage() {
        return mainCtx.getBasePackage();
    }

    public String getModuleName() {
        return moduleName;
    }

    public List<ApplicationConfig> getApplicationConfigs() {
        return applicationConfigs;
    }

    public boolean addZipDir(String zipDir) {
        return mainCtx.addZipDir(zipDir);
    }

    public void addEntity(String entityName) {
        entityList.add(entityName);
    }

    public List<String> getEntityList() {
        return entityList;
    }

    public boolean isSnapshotMode() {
        return snapshotMode;
    }

    public DynamicModuleInfo getDynamicModuleInfo() {
        return dynamicModuleInfo;
    }

    public void setDynamicModuleInfo(DynamicModuleInfo dynamicModuleInfo) {
        this.dynamicModuleInfo = dynamicModuleInfo;
    }

    public void nextApplication(String applicationName, String applicationDesc, boolean custom) {
        nextApplicationConfig = new ApplicationConfig(applicationName, applicationDesc, custom);
        applicationConfigs.add(nextApplicationConfig);
    }

    public void addModuleAppConfig(ModuleAppConfig moduleAppConfig) {
        moduleAppsConfig.getModuleAppList().add(moduleAppConfig);
    }

    public ModuleAppsConfig getModuleAppsConfig() {
        return moduleAppsConfig;
    }

    public void setReportsConfig(AppReportsConfig reportsConfig) {
        nextApplicationConfig.setReportsConfig(reportsConfig);
    }

    public void setNotifTemplatesConfig(AppNotifTemplatesConfig notifTemplatesConfig) {
        nextApplicationConfig.setNotifTemplatesConfig(notifTemplatesConfig);
    }

    public void setAppHelpSheetsConfig(AppHelpSheetsConfig appHelpSheetsConfig) {
        nextApplicationConfig.setHelpSheetsConfig(appHelpSheetsConfig);
    }
    
    public void setNotifLargeTextsConfig(AppNotifLargeTextsConfig notifLargeTextsConfig) {
        nextApplicationConfig.setNotifLargeTextsConfig(notifLargeTextsConfig);
    }

    public void setWorkflowsConfig(AppWorkflowsConfig workflowsConfig) {
        nextApplicationConfig.setWorkflowsConfig(workflowsConfig);
    }

    public void setWorkflowWizardsConfig(AppWorkflowWizardsConfig workflowWizardsConfig) {
        nextApplicationConfig.setWorkflowWizardsConfig(workflowWizardsConfig);
    }

    public void setWfChannelsConfig(WfChannelsConfig wfChannelsConfig) {
        nextApplicationConfig.setWfChannelsConfig(wfChannelsConfig);
    }

    public void setChartsConfig(AppChartsConfig chartsConfig) {
        nextApplicationConfig.setChartsConfig(chartsConfig);
    }

    public void setChartDataSourcesConfig(AppChartDataSourcesConfig chartDataSourcesConfig) {
        nextApplicationConfig.setChartDataSourcesConfig(chartDataSourcesConfig);
    }

    public void setDashboardsConfig(AppDashboardsConfig dashboardsConfig) {
        nextApplicationConfig.setDashboardsConfig(dashboardsConfig);
    }

    public void addMessageGap(StaticMessageCategoryType category) {
        nextApplicationConfig.addMessageGap(category);
    }

    public AppChartsConfig getChartsConfig() {
        return nextApplicationConfig.getChartsConfig();
    }

    public AppChartDataSourcesConfig getChartDataSourcesConfig() {
        return nextApplicationConfig.getChartDataSourcesConfig();
    }

    public AppDashboardsConfig getDashboardsConfig() {
        return nextApplicationConfig.getDashboardsConfig();
    }

    public AppReportsConfig getReportsConfig() {
        return nextApplicationConfig.getReportsConfig();
    }

    public AppNotifTemplatesConfig getNotifTemplatesConfig() {
        return nextApplicationConfig.getNotifTemplatesConfig();
    }

    public AppNotifLargeTextsConfig getNotifLargeTextsConfig() {
        return nextApplicationConfig.getNotifLargeTextsConfig();
    }

    public AppWorkflowsConfig getWorkflowsConfig() {
        return nextApplicationConfig.getWorkflowsConfig();
    }

    public AppWorkflowWizardsConfig getWorkflowWizardsConfig() {
        return nextApplicationConfig.getWorkflowWizardsConfig();
    }

    public WfChannelsConfig getWfChannelsConfig() {
        return nextApplicationConfig.getWfChannelsConfig();
    }

    public void addMessage(StaticMessageCategoryType category, String key, String val) {
        if (val != null) {
        for (Map.Entry<String, String> entry : messageReplacements.entrySet()) {
            val = val.replaceAll(entry.getKey(), entry.getValue());
        }
        } else {
            val = "";
        }

        nextApplicationConfig.addMessage(category, key, val);
    }

    public String getExtensionEntityClassName(AppEntity appEntity) {
        return ApplicationCodeGenUtils.generateExtensionEntityClassName(mainCtx.getBasePackage(), moduleName,
                appEntity.getName());
    }
}
