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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.FieldSequenceDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.chart.constants.ChartModuleErrorConstants;
import com.flowcentraltech.flowcentral.chart.constants.ChartModuleNameConstants;
import com.flowcentraltech.flowcentral.chart.data.ChartDataSourceDef;
import com.flowcentraltech.flowcentral.chart.data.ChartDef;
import com.flowcentraltech.flowcentral.chart.data.ChartDetails;
import com.flowcentraltech.flowcentral.chart.data.ChartSnapshotDef;
import com.flowcentraltech.flowcentral.chart.entities.Chart;
import com.flowcentraltech.flowcentral.chart.entities.ChartDataSource;
import com.flowcentraltech.flowcentral.chart.entities.ChartDataSourceQuery;
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
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Default chart business service implementation.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(ChartModuleNameConstants.CHART_MODULE_SERVICE)
public class ChartModuleServiceImpl extends AbstractFlowCentralService implements ChartModuleService {

    @Configurable
    private AppletUtilities appletUtilities;

    private FactoryMap<String, ChartDef> chartDefFactoryMap;

    private FactoryMap<String, ChartDataSourceDef> chartDataSourceDefFactoryMap;

    private FactoryMap<String, ChartSnapshotDef> chartSnapshotDefFactoryMap;

    public ChartModuleServiceImpl() {
        this.chartDefFactoryMap = new FactoryMap<String, ChartDef>(true)
            {

                @Override
                protected boolean stale(String chartName, ChartDef chartDef) throws Exception {
                    return isStale(new ChartQuery(), chartDef);
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
                    cdb.title(chart.getTitle()).subTitle(chart.getSubTitle()).category(chart.getCategory())
                            .series(chart.getSeries()).color(chart.getColor())
                            .width(DataUtils.convert(int.class, chart.getWidth()))
                            .height(DataUtils.convert(int.class, chart.getHeight())).showGrid(chart.isShowGrid())
                            .showDataLabels(chart.isShowDataLabels()).formatDataLabels(chart.isFormatDataLabels())
                            .formatYLabels(chart.isFormatYLabels()).stacked(chart.isStacked()).smooth(chart.isSmooth());
                    return cdb.build();
                }

            };

        this.chartDataSourceDefFactoryMap = new FactoryMap<String, ChartDataSourceDef>(true)
            {

                @Override
                protected boolean stale(String longName, ChartDataSourceDef chartDataSourceDef) throws Exception {
                    return isStale(new ChartDataSourceQuery(), chartDataSourceDef);
                }

                @Override
                protected ChartDataSourceDef create(String longName, Object... arg1) throws Exception {
                    ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
                    ChartDataSource chartDataSource = environment().list(new ChartDataSourceQuery()
                            .applicationName(nameParts.getApplicationName()).name(nameParts.getEntityName()));
                    if (chartDataSource == null) {
                        throw new UnifyException(ChartModuleErrorConstants.CANNOT_FIND_APPLICATION_CHART, longName);
                    }

                    FieldSequenceDef groupingFieldSequenceDef = InputWidgetUtils
                            .getFieldSequenceDef(chartDataSource.getFieldSequence());

                    ChartDataSourceDef chartDataSourceDef = new ChartDataSourceDef(chartDataSource.getType(),
                            chartDataSource.getTimeSeriesType(), longName, chartDataSource.getDescription(),
                            chartDataSource.getCategoryField(),
                            appletUtilities.getEntityDef(chartDataSource.getEntity()),
                            InputWidgetUtils.getFilterDef(appletUtilities, null, chartDataSource.getCategoryBase()),
                            InputWidgetUtils.getPropertySequenceDef(chartDataSource.getSeries()),
                            InputWidgetUtils.getPropertySequenceDef(chartDataSource.getCategories()),
                            groupingFieldSequenceDef, chartDataSource.getLimit(), chartDataSource.getId(),
                            chartDataSource.getVersionNo());
                    return chartDataSourceDef;
                }

            };

        this.chartSnapshotDefFactoryMap = new FactoryMap<String, ChartSnapshotDef>(true)
            {

                @Override
                protected boolean stale(String chartSnapshotName, ChartSnapshotDef chartSnapshotDef) throws Exception {
                    return isStale(new ChartSnapshotQuery(), chartSnapshotDef);
                }

                @Override
                protected ChartSnapshotDef create(String chartSnapshotName, Object... arg1) throws Exception {
                    ChartSnapshot chartSnapshot = environment().list(new ChartSnapshotQuery().name(chartSnapshotName));
                    if (chartSnapshot == null) {
                        throw new UnifyException(ChartModuleErrorConstants.CANNOT_FIND_CHART_SNAPSHOT,
                                chartSnapshotName);
                    }

                    ChartDetails.Builder cdb = ChartDetails.newBuilder(chartSnapshot.getCategoryDataType());
                    String[] _categories = DataUtils.arrayFromJsonString(String[].class, chartSnapshot.getCategories());
                    for (ChartSnapshotSeries series : chartSnapshot.getSeriesList()) {
                        cdb.createSeries(series.getSeriesDataType(), series.getName());
                        Number[] _nseries = series.getSeriesDataType().isInteger()
                                ? DataUtils.arrayFromJsonString(Integer[].class, series.getSeries())
                                : DataUtils.arrayFromJsonString(Double[].class, series.getSeries());
                        if (_nseries != null) {
                            for (int i = 0; i < _nseries.length; i++) {
                                cdb.addSeriesData(series.getName(), _categories[i], _nseries[i]);
                            }
                        }
                    }

                    ChartDetails chartDetails = cdb.build();
                    return new ChartSnapshotDef(chartSnapshot.getName(), chartSnapshot.getDescription(), chartDetails,
                            chartSnapshot.getId(), chartSnapshot.getVersionNo());
                }

            };

    }

