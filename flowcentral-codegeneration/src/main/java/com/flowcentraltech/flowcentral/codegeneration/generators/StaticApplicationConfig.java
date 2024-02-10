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

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.flowcentraltech.flowcentral.configuration.xml.AppChartDataSourcesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppChartsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppDashboardsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppNotifLargeTextsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppNotifTemplatesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppReportsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppWorkflowWizardsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppWorkflowsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WfChannelsConfig;

/**
 * Static application configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class StaticApplicationConfig {

    private String applicationName;
    
    private String applicationDesc;

    private AppChartsConfig chartsConfig;

    private AppChartDataSourcesConfig chartDataSourcesConfig;
    
    private AppDashboardsConfig dashboardsConfig;

    private AppReportsConfig reportsConfig;

    private AppNotifTemplatesConfig notifTemplatesConfig;

    private AppNotifLargeTextsConfig notifLargeTextsConfig;

    private AppWorkflowsConfig workflowsConfig;

    private AppWorkflowWizardsConfig workflowWizardsConfig;

    private WfChannelsConfig wfChannelsConfig;
    
    private EnumMap<StaticMessageCategoryType, Map<String, String>> messages;
    
    private long gapCounter;

    public StaticApplicationConfig(String applicationName, String applicationDesc) {
        this.applicationName = applicationName;
        this.applicationDesc = applicationDesc;
        this.messages = new EnumMap<StaticMessageCategoryType, Map<String, String>>(StaticMessageCategoryType.class);
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getApplicationDesc() {
        return applicationDesc;
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

    public WfChannelsConfig getWfChannelsConfig() {
        return wfChannelsConfig;
    }

    public void setWfChannelsConfig(WfChannelsConfig wfChannelsConfig) {
        this.wfChannelsConfig = wfChannelsConfig;
    }

    public void addMessageGap(StaticMessageCategoryType category) {
        getMessages(category).put("_ctxGap" + (++gapCounter), null);
    }

    public void addMessage(StaticMessageCategoryType category, String key, String value) {
        getMessages(category).put(key, value);
    }

    public Map<String, String> getMessages(StaticMessageCategoryType category) {
        Map<String, String> msgs = messages.get(category);
        if (msgs == null) {
            synchronized (this) {
                msgs = messages.get(category);
                if (msgs == null) {
                    msgs = new LinkedHashMap<String, String>();
                    messages.put(category, msgs);
                }
            }
        }

        return msgs;
    }
}
