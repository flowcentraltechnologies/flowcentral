/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
import java.util.zip.ZipOutputStream;

import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.chart.business.ChartModuleService;
import com.flowcentraltech.flowcentral.chart.entities.Chart;
import com.flowcentraltech.flowcentral.chart.entities.ChartDataSource;
import com.flowcentraltech.flowcentral.configuration.xml.AppChartConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppChartDataSourceConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppChartDataSourcesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppChartsConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Charts XML Generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("charts-xml-generator")
public class ChartsXmlGenerator extends AbstractStaticModuleArtifactGenerator {

    @Configurable
    private ChartModuleService chartModuleService;

    @Override
    protected void doGenerate(ExtensionModuleStaticFileBuilderContext ctx, String applicationName, ZipOutputStream out)
            throws UnifyException {
        List<Long> chartIdList = chartModuleService.findCustomChartIdList(applicationName);
        if (!DataUtils.isBlank(chartIdList)) {
            final String lowerCaseApplicationName = applicationName.toLowerCase();

            AppChartsConfig appChartsConfig = new AppChartsConfig();
            List<AppChartConfig> chartConfigList = new ArrayList<AppChartConfig>();
            for (Long chartId : chartIdList) {
                AppChartConfig appChartConfig = new AppChartConfig();
                Chart chart = chartModuleService.findChart(chartId);
                String descKey = getDescriptionKey(lowerCaseApplicationName, "chart", chart.getName());
                String titleKey = descKey + ".title";
                String subTitleKey = descKey + ".subtitle";
                ctx.addMessage(StaticMessageCategoryType.CHART, descKey, chart.getDescription());
                ctx.addMessage(StaticMessageCategoryType.CHART, titleKey, chart.getTitle());
                ctx.addMessage(StaticMessageCategoryType.CHART, subTitleKey, chart.getSubTitle());

                appChartConfig.setType(chart.getType());
                appChartConfig.setPaletteType(chart.getPaletteType());
                appChartConfig.setName(chart.getName());
                appChartConfig.setDescription("$m{" + descKey + "}");
                appChartConfig.setTitle("$m{" + titleKey + "}");
                appChartConfig.setSubTitle("$m{" + subTitleKey + "}");
                appChartConfig.setWidth(chart.getWidth());
                appChartConfig.setHeight(chart.getHeight());
                appChartConfig.setProvider(chart.getProvider());
                appChartConfig.setRule(chart.getRule());
                appChartConfig.setCategory(chart.getCategory());
                appChartConfig.setSeries(chart.getSeries());
                appChartConfig.setColor(chart.getColor());
                appChartConfig.setShowGrid(chart.isShowGrid());
                appChartConfig.setShowDataLabels(chart.isShowDataLabels());
                appChartConfig.setFormatDataLabels(chart.isFormatDataLabels());
                appChartConfig.setFormatYLabels(chart.isFormatYLabels());
                appChartConfig.setStacked(chart.isStacked());
                appChartConfig.setSmooth(chart.isSmooth());

                chartConfigList.add(appChartConfig);
            }

            appChartsConfig.setChartList(chartConfigList);
            ctx.setChartsConfig(appChartsConfig);
        }

        List<Long> chartDataSourceIdList = chartModuleService.findCustomChartDataSourceIdList(applicationName);
        if (!DataUtils.isBlank(chartDataSourceIdList)) {
            final String lowerCaseApplicationName = applicationName.toLowerCase();

            AppChartDataSourcesConfig appChartDataSourcesConfig = new AppChartDataSourcesConfig();
            List<AppChartDataSourceConfig> chartDataSourceConfigList = new ArrayList<AppChartDataSourceConfig>();
            for (Long chartDataSourceId : chartDataSourceIdList) {
                AppChartDataSourceConfig appChartDataSourceConfig = new AppChartDataSourceConfig();
                ChartDataSource chartDataSource = chartModuleService.findChartDataSource(chartDataSourceId);
                String descKey = getDescriptionKey(lowerCaseApplicationName, "chartdatasource",
                        chartDataSource.getName());
                ctx.addMessage(StaticMessageCategoryType.CHART, descKey, chartDataSource.getDescription());

                appChartDataSourceConfig.setType(chartDataSource.getType());
                appChartDataSourceConfig.setLimit(chartDataSource.getLimit());
                appChartDataSourceConfig.setTimeSeriesType(chartDataSource.getTimeSeriesType());
                appChartDataSourceConfig.setCategoryField(chartDataSource.getCategoryField());
                appChartDataSourceConfig.setName(chartDataSource.getName());
                appChartDataSourceConfig.setDescription("$m{" + descKey + "}");
                appChartDataSourceConfig.setEntity(chartDataSource.getEntity());
                appChartDataSourceConfig
                        .setCategoryBase(InputWidgetUtils.getFilterConfig(au(), chartDataSource.getCategoryBase()));
                appChartDataSourceConfig
                        .setSeries(InputWidgetUtils.getPropertySequenceConfig(chartDataSource.getSeries()));
                appChartDataSourceConfig
                        .setCategories(InputWidgetUtils.getPropertySequenceConfig(chartDataSource.getCategories()));
                appChartDataSourceConfig.setFieldSequence(
                        InputWidgetUtils.getFieldSequenceConfig(chartDataSource.getFieldSequence()));
                chartDataSourceConfigList.add(appChartDataSourceConfig);
            }

            appChartDataSourcesConfig.setChartDataSourceList(chartDataSourceConfigList);
            ctx.setChartDataSourcesConfig(appChartDataSourcesConfig);
        }
    }

}