    @Override
    public void clearDefinitionsCache() throws UnifyException {
        logDebug("Clearing definitions cache...");
        chartDefFactoryMap.clear();
        chartDataSourceDefFactoryMap.clear();
        chartSnapshotDefFactoryMap.clear();
        logDebug("Definitions cache clearing successfully completed.");
    }

    @Override
    public void clearChartCache() throws UnifyException {

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
    public List<Long> findCustomChartIdList(String applicationName) throws UnifyException {
        return environment().valueList(Long.class, "id", new ChartQuery().applicationName(applicationName).isCustom());
    }

    @Override
    public List<ChartDataSource> findChartDataSources(ChartDataSourceQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public ChartDataSource findChartDataSource(Long chartDataSourceId) throws UnifyException {
        return environment().find(ChartDataSource.class, chartDataSourceId);
    }

    @Override
    public List<Long> findCustomChartDataSourceIdList(String applicationName) throws UnifyException {
        return environment().valueList(Long.class, "id",
                new ChartDataSourceQuery().applicationName(applicationName).isCustom());
    }

    @Override
    public List<ChartSnapshot> findChartSnapshots(ChartSnapshotQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public List<ChartDef> findChartDefs(String applicationName) throws UnifyException {
        List<String> chartNames = environment().valueList(String.class, "name",
                new ChartQuery().applicationName(applicationName));
        if (!DataUtils.isBlank(chartNames)) {
            List<ChartDef> resultList = new ArrayList<ChartDef>();
            for (String chartName : chartNames) {
                resultList.add(chartDefFactoryMap
                        .get(ApplicationNameUtils.getApplicationEntityLongName(applicationName, chartName)));
            }

            return resultList;
        }

        return Collections.emptyList();
    }

    @Override
    public ChartDef getChartDef(String chartName) throws UnifyException {
        return chartDefFactoryMap.get(chartName);
    }

    @Override
    public ChartDataSourceDef getChartDataSourceDef(String chartDataSourceName) throws UnifyException {
        return chartDataSourceDefFactoryMap.get(chartDataSourceName);
    }

    @Override
    public ChartSnapshotDef getChartSnapshotDef(String snapshotName) throws UnifyException {
        return chartSnapshotDefFactoryMap.get(snapshotName);
    }

    @Override
    public List<? extends Listable> getEntityChartDataSources(String entity) throws UnifyException {
        List<ChartDataSource> list = environment().listAll(new ChartDataSourceQuery().entity(entity)
                .addSelect("name", "description", "applicationName").addOrder("description"));
        return ApplicationNameUtils.getListableList(list);
    }

    @Override
    public List<? extends Listable> getChartDataSourceListForCharts(List<String> charts) throws UnifyException {
        if (!DataUtils.isBlank(charts)) {
            Set<String> chartDataSources = new HashSet<String>();
            for (String chart : charts) {
                ApplicationEntityNameParts parts = ApplicationNameUtils.getApplicationEntityNameParts(chart);
                final String chartDataSource = environment().value(String.class, "rule",
                        new ChartQuery().provider(ChartModuleNameConstants.CHARTDATASOURCE_PROVIDER)
                                .applicationName(parts.getApplicationName()).name(parts.getEntityName()));
                chartDataSources.add(chartDataSource);
            }

            if (!chartDataSources.isEmpty()) {
                List<ListData> resultList = new ArrayList<ListData>();
                for (String chartDataSource : chartDataSources) {
                    ApplicationEntityNameParts parts = ApplicationNameUtils
                            .getApplicationEntityNameParts(chartDataSource);
                    final String description = environment().value(String.class, "description",
                            new ChartDataSourceQuery().applicationName(parts.getApplicationName())
                                    .name(parts.getEntityName()));
                    resultList.add(new ListData(chartDataSource, description));
                }

                return resultList;
            }
        }

        return Collections.emptyList();
    }

    @Override
    public List<? extends Listable> getChartDataSourceListByEntity(String entity) throws UnifyException {
        List<ChartDataSource> sources = environment()
                .listAll(new ChartDataSourceQuery().entity(entity).addSelect("applicationName", "name", "description"));
        return ApplicationNameUtils.getListableList(sources);
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
                    new ChartSnapshotSeries(ChartSeriesDataType.INTEGER, "Sales",
                            "[6000,4050,3820,5000,7200,5580,6240]"),
                    new ChartSnapshotSeries(ChartSeriesDataType.INTEGER, "Costs",
                            "[2500,400,3500,3840,4000,5830,5000]")));
            environment().create(chartSnapshot);
        }

    }

}
