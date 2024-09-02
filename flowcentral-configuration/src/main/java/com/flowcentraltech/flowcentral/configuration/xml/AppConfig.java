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
package com.flowcentraltech.flowcentral.configuration.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Application configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "application")
public class AppConfig extends BaseNameConfig {

    @JacksonXmlProperty(isAttribute = true)
    private String module;

    @JacksonXmlProperty(isAttribute = true)
    private int displayIndex;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean developable;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean menuAccess;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean allowSecondaryTenants;

    @JacksonXmlProperty(localName = "reports")
    private AppReportsConfig reportsConfig;

    @JacksonXmlProperty(localName = "notifTemplates")
    private AppNotifTemplatesConfig notifTemplatesConfig;

    @JacksonXmlProperty(localName = "notifLargeTexts")
    private AppNotifLargeTextsConfig notifLargeTextsConfig;

    @JacksonXmlProperty(localName = "workflows")
    private AppWorkflowsConfig workflowsConfig;

    @JacksonXmlProperty(localName = "workflow-wizards")
    private AppWorkflowWizardsConfig workflowWizardsConfig;
    
    @JacksonXmlProperty(localName = "applets")
    private AppletsConfig appletsConfig;
    
    @JacksonXmlProperty(localName = "helpSheets")
    private AppHelpSheetsConfig helpSheetsConfig;
    
    @JacksonXmlProperty(localName = "enumerations")
    private EnumerationsConfig enumerationsConfig;
    
    @JacksonXmlProperty(localName = "widgetTypes")
    private WidgetTypesConfig widgetTypesConfig;

    @JacksonXmlProperty(localName = "references")
    private RefsConfig refsConfig;

    @JacksonXmlProperty(localName = "entities")
    private AppEntitiesConfig entitiesConfig;

    @JacksonXmlProperty(localName = "tables")
    private AppTablesConfig tablesConfig;

    @JacksonXmlProperty(localName = "workflow-channels")
    private WfChannelsConfig wfChannelsConfig;
    
    @JacksonXmlProperty(localName = "forms")
    private AppFormsConfig formsConfig;

    @JacksonXmlProperty(localName = "assignmentPages")
    private AppAssignmentPagesConfig assignmentPagesConfig;

    @JacksonXmlProperty(localName = "propertyLists")
    private PropertyListsConfig propertyListsConfig;

    @JacksonXmlProperty(localName = "propertyRules")
    private PropertyRulesConfig propertyRulesConfig;

    @JacksonXmlProperty(localName = "charts")
    private AppChartsConfig chartsConfig;

    @JacksonXmlProperty(localName = "chart-datasources")
    private AppChartDataSourcesConfig chartDataSourcesConfig;
    
    @JacksonXmlProperty(localName = "dashboards")
    private AppDashboardsConfig dashboardsConfig;

    @JacksonXmlProperty(localName = "suggestionTypes")
    private SuggestionTypesConfig suggestionTypesConfig;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean custom;
    
    public AppConfig() {
        this.menuAccess = Boolean.TRUE;
        this.developable = Boolean.FALSE;
        this.custom = Boolean.FALSE;
        this.allowSecondaryTenants = Boolean.FALSE;
   }
    
    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public int getDisplayIndex() {
        return displayIndex;
    }

    public void setDisplayIndex(int displayIndex) {
        this.displayIndex = displayIndex;
    }

    public Boolean getDevelopable() {
        return developable;
    }

    public void setDevelopable(Boolean developable) {
        this.developable = developable;
    }

    public Boolean getCustom() {
        return custom;
    }

    public void setCustom(Boolean custom) {
        this.custom = custom;
    }

    public Boolean getMenuAccess() {
        return menuAccess;
    }

    public void setMenuAccess(Boolean menuAccess) {
        this.menuAccess = menuAccess;
    }

    public Boolean getAllowSecondaryTenants() {
        return allowSecondaryTenants;
    }

    public void setAllowSecondaryTenants(Boolean allowSecondaryTenants) {
        this.allowSecondaryTenants = allowSecondaryTenants;
    }

    public AppReportsConfig getReportsConfig() {
        return reportsConfig;
    }

    public void setReportsConfig(AppReportsConfig reportsConfig) {
        this.reportsConfig = reportsConfig;
    }

    public AppNotifTemplatesConfig getNotifTemplatesConfig() {
        return notifTemplatesConfig;
    }

    public void setNotifTemplatesConfig(AppNotifTemplatesConfig notifTemplatesConfig) {
        this.notifTemplatesConfig = notifTemplatesConfig;
    }

    public AppNotifLargeTextsConfig getNotifLargeTextsConfig() {
        return notifLargeTextsConfig;
    }

