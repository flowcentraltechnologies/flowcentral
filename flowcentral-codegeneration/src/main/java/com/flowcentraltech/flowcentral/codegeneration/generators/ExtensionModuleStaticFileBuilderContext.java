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

    private List<StaticApplicationConfig> staticApplicationConfigs;

    private StaticApplicationConfig nextStaticApplicationConfig;

    private ModuleAppsConfig moduleAppsConfig;

    private List<String> entityList;

    private Map<String, String> messageReplacements;

    private String moduleName;

    private DynamicModuleInfo dynamicModuleInfo;

    public ExtensionModuleStaticFileBuilderContext(ExtensionStaticFileBuilderContext mainCtx, String moduleName,
            Map<String, String> messageReplacements) {
        this.staticApplicationConfigs = new ArrayList<StaticApplicationConfig>();
        this.moduleAppsConfig = new ModuleAppsConfig();
        this.moduleAppsConfig.setModuleAppList(new ArrayList<ModuleAppConfig>());
        this.entityList = new ArrayList<String>();
        this.mainCtx = mainCtx;
        this.moduleName = moduleName;
        this.messageReplacements = messageReplacements;
    }

    public String getBasePackage() {
        return mainCtx.getBasePackage();
    }

    public String getModuleName() {
        return moduleName;
    }

    public List<StaticApplicationConfig> getStaticApplicationConfigs() {
        return staticApplicationConfigs;
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

    public DynamicModuleInfo getDynamicModuleInfo() {
        return dynamicModuleInfo;
    }

    public void setDynamicModuleInfo(DynamicModuleInfo dynamicModuleInfo) {
        this.dynamicModuleInfo = dynamicModuleInfo;
    }

    public void nextApplication(String applicationName, String applicationDesc) {
        nextStaticApplicationConfig = new StaticApplicationConfig(applicationName, applicationDesc);
        staticApplicationConfigs.add(nextStaticApplicationConfig);
    }

    public void addModuleAppConfig(ModuleAppConfig moduleAppConfig) {
        moduleAppsConfig.getModuleAppList().add(moduleAppConfig);
    }

    public ModuleAppsConfig getModuleAppsConfig() {
        return moduleAppsConfig;
    }

    public void setReportsConfig(AppReportsConfig reportsConfig) {
        nextStaticApplicationConfig.setReportsConfig(reportsConfig);
    }

    public void setNotifTemplatesConfig(AppNotifTemplatesConfig notifTemplatesConfig) {
        nextStaticApplicationConfig.setNotifTemplatesConfig(notifTemplatesConfig);
    }

    public void setNotifLargeTextsConfig(AppNotifLargeTextsConfig notifLargeTextsConfig) {
        nextStaticApplicationConfig.setNotifLargeTextsConfig(notifLargeTextsConfig);
    }

    public void setWorkflowsConfig(AppWorkflowsConfig workflowsConfig) {
        nextStaticApplicationConfig.setWorkflowsConfig(workflowsConfig);
    }

    public void setWorkflowWizardsConfig(AppWorkflowWizardsConfig workflowWizardsConfig) {
        nextStaticApplicationConfig.setWorkflowWizardsConfig(workflowWizardsConfig);
    }

    public void setWfChannelsConfig(WfChannelsConfig wfChannelsConfig) {
        nextStaticApplicationConfig.setWfChannelsConfig(wfChannelsConfig);
    }

    public void setChartsConfig(AppChartsConfig chartsConfig) {
        nextStaticApplicationConfig.setChartsConfig(chartsConfig);
    }

    public void setChartDataSourcesConfig(AppChartDataSourcesConfig chartDataSourcesConfig) {
        nextStaticApplicationConfig.setChartDataSourcesConfig(chartDataSourcesConfig);
    }

    public void setDashboardsConfig(AppDashboardsConfig dashboardsConfig) {
        nextStaticApplicationConfig.setDashboardsConfig(dashboardsConfig);
    }

    public void addMessageGap(StaticMessageCategoryType category) {
        nextStaticApplicationConfig.addMessageGap(category);
    }

    public AppChartsConfig getChartsConfig() {
        return nextStaticApplicationConfig.getChartsConfig();
    }

    public AppDashboardsConfig getDashboardsConfig() {
        return nextStaticApplicationConfig.getDashboardsConfig();
    }

    public AppReportsConfig getReportsConfig() {
        return nextStaticApplicationConfig.getReportsConfig();
    }

    public AppNotifTemplatesConfig getNotifTemplatesConfig() {
        return nextStaticApplicationConfig.getNotifTemplatesConfig();
    }

    public AppNotifLargeTextsConfig getNotifLargeTextsConfig() {
        return nextStaticApplicationConfig.getNotifLargeTextsConfig();
    }

    public AppWorkflowsConfig getWorkflowsConfig() {
        return nextStaticApplicationConfig.getWorkflowsConfig();
    }

    public AppWorkflowWizardsConfig getWorkflowWizardsConfig() {
        return nextStaticApplicationConfig.getWorkflowWizardsConfig();
    }

    public WfChannelsConfig getWfChannelsConfig() {
        return nextStaticApplicationConfig.getWfChannelsConfig();
    }

    public void addMessage(StaticMessageCategoryType category, String key, String val) {
        if (val != null) {
        for (Map.Entry<String, String> entry : messageReplacements.entrySet()) {
            val = val.replaceAll(entry.getKey(), entry.getValue());
        }
        } else {
            val = "";
        }

        nextStaticApplicationConfig.addMessage(category, key, val);
    }

    public String getExtensionEntityClassName(AppEntity appEntity) {
        return ApplicationCodeGenUtils.generateExtensionEntityClassName(mainCtx.getBasePackage(), moduleName,
                appEntity.getName());
    }
}
