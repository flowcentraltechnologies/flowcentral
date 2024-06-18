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

package com.flowcentraltech.flowcentral.chart.business;

import java.util.Arrays;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AbstractApplicationArtifactInstaller;
import com.flowcentraltech.flowcentral.application.entities.AppFieldSequence;
import com.flowcentraltech.flowcentral.application.util.ApplicationReplicationContext;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.chart.constants.ChartModuleNameConstants;
import com.flowcentraltech.flowcentral.chart.entities.Chart;
import com.flowcentraltech.flowcentral.chart.entities.ChartDataSource;
import com.flowcentraltech.flowcentral.chart.entities.ChartDataSourceQuery;
import com.flowcentraltech.flowcentral.chart.entities.ChartQuery;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.util.ConfigUtils;
import com.flowcentraltech.flowcentral.configuration.data.ApplicationInstall;
import com.flowcentraltech.flowcentral.configuration.data.ApplicationRestore;
import com.flowcentraltech.flowcentral.configuration.xml.AppChartConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppChartDataSourceConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FieldSequenceConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.criterion.Update;
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
        environment().updateAll(new ChartQuery().applicationId(applicationId).isNotActualCustom(),
                new Update().add("deprecated", Boolean.TRUE));
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
                    chart.setCategory(appChartConfig.getCategory());
                    chart.setColor(appChartConfig.getColor());
                    chart.setSeries(appChartConfig.getSeries());
                    chart.setShowGrid(appChartConfig.isShowGrid());
                    chart.setShowDataLabels(appChartConfig.isShowDataLabels());
                    chart.setFormatDataLabels(appChartConfig.isFormatDataLabels());
                    chart.setFormatYLabels(appChartConfig.isFormatYLabels());
                    chart.setStacked(appChartConfig.isStacked());
                    chart.setSmooth(appChartConfig.isSmooth());
                    chart.setDeprecated(false);
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
                        oldChart.setCategory(appChartConfig.getCategory());
                        oldChart.setColor(appChartConfig.getColor());
                        oldChart.setSeries(appChartConfig.getSeries());
                        oldChart.setShowGrid(appChartConfig.isShowGrid());
                        oldChart.setShowDataLabels(appChartConfig.isShowDataLabels());
                        oldChart.setFormatDataLabels(appChartConfig.isFormatDataLabels());
                        oldChart.setFormatYLabels(appChartConfig.isFormatYLabels());
                        oldChart.setStacked(appChartConfig.isStacked());
                        oldChart.setSmooth(appChartConfig.isSmooth());
                        oldChart.setDeprecated(false);
                        environment().updateByIdVersion(oldChart);
                    }
                }
            }
        }

        // Install configured chart data sources
        environment().updateAll(new ChartDataSourceQuery().applicationId(applicationId).isNotActualCustom(),
                new Update().add("deprecated", Boolean.TRUE));
        if (applicationConfig.getChartDataSourcesConfig() != null
                && !DataUtils.isBlank(applicationConfig.getChartDataSourcesConfig().getChartDataSourceList())) {
            for (AppChartDataSourceConfig appChartDataSourceConfig : applicationConfig.getChartDataSourcesConfig()
                    .getChartDataSourceList()) {
                String description = resolveApplicationMessage(appChartDataSourceConfig.getDescription());
                logDebug(taskMonitor, "Installing chart chart data source [{0}]...", description);

                ChartDataSource oldChartDataSource = environment().findLean(new ChartDataSourceQuery()
                        .applicationId(applicationId).name(appChartDataSourceConfig.getName()));
                if (oldChartDataSource == null) {
                    ChartDataSource chartDataSource = new ChartDataSource();
                    chartDataSource.setApplicationId(applicationId);
                    chartDataSource.setType(appChartDataSourceConfig.getType());
                    chartDataSource.setTimeSeriesType(appChartDataSourceConfig.getTimeSeriesType());
                    chartDataSource.setCategoryField(appChartDataSourceConfig.getCategoryField());
                    chartDataSource.setEntity(appChartDataSourceConfig.getEntity());
                    chartDataSource.setName(appChartDataSourceConfig.getName());
                    chartDataSource.setDescription(description);
                    chartDataSource.setLimit(appChartDataSourceConfig.getLimit());
                    chartDataSource
                            .setCategoryBase(InputWidgetUtils.newAppFilter(appChartDataSourceConfig.getCategoryBase()));
                    chartDataSource
                            .setSeries(InputWidgetUtils.newAppPropertySequence(appChartDataSourceConfig.getSeries()));
                    chartDataSource.setCategories(
                            InputWidgetUtils.newAppPropertySequence(appChartDataSourceConfig.getCategories()));
                    chartDataSource.setFieldSequence(newAppFieldSequence(appChartDataSourceConfig.getFieldSequence()));
                    chartDataSource.setDeprecated(false);
                    chartDataSource.setConfigType(ConfigType.MUTABLE_INSTALL);
                    environment().create(chartDataSource);
                } else {
                    if (ConfigUtils.isSetInstall(oldChartDataSource)) {
                        oldChartDataSource.setType(appChartDataSourceConfig.getType());
                        oldChartDataSource.setTimeSeriesType(appChartDataSourceConfig.getTimeSeriesType());
                        oldChartDataSource.setCategoryField(appChartDataSourceConfig.getCategoryField());
                        oldChartDataSource.setEntity(appChartDataSourceConfig.getEntity());
                        oldChartDataSource.setDescription(description);
                        oldChartDataSource.setLimit(appChartDataSourceConfig.getLimit());
                        oldChartDataSource.setCategoryBase(
                                InputWidgetUtils.newAppFilter(appChartDataSourceConfig.getCategoryBase()));
                        oldChartDataSource.setSeries(
                                InputWidgetUtils.newAppPropertySequence(appChartDataSourceConfig.getSeries()));
                        oldChartDataSource.setCategories(
                                InputWidgetUtils.newAppPropertySequence(appChartDataSourceConfig.getCategories()));
                        oldChartDataSource.setFieldSequence(newAppFieldSequence(appChartDataSourceConfig.getFieldSequence()));
                        oldChartDataSource.setDeprecated(false);
                        environment().updateByIdVersion(oldChartDataSource);
                    }
                }
            }
        }

    }

    @Override
    public void restoreApplicationArtifacts(TaskMonitor taskMonitor, ApplicationRestore applicationRestore)
            throws UnifyException {
        final AppConfig applicationConfig = applicationRestore.getApplicationConfig();
        final Long applicationId = applicationRestore.getApplicationId();

        // Charts
        logDebug(taskMonitor, "Executing chart restore...");
        if (applicationConfig.getChartsConfig() != null
                && !DataUtils.isBlank(applicationConfig.getChartsConfig().getChartList())) {
            for (AppChartConfig appChartConfig : applicationConfig.getChartsConfig().getChartList()) {
                String description = resolveApplicationMessage(appChartConfig.getDescription());
                String title = resolveApplicationMessage(appChartConfig.getTitle());
                String subTitle = resolveApplicationMessage(appChartConfig.getSubTitle());
                logDebug(taskMonitor, "Restoring chart chart [{0}]...", description);
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
                chart.setCategory(appChartConfig.getCategory());
                chart.setColor(appChartConfig.getColor());
                chart.setSeries(appChartConfig.getSeries());
                chart.setShowGrid(appChartConfig.isShowGrid());
                chart.setShowDataLabels(appChartConfig.isShowDataLabels());
                chart.setFormatDataLabels(appChartConfig.isFormatDataLabels());
                chart.setFormatYLabels(appChartConfig.isFormatYLabels());
                chart.setStacked(appChartConfig.isStacked());
                chart.setSmooth(appChartConfig.isSmooth());
                chart.setDeprecated(false);
                chart.setConfigType(ConfigType.CUSTOM);
                environment().create(chart);
            }
        }

        // Chart data sources
        if (applicationConfig.getChartDataSourcesConfig() != null
                && !DataUtils.isBlank(applicationConfig.getChartDataSourcesConfig().getChartDataSourceList())) {
            for (AppChartDataSourceConfig appChartDataSourceConfig : applicationConfig.getChartDataSourcesConfig()
                    .getChartDataSourceList()) {
                String description = resolveApplicationMessage(appChartDataSourceConfig.getDescription());
                logDebug(taskMonitor, "Restoring chart chart data source [{0}]...", description);
                ChartDataSource chartDataSource = new ChartDataSource();
                chartDataSource.setApplicationId(applicationId);
                chartDataSource.setType(appChartDataSourceConfig.getType());
                chartDataSource.setTimeSeriesType(appChartDataSourceConfig.getTimeSeriesType());
                chartDataSource.setCategoryField(appChartDataSourceConfig.getCategoryField());
                chartDataSource.setEntity(appChartDataSourceConfig.getEntity());
                chartDataSource.setName(appChartDataSourceConfig.getName());
                chartDataSource.setDescription(description);
                chartDataSource.setLimit(appChartDataSourceConfig.getLimit());
                chartDataSource
                        .setCategoryBase(InputWidgetUtils.newAppFilter(appChartDataSourceConfig.getCategoryBase()));
                chartDataSource
                        .setSeries(InputWidgetUtils.newAppPropertySequence(appChartDataSourceConfig.getSeries()));
                chartDataSource.setCategories(
                        InputWidgetUtils.newAppPropertySequence(appChartDataSourceConfig.getCategories()));
                chartDataSource.setFieldSequence(newAppFieldSequence(appChartDataSourceConfig.getFieldSequence()));
                chartDataSource.setDeprecated(false);
                chartDataSource.setConfigType(ConfigType.CUSTOM);
                environment().create(chartDataSource);
            }
        }
    }

    @Override
    public void replicateApplicationArtifacts(TaskMonitor taskMonitor, Long srcApplicationId, Long destApplicationId,
            ApplicationReplicationContext ctx) throws UnifyException {
        // Charts
        logDebug(taskMonitor, "Replicating charts...");
        List<Long> chartIdList = environment().valueList(Long.class, "id",
                new ChartQuery().applicationId(srcApplicationId));
        for (Long chartId : chartIdList) {
            Chart srcChart = environment().find(Chart.class, chartId);
            String oldDescription = srcChart.getDescription();
            srcChart.setApplicationId(destApplicationId);
            srcChart.setName(ctx.nameSwap(srcChart.getName()));
            srcChart.setDescription(ctx.messageSwap(srcChart.getDescription()));
            srcChart.setTitle(ctx.messageSwap(srcChart.getTitle()));
            srcChart.setSubTitle(ctx.messageSwap(srcChart.getSubTitle()));
            srcChart.setProvider(ctx.componentSwap(srcChart.getProvider()));

            environment().create(srcChart);
            logDebug(taskMonitor, "Chart [{0}] -> [{1}]...", oldDescription, srcChart.getDescription());
        }
    }

    @Override
    protected List<DeletionParams> getDeletionParams() throws UnifyException {
        return Arrays.asList(new DeletionParams("charts", new ChartQuery()));
    }

    private AppFieldSequence newAppFieldSequence(FieldSequenceConfig fieldSequenceConfig) throws UnifyException {
        if (fieldSequenceConfig != null) {
            return new AppFieldSequence(InputWidgetUtils.getFieldSequenceDefinition(fieldSequenceConfig));
        }

        return null;
    }

}
