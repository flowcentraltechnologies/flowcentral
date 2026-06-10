/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityCategoryDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.EntitySeriesDef;
import com.flowcentraltech.flowcentral.application.data.FieldSequenceDef;
import com.flowcentraltech.flowcentral.application.data.FieldSequenceEntryDef;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.data.PropertySequenceEntryDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.chart.constants.ChartModuleErrorConstants;
import com.flowcentraltech.flowcentral.chart.constants.ChartModuleNameConstants;
import com.flowcentraltech.flowcentral.chart.data.CDSnapshot;
import com.flowcentraltech.flowcentral.chart.data.CDSnapshotCategory;
import com.flowcentraltech.flowcentral.chart.data.CDSnapshotSeries;
import com.flowcentraltech.flowcentral.chart.data.ChartCategory;
import com.flowcentraltech.flowcentral.chart.data.ChartConfiguration;
import com.flowcentraltech.flowcentral.chart.data.ChartDataSourceDef;
import com.flowcentraltech.flowcentral.chart.data.ChartDef;
import com.flowcentraltech.flowcentral.chart.data.ChartDetails;
import com.flowcentraltech.flowcentral.chart.data.ChartDetailsContext;
import com.flowcentraltech.flowcentral.chart.data.ChartSeries;
import com.flowcentraltech.flowcentral.chart.data.ChartViewOption;
import com.flowcentraltech.flowcentral.chart.entities.Chart;
import com.flowcentraltech.flowcentral.chart.entities.ChartDataSource;
import com.flowcentraltech.flowcentral.chart.entities.ChartDataSourceQuery;
import com.flowcentraltech.flowcentral.chart.entities.ChartDataSourceSnapshotQuery;
import com.flowcentraltech.flowcentral.chart.entities.ChartDatasourceSnapshot;
import com.flowcentraltech.flowcentral.chart.entities.ChartQuery;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.configuration.constants.ChartType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.application.InstallationContext;
import com.tcdng.unify.core.constant.FrequencyUnit;
import com.tcdng.unify.core.constant.TimeSeriesType;
import com.tcdng.unify.core.criterion.AggregateFunction;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.GroupingFunction;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.StaleableFactoryMap;
import com.tcdng.unify.core.database.Aggregation;
import com.tcdng.unify.core.database.Grouping;
import com.tcdng.unify.core.database.GroupingAggregation;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Default chart business service implementation.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Transactional
@Component(ChartModuleNameConstants.CHART_MODULE_SERVICE)
public class ChartModuleServiceImpl extends AbstractFlowCentralService implements ChartModuleService {

    private static final String DATASOURCE_LOCK_BASE = "CHARTDATASOURCE::";

    @Configurable
    private AppletUtilities appletUtilities;

    private final FactoryMap<String, ChartDef> chartDefFactoryMap;

    private final FactoryMap<String, ChartDataSourceDef> chartDataSourceDefFactoryMap;

    private final FactoryMap<String, ChartDetailsContext> chartDetailsContextFactoryMap;

