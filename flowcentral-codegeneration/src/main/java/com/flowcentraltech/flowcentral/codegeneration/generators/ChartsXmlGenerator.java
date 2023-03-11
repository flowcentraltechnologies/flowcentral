/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.chart.business.ChartModuleService;
import com.flowcentraltech.flowcentral.chart.entities.Chart;
import com.flowcentraltech.flowcentral.configuration.xml.AppChartConfig;
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
public class ChartsXmlGenerator extends AbstractStaticArtifactGenerator {

    @Configurable
    private ChartModuleService chartModuleService;

    public void setChartModuleService(ChartModuleService chartModuleService) {
        this.chartModuleService = chartModuleService;
    }

    @Override
    protected void doGenerate(ExtensionModuleStaticFileBuilderContext ctx, String applicationName, ZipOutputStream out)
            throws UnifyException {
        List<Long> chartIdList = chartModuleService.findChartIdList(applicationName);
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
                appChartConfig.setShowGrid(chart.isShowGrid());
                appChartConfig.setShowDataLabels(chart.isShowDataLabels());
                appChartConfig.setStacked(chart.isStacked());
                appChartConfig.setSmooth(chart.isSmooth());

                chartConfigList.add(appChartConfig);
            }

            appChartsConfig.setChartList(chartConfigList);
            ctx.setChartsConfig(appChartsConfig);
        }

    }

}
