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

package com.flowcentraltech.flowcentral.chart.business;

import java.util.Arrays;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AbstractApplicationArtifactInstaller;
import com.flowcentraltech.flowcentral.chart.constants.ChartModuleNameConstants;
import com.flowcentraltech.flowcentral.chart.entities.Chart;
import com.flowcentraltech.flowcentral.chart.entities.ChartQuery;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.util.ConfigUtils;
import com.flowcentraltech.flowcentral.configuration.data.ApplicationInstall;
import com.flowcentraltech.flowcentral.configuration.xml.AppChartConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Application chart installer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(ChartModuleNameConstants.APPLICATION_CHART_INSTALLER)
public class ApplicationChartInstallerImpl extends AbstractApplicationArtifactInstaller {

    @Override
    public void installApplicationArtifacts(final TaskMonitor taskMonitor, final ApplicationInstall applicationInstall)
            throws UnifyException {
        final AppConfig applicationConfig = applicationInstall.getApplicationConfig();
        final Long applicationId = applicationInstall.getApplicationId();

        logDebug(taskMonitor, "Executing chart installer...");
        // Install configured charts
        if (applicationConfig.getChartsConfig() != null
                && !DataUtils.isBlank(applicationConfig.getChartsConfig().getChartList())) {
            for (AppChartConfig appChartConfig : applicationConfig.getChartsConfig().getChartList()) {
                String description = resolveApplicationMessage(appChartConfig.getDescription());
                String title = resolveApplicationMessage(appChartConfig.getTitle());
                String subTitle = resolveApplicationMessage(appChartConfig.getSubTitle());
                logDebug(taskMonitor, "Installing chart chart [{0}]...", description);

                Chart oldChart = environment()
                        .findLean(new ChartQuery().applicationId(applicationId).name(appChartConfig.getName()));
                if (oldChart == null) {
                    Chart chart = new Chart();
                    chart.setApplicationId(applicationId);
                    chart.setType(appChartConfig.getType());
                    chart.setPaletteType(appChartConfig.getPaletteType());
                    chart.setName(appChartConfig.getName());
                    chart.setDescription(description);
                    chart.setTitle(title);
                    chart.setSubTitle(subTitle);
                    chart.setWidth(appChartConfig.getWidth());
                    chart.setHeight(appChartConfig.getHeight());
                    chart.setProvider(appChartConfig.getProvider());
                    chart.setRule(appChartConfig.getRule());
                    chart.setShowGrid(appChartConfig.isShowGrid());
                    chart.setShowDataLabels(appChartConfig.isShowDataLabels());
                    chart.setStacked(appChartConfig.isStacked());
                    chart.setSmooth(appChartConfig.isSmooth());
                    chart.setConfigType(ConfigType.MUTABLE_INSTALL);
                    environment().create(chart);
                } else {
                    if (ConfigUtils.isSetInstall(oldChart)) {
                        oldChart.setType(appChartConfig.getType());
                        oldChart.setPaletteType(appChartConfig.getPaletteType());
                        oldChart.setDescription(description);
                        oldChart.setTitle(title);
                        oldChart.setSubTitle(subTitle);
                        oldChart.setWidth(appChartConfig.getWidth());
                        oldChart.setHeight(appChartConfig.getHeight());
                        oldChart.setProvider(appChartConfig.getProvider());
                        oldChart.setRule(appChartConfig.getRule());
                        oldChart.setShowGrid(appChartConfig.isShowGrid());
                        oldChart.setShowDataLabels(appChartConfig.isShowDataLabels());
                        oldChart.setStacked(appChartConfig.isStacked());
                        oldChart.setSmooth(appChartConfig.isSmooth());
                        environment().updateByIdVersion(oldChart);
                    }
                }
            }
        }
    }

    @Override
    protected List<DeletionParams> getDeletionParams() throws UnifyException {
        return Arrays.asList(new DeletionParams("charts", new ChartQuery()));
    }

}