    public ChartModuleServiceImpl() {
        this.chartDefFactoryMap = new StaleableFactoryMap<String, ChartDef>()
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
                            .height(DataUtils.convert(int.class, chart.getHeight())).stacked(chart.isStacked())
                            .smooth(chart.isSmooth());
                    return cdb.build();
                }

            };

        this.chartDataSourceDefFactoryMap = new StaleableFactoryMap<String, ChartDataSourceDef>()
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

                    ChartDataSourceDef chartDataSourceDef = new ChartDataSourceDef(chartDataSource.getType(), longName,
                            chartDataSource.getDescription(), appletUtilities.getEntityDef(chartDataSource.getEntity()),
                            InputWidgetUtils.getFilterDef(appletUtilities, null, chartDataSource.getCategoryBase()),
                            InputWidgetUtils.getPropertySequenceDef(chartDataSource.getSeries()),
                            InputWidgetUtils.getPropertySequenceDef(chartDataSource.getCategories()),
                            groupingFieldSequenceDef, chartDataSource.getCacheRefreshRate(), chartDataSource.getId(),
                            chartDataSource.getVersionNo());

                    // Expire snapshot
                    environment().updateAll(
                            new ChartDataSourceSnapshotQuery().chartDataSourceId(chartDataSourceDef.getId()),
                            new Update().add("snapshotExpiresOn", getNow()));
                    return chartDataSourceDef;
                }

            };

        this.chartDetailsContextFactoryMap = new StaleableFactoryMap<String, ChartDetailsContext>()
            {

                @Override
                protected boolean stale(String longName, ChartDetailsContext ctx) throws Exception {
                    return getNow().after(ctx.getSnapshotExpiresOn());
                }

                @Override
                protected ChartDetailsContext create(String longName, Object... args) throws Exception {
                    final ChartConfiguration chartConfiguration = (ChartConfiguration) args[0];
                    final ChartDef chartDef = getChartDef(chartConfiguration.getChart());
                    final ChartDataSourceDef chartDataSourceDef = getChartDataSourceDef(chartDef.getRule());
                    final EntityDef entityDef = chartDataSourceDef.getEntityDef();
                    final CDSnapshot cdSnapshot = getChartDatasourceSnapshot(chartDataSourceDef.getLongName(),
                            new ChartViewOption(chartConfiguration.getViewOptionName(),
                                    InputWidgetUtils.getRestriction(appletUtilities, entityDef, null,
                                            chartConfiguration.getViewOptionCatBase(chartDataSourceDef.getLongName()),
                                            getNow())));
                    return new ChartDetailsContext(cdSnapshot, chartDataSourceDef.getId());
                }

            };
    }

    @Override
    public ChartDetails getChartDetails(ChartConfiguration chartConfiguration) throws UnifyException {
        final ChartDef chartDef = getChartDef(chartConfiguration.getChart());
        final String viewOption = chartConfiguration.getViewOptionName();
        final ChartDetailsContext ctx = chartDetailsContextFactoryMap.get(chartDef.getRule() + "." + viewOption,
                chartConfiguration);

        final ChartType chartType = chartDef.getType();
        final List<String> cseries = chartDef.getSeries();
        final List<String> ccategories = chartDef.getCategories();
        if (chartType.isCard()) {
            return new ChartDetails(chartDef,
                    new ChartSeries[] { ctx.newChartSeries(ccategories.get(0), cseries.get(0)) },
                    new ChartCategory[] { ctx.newChartCategory(ccategories.get(0)) });
        } else if (chartType.isTable()) {
            final List<String> gcseries = new ArrayList<String>(ctx.getGroupingNames());
            gcseries.addAll(cseries);
            return new ChartDetails(chartDef, ctx.newChartSeries(ccategories.get(0), gcseries),
                    new ChartCategory[] { ctx.newChartCategory(ccategories.get(0)) });
        } else if (chartType.triggerAxisChart()) {
            if (ctx.isWithGrouping()) {
                if (ctx.isDatetimeGrouping()) {

                } else {

                }
            } else {
                return new ChartDetails(chartDef, ctx.newChartSeriesAcross(ccategories, cseries),
                        ctx.newChartCategories(ccategories));
            }
        } else if (chartType.isCircularChart() || chartType.isPolarChart()) {
            return new ChartDetails(chartDef,
                    new ChartSeries[] { ctx.newChartSeriesAcross(ccategories, cseries.get(0)) },
                    ctx.newChartCategories(ccategories));
        } else if (chartType.isRadarChart()) {
            return new ChartDetails(chartDef, ctx.newChartSeriesAcross(ccategories, cseries),
                    ctx.newChartCategories(ccategories));
        }

        return ChartDetails.BLANK;
    }

    @Override
    public CDSnapshot getChartDatasourceSnapshot(String chartDatasourceName, ChartViewOption chartViewOption)
            throws UnifyException {
        final ChartDataSourceDef chartDataSourceDef = getChartDataSourceDef(chartDatasourceName);
        final Date now = getNow();
        ChartDatasourceSnapshot chartDatasourceSnapshot = environment()
                .find(new ChartDataSourceSnapshotQuery().chartDataSourceId(chartDataSourceDef.getId()));
        if (chartDatasourceSnapshot == null || now.after(chartDatasourceSnapshot.getSnapshotExpiresOn())) {
            final String lockName = DATASOURCE_LOCK_BASE + chartDatasourceName + "." + chartViewOption.getName();
            grabLock(lockName);
            try {
                chartDatasourceSnapshot = environment()
                        .find(new ChartDataSourceSnapshotQuery().chartDataSourceId(chartDataSourceDef.getId()));
                if (chartDatasourceSnapshot == null || now.after(chartDatasourceSnapshot.getSnapshotExpiresOn())) {
                    final CDSnapshot cdSnapshot = new CDSnapshot();
                    cdSnapshot.setChartDatasourceName(chartDatasourceName);
                    cdSnapshot.setView(chartViewOption.getName());

                    if (chartDatasourceSnapshot != null) {
                        environment().delete(chartDatasourceSnapshot);
                    }

                    String[] groupingNames = new String[0];
                    if (chartDataSourceDef.isWithSeries()) {
                        final EntityDef entityDef = chartDataSourceDef.getEntityDef();
                        final List<AggregateFunction> aggregateFunctions = new ArrayList<AggregateFunction>();
                        for (PropertySequenceEntryDef sequenceDef : chartDataSourceDef.getSeries().getSequenceList()) {
                            EntitySeriesDef entitySeriesDef = entityDef.getEntitySeriesDef(sequenceDef.getProperty());
                            aggregateFunctions.add(entitySeriesDef.getType().function(entitySeriesDef.getName(),
                                    entitySeriesDef.getFieldName(),
                                    entitySeriesDef.getLabel() != null ? entitySeriesDef.getLabel()
                                            : entitySeriesDef.getName()));
                        }

                        final FilterDef catBaseFilterDef = chartDataSourceDef.getCategoryBase();
                        Restriction baseRestriction = InputWidgetUtils.getRestriction(appletUtilities, entityDef, null,
                                catBaseFilterDef, now);

                        if (chartViewOption.isWithRestriction()) {
                            baseRestriction = baseRestriction == null ? chartViewOption.getRestriction()
                                    : new And().add(baseRestriction).add(chartViewOption.getRestriction());
                        }

                        List<GroupingFunction> groupingFunctions = Collections.emptyList();
                        if (chartDataSourceDef.isWithGroupingFields()) {
                            groupingFunctions = new ArrayList<GroupingFunction>();
                            final int glen = chartDataSourceDef.getGroupingFieldSequenceDef().getFieldSequenceList()
                                    .size();
                            groupingNames = new String[glen];
                            int g = 0;
                            for (FieldSequenceEntryDef fieldSequenceDef : chartDataSourceDef
                                    .getGroupingFieldSequenceDef().getFieldSequenceList()) {
                                final String grpName = "group" + g;
                                final String fieldName = fieldSequenceDef.getFieldName();
                                final EntityFieldDef entityFieldDef = entityDef.getFieldDef(fieldName);
                                final GroupingFunction _groupingFunction = entityFieldDef.isDateTime()
                                        ? new GroupingFunction(grpName, fieldName, entityFieldDef.getFieldLabel(),
                                                fieldSequenceDef.isWithParam()
                                                        ? TimeSeriesType.fromCode(fieldSequenceDef.getParam())
                                                        : TimeSeriesType.DAY)
                                        : new GroupingFunction(fieldName, entityFieldDef.getFieldLabel());
                                groupingFunctions.add(_groupingFunction);
                                g++;
                            }
                        }

                        cdSnapshot.setGroupingNames(groupingNames);

                        CDSnapshotCategory[] categories = null;
                        if (chartDataSourceDef.isWithCategories()) {
                            categories = new CDSnapshotCategory[chartDataSourceDef.getCategories().getSequenceList()
                                    .size()];
                            int i = 0;
                            for (PropertySequenceEntryDef propertySequenceEntryDef : chartDataSourceDef.getCategories()
                                    .getSequenceList()) {
                                final EntityCategoryDef entityCategoryDef = entityDef
                                        .getEntityCategorysDef(propertySequenceEntryDef.getProperty());
                                final String cat = entityCategoryDef.getName();
                                final String catlabel = !StringUtils.isBlank(propertySequenceEntryDef.getLabel())
                                        ? propertySequenceEntryDef.getLabel()
                                        : entityCategoryDef.getLabel();

                                final FilterDef enFilterDef = entityCategoryDef.getFilterDef();
                                Restriction restriction = InputWidgetUtils.getRestriction(appletUtilities, entityDef,
                                        null, enFilterDef, now);
                                if (baseRestriction != null) {
                                    restriction = restriction == null ? baseRestriction
                                            : new And().add(baseRestriction).add(restriction);
                                }

                                categories[i] = getChartDatasourceSnapshotSeries(cdSnapshot, entityDef,
                                        aggregateFunctions, restriction, groupingFunctions, cat, catlabel);
                                i++;
                            }
                        } else {
                            categories = new CDSnapshotCategory[1];
                            categories[0] = getChartDatasourceSnapshotSeries(cdSnapshot, entityDef, aggregateFunctions,
                                    baseRestriction, groupingFunctions, ChartModuleNameConstants.CHART_DEFAULT_VIEW_NAME, "Default");
                        }

                        cdSnapshot.setCategories(categories);
                    }

                    final Date takenOn = getNow();
                    final Date expiresOn = CalendarUtils.getDateWithFrequencyOffset(takenOn, FrequencyUnit.SECOND,
                            chartDataSourceDef.getCacheRefreshRate().seconds());
                    cdSnapshot.setTakenOn(takenOn);
                    cdSnapshot.setExpiresOn(expiresOn);

                    chartDatasourceSnapshot = new ChartDatasourceSnapshot();
                    chartDatasourceSnapshot.setChartDataSourceId(chartDataSourceDef.getId());
                    chartDatasourceSnapshot.setViewOption(chartViewOption.getName());
                    chartDatasourceSnapshot.setSnapshot(DataUtils.asJsonString(cdSnapshot));
                    chartDatasourceSnapshot.setSnapshotExpiresOn(expiresOn);
                    environment().create(chartDatasourceSnapshot);
                    return cdSnapshot;
                }
            } finally {
                releaseLock(lockName);
            }
        }

        return DataUtils.fromJsonString(CDSnapshot.class, chartDatasourceSnapshot.getSnapshot());
    }

    private CDSnapshotCategory getChartDatasourceSnapshotSeries(CDSnapshot cdSnapshot, EntityDef entityDef,
            List<AggregateFunction> aggregateFunctions, Restriction restriction,
            List<GroupingFunction> groupingFunctions, String catName, String catLabel) throws UnifyException {
        final CDSnapshotCategory cdSnapshotCategory = new CDSnapshotCategory();
        cdSnapshotCategory.setCat(catName);
        cdSnapshotCategory.setLbl(catLabel);

        final Query<?> query = appletUtilities.application().queryOf(entityDef.getLongName()).ignoreEmptyCriteria(true);
        if (restriction != null) {
            query.addRestriction(restriction);
        }

        int groupingStart = 0;
        boolean datetimeGrouping = false;
        boolean numericMerged = false;
        final List<GroupingAggregation> aggregations = environment().aggregate(aggregateFunctions, query,
                groupingFunctions);
        final int seriesLen = aggregateFunctions.size() + groupingFunctions.size();
        final int dataLen = aggregations.size();
        final CDSnapshotSeries[] series = new CDSnapshotSeries[seriesLen];
        for (int i = 0; i < dataLen; i++) {
            final GroupingAggregation groupingAggregation = aggregations.get(i);
            int j = 0;
            for (Aggregation aggregation : groupingAggregation.getAggregation()) {
                CDSnapshotSeries _series = series[j];
                if (_series == null) {
                    _series = series[j] = new CDSnapshotSeries();
                    _series.setTy(entityDef.getFieldDef(aggregation.getFieldName()).getDataType().code());
                    _series.setNm(aggregation.getName());
                    _series.setLbl(aggregation.getFieldLabel());
                    _series.setFld(aggregation.getFieldName());
                    _series.setGrouping(0);
                    _series.setTime(false);
                    _series.setVals(new String[dataLen]);
                }

                if (aggregation.isWithValue()) {
                    _series.getVals()[i] = String.valueOf(aggregation.getValue());
                }

                j++;
            }

            for (Grouping grouping : groupingAggregation.getGroupings()) {
                CDSnapshotSeries _series = series[j];
                if (_series == null) {
                    groupingStart = j;
                    final EntityFieldDataType otype = entityDef.getFieldDef(grouping.getFieldName()).getDataType();
                    final EntityFieldDataType type = (numericMerged = (datetimeGrouping = otype.isDatetime())
                            && grouping.isString()) ? EntityFieldDataType.STRING : otype;
                    _series = series[j] = new CDSnapshotSeries();
                    _series.setTy(type.code());
                    _series.setNm(grouping.getName());
                    _series.setLbl(grouping.getFieldLabel());
                    _series.setFld(grouping.getFieldName());
                    _series.setGrouping(grouping.isString() ? 1 : 2);
                    _series.setTime(otype.isDatetime());
                    _series.setVals(new String[dataLen]);
                }

                if (grouping.isWithGrouping()) {
                    _series.getVals()[i] = grouping.isDate() ? String.valueOf(grouping.getAsDate().getTime())
                            : grouping.getAsString();
                }

                j++;
            }
        }

        cdSnapshot.setGroupingStart(groupingStart);
        cdSnapshot.setDatetimeGrouping(datetimeGrouping);
        cdSnapshot.setNumericMerged(numericMerged);

        cdSnapshotCategory.setSeries(series);
        return cdSnapshotCategory;
    }

    @Override
    public void clearDefinitionsCache() throws UnifyException {
        logDebug("Clearing definitions cache...");
        chartDefFactoryMap.clear();
        chartDataSourceDefFactoryMap.clear();
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
    protected void doInstallModuleFeatures(InstallationContext ctx, ModuleInstall moduleInstall) throws UnifyException {

    }

}