    public void setNotifLargeTextsConfig(AppNotifLargeTextsConfig notifLargeTextsConfig) {
        this.notifLargeTextsConfig = notifLargeTextsConfig;
    }

    public AppWorkflowsConfig getWorkflowsConfig() {
        return workflowsConfig;
    }

    public void setWorkflowsConfig(AppWorkflowsConfig workflowsConfig) {
        this.workflowsConfig = workflowsConfig;
    }

    public AppWorkflowWizardsConfig getWorkflowWizardsConfig() {
        return workflowWizardsConfig;
    }

    public void setWorkflowWizardsConfig(AppWorkflowWizardsConfig workflowWizardsConfig) {
        this.workflowWizardsConfig = workflowWizardsConfig;
    }

    public AppHelpSheetsConfig getHelpSheetsConfig() {
        return helpSheetsConfig;
    }

    public void setHelpSheetsConfig(AppHelpSheetsConfig helpSheetsConfig) {
        this.helpSheetsConfig = helpSheetsConfig;
    }

    public AppletsConfig getAppletsConfig() {
        return appletsConfig;
    }

    public void setAppletsConfig(AppletsConfig appletsConfig) {
        this.appletsConfig = appletsConfig;
    }

    public EnumerationsConfig getEnumerationsConfig() {
        return enumerationsConfig;
    }

    public void setEnumerationsConfig(EnumerationsConfig enumerationsConfig) {
        this.enumerationsConfig = enumerationsConfig;
    }

    public WidgetTypesConfig getWidgetTypesConfig() {
        return widgetTypesConfig;
    }

    public void setWidgetTypesConfig(WidgetTypesConfig widgetTypesConfig) {
        this.widgetTypesConfig = widgetTypesConfig;
    }

    public RefsConfig getRefsConfig() {
        return refsConfig;
    }

    public void setRefsConfig(RefsConfig refsConfig) {
        this.refsConfig = refsConfig;
    }

    public AppEntitiesConfig getEntitiesConfig() {
        return entitiesConfig;
    }

    public void setEntitiesConfig(AppEntitiesConfig entitiesConfig) {
        this.entitiesConfig = entitiesConfig;
    }

    public AppTablesConfig getTablesConfig() {
        return tablesConfig;
    }

    public void setTablesConfig(AppTablesConfig tablesConfig) {
        this.tablesConfig = tablesConfig;
    }

    public AppFormsConfig getFormsConfig() {
        return formsConfig;
    }

    public void setFormsConfig(AppFormsConfig formsConfig) {
        this.formsConfig = formsConfig;
    }

    public WfChannelsConfig getWfChannelsConfig() {
        return wfChannelsConfig;
    }

    public void setWfChannelsConfig(WfChannelsConfig wfChannelsConfig) {
        this.wfChannelsConfig = wfChannelsConfig;
    }

    public AppAssignmentPagesConfig getAssignmentPagesConfig() {
        return assignmentPagesConfig;
    }

    public void setAssignmentPagesConfig(AppAssignmentPagesConfig assignmentPagesConfig) {
        this.assignmentPagesConfig = assignmentPagesConfig;
    }

    public PropertyListsConfig getPropertyListsConfig() {
        return propertyListsConfig;
    }

    public void setPropertyListsConfig(PropertyListsConfig propertyListsConfig) {
        this.propertyListsConfig = propertyListsConfig;
    }

    public PropertyRulesConfig getPropertyRulesConfig() {
        return propertyRulesConfig;
    }

    public void setPropertyRulesConfig(PropertyRulesConfig propertyRulesConfig) {
        this.propertyRulesConfig = propertyRulesConfig;
    }

    public AppChartsConfig getChartsConfig() {
        return chartsConfig;
    }

    public void setChartsConfig(AppChartsConfig chartsConfig) {
        this.chartsConfig = chartsConfig;
    }

    public AppChartDataSourcesConfig getChartDataSourcesConfig() {
        return chartDataSourcesConfig;
    }

    public void setChartDataSourcesConfig(AppChartDataSourcesConfig chartDataSourcesConfig) {
        this.chartDataSourcesConfig = chartDataSourcesConfig;
    }

    public AppDashboardsConfig getDashboardsConfig() {
        return dashboardsConfig;
    }

    public void setDashboardsConfig(AppDashboardsConfig dashboardsConfig) {
        this.dashboardsConfig = dashboardsConfig;
    }

    public SuggestionTypesConfig getSuggestionTypesConfig() {
        return suggestionTypesConfig;
    }

    public void setSuggestionTypesConfig(SuggestionTypesConfig suggestionTypesConfig) {
        this.suggestionTypesConfig = suggestionTypesConfig;
    }
}
