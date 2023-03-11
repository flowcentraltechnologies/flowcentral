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
package com.flowcentraltech.flowcentral.chart.business;

import java.util.Arrays;
import java.util.List;

import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.chart.constants.ChartModuleErrorConstants;
import com.flowcentraltech.flowcentral.chart.constants.ChartModuleNameConstants;
import com.flowcentraltech.flowcentral.chart.data.ChartData;
import com.flowcentraltech.flowcentral.chart.data.ChartDef;
import com.flowcentraltech.flowcentral.chart.data.ChartSnapshotDef;
import com.flowcentraltech.flowcentral.chart.entities.Chart;
import com.flowcentraltech.flowcentral.chart.entities.ChartQuery;
import com.flowcentraltech.flowcentral.chart.entities.ChartSnapshot;
import com.flowcentraltech.flowcentral.chart.entities.ChartSnapshotQuery;
import com.flowcentraltech.flowcentral.chart.entities.ChartSnapshotSeries;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.configuration.constants.ChartCategoryDataType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartSeriesDataType;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.data.FactoryMap;

/**
 * Default chart business service implementation.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(ChartModuleNameConstants.CHART_MODULE_SERVICE)
public class ChartModuleServiceImpl extends AbstractFlowCentralService implements ChartModuleService {

    private FactoryMap<String, ChartDef> chartDefFactoryMap;

    private FactoryMap<String, ChartSnapshotDef> chartSnapshotDefFactoryMap;

    public ChartModuleServiceImpl() {
        this.chartDefFactoryMap = new FactoryMap<String, ChartDef>(true)
            {
                @Override
                protected boolean stale(String chartName, ChartDef chartDef) throws Exception {
                    return environment().value(long.class, "versionNo", new ChartQuery().id(chartDef.getId())) > chartDef
                            .getVersion();
                }

                @Override
                protected ChartDef create(String longName, Object... arg1) throws Exception {
                    ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
                    Chart chart = environment().list(new ChartQuery().applicationName(nameParts.getApplicationName())
                            .name(nameParts.getEntityName()));
                    if (chart == null) {
                        throw new UnifyException(ChartModuleErrorConstants.CANNOT_FIND_APPLICATION_CHART, longName);
                    }

                    ChartDef.Builder cdb = ChartDef.newBuilder(chart.getType(), chart.getPaletteType(),
                            chart.getProvider(), chart.getRule(), longName, chart.getDescription(), chart.getId(),
                            chart.getVersionNo());
                    cdb.title(chart.getTitle()).subTitle(chart.getSubTitle()).width(chart.getWidth())
                            .height(chart.getHeight()).showGrid(chart.isShowGrid())
                            .showDataLabels(chart.isShowDataLabels()).stacked(chart.isStacked())
                            .smooth(chart.isSmooth());
                    return cdb.build();
                }

            };

        this.chartSnapshotDefFactoryMap = new FactoryMap<String, ChartSnapshotDef>(true)
            {
                @Override
                protected boolean stale(String chartSnapshotName, ChartSnapshotDef chartSnapshotDef) throws Exception {
                    return environment().value(long.class, "versionNo",
                            new ChartSnapshotQuery().id(chartSnapshotDef.getId())) > chartSnapshotDef.getVersion();
                }

                @Override
                protected ChartSnapshotDef create(String chartSnapshotName, Object... arg1) throws Exception {
                    ChartSnapshot chartSnapshot = environment().list(new ChartSnapshotQuery().name(chartSnapshotName));
                    if (chartSnapshot == null) {
                        throw new UnifyException(ChartModuleErrorConstants.CANNOT_FIND_CHART_SNAPSHOT,
                                chartSnapshotName);
                    }

                    ChartData.Builder cdb = ChartData.newBuilder();
                    cdb.categories(chartSnapshot.getCategoryDataType(), chartSnapshot.getCategories());
                    for (ChartSnapshotSeries series : chartSnapshot.getSeriesList()) {
                        cdb.addSeries(series.getSeriesDataType(), series.getName(), series.getSeries());
                    }

                    ChartData chartData = cdb.build();
                    return new ChartSnapshotDef(chartSnapshot.getName(), chartSnapshot.getDescription(), chartData,
                            chartSnapshot.getId(), chartSnapshot.getVersionNo());
                }

            };

    }

    @Override
    public List<Chart> findCharts(ChartQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public Chart findChart(Long chartId) throws UnifyException {
        return environment().find(Chart.class, chartId);
    }

    @Override
    public List<Long> findChartIdList(String applicationName) throws UnifyException {
        return environment().valueList(Long.class, "id", new ChartQuery().applicationName(applicationName));
    }

    @Override
    public List<ChartSnapshot> findChartSnapshots(ChartSnapshotQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public ChartDef getChartDef(String chartName) throws UnifyException {
        return chartDefFactoryMap.get(chartName);
    }

    @Override
    public ChartSnapshotDef getChartSnapshotDef(String snapshotName) throws UnifyException {
        return chartSnapshotDefFactoryMap.get(snapshotName);
    }

    @Override
    public boolean isChartSnapshotExist(String snapshotName) throws UnifyException {
        return environment().countAll(new ChartSnapshotQuery().name(snapshotName)) > 0;
    }

    @Override
    public void saveChartSnapshot(ChartSnapshot chartSnapshot) throws UnifyException {
        if (environment().countAll(new ChartSnapshotQuery().name(chartSnapshot.getName())) == 0) {
            environment().create(chartSnapshot);
        } else {
            environment().updateByIdVersion(chartSnapshot);
        }
    }

    @Override
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {
        if (environment().countAll(new ChartSnapshotQuery().name("sampleSalesChartSnapshot")) == 0) {
            ChartSnapshot chartSnapshot = new ChartSnapshot(ChartCategoryDataType.STRING, "sampleSalesChartSnapshot",
                    "Sample Sales Chart Snapshot",
                    "[\"Sunday\", \"Monday\", \"Tuesday\", \"Wednesday\", \"Thursday\", \"Friday\", \"Saturday\"]");
            chartSnapshot.setSeriesList(Arrays
                    .asList(new ChartSnapshotSeries(ChartSeriesDataType.INTEGER, "Sales", "[60,40,30,50,70,55,62]")));
            environment().create(chartSnapshot);
        }

        if (environment().countAll(new ChartSnapshotQuery().name("sampleCostsChartSnapshot")) == 0) {
            ChartSnapshot chartSnapshot = new ChartSnapshot(ChartCategoryDataType.STRING, "sampleCostsChartSnapshot",
                    "Sample Costs Chart Snapshot",
                    "[\"Sunday\", \"Monday\", \"Tuesday\", \"Wednesday\", \"Thursday\", \"Friday\", \"Saturday\"]");
            chartSnapshot.setSeriesList(Arrays
                    .asList(new ChartSnapshotSeries(ChartSeriesDataType.INTEGER, "Costs", "[25,40,35,38,40,58,50]")));
            environment().create(chartSnapshot);
        }

        if (environment().countAll(new ChartSnapshotQuery().name("sampleSalesAndCostsChartSnapshot")) == 0) {
            ChartSnapshot chartSnapshot = new ChartSnapshot(ChartCategoryDataType.STRING,
                    "sampleSalesAndCostsChartSnapshot", "Sample Sales and Costs Chart Snapshot",
                    "[\"Sunday\", \"Monday\", \"Tuesday\", \"Wednesday\", \"Thursday\", \"Friday\", \"Saturday\"]");
            chartSnapshot.setSeriesList(Arrays.asList(
                    new ChartSnapshotSeries(ChartSeriesDataType.INTEGER, "Sales", "[60,40,30,50,70,55,62]"),
                    new ChartSnapshotSeries(ChartSeriesDataType.INTEGER, "Costs", "[25,40,35,38,40,58,50]")));
            environment().create(chartSnapshot);
        }

    }

}
