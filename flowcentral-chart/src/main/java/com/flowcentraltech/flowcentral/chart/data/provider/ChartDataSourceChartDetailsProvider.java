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

package com.flowcentraltech.flowcentral.chart.data.provider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.data.EntityCategoryDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.EntitySeriesDef;
import com.flowcentraltech.flowcentral.application.data.PropertySequenceDef;
import com.flowcentraltech.flowcentral.application.data.PropertySequenceEntryDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.chart.constants.ChartModuleNameConstants;
import com.flowcentraltech.flowcentral.chart.data.AbstractChartDetailsProvider;
import com.flowcentraltech.flowcentral.chart.data.ChartDataSourceDef;
import com.flowcentraltech.flowcentral.chart.data.ChartDetails;
import com.flowcentraltech.flowcentral.chart.data.ChartTableColumn;
import com.flowcentraltech.flowcentral.chart.entities.ChartDataSource;
import com.flowcentraltech.flowcentral.chart.entities.ChartDataSourceQuery;
import com.flowcentraltech.flowcentral.chart.util.ChartUtils;
import com.flowcentraltech.flowcentral.configuration.constants.ChartCategoryDataType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartSeriesDataType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartTimeSeriesType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.criterion.AggregateFunction;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.GroupingFunction;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.database.Aggregation;
import com.tcdng.unify.core.database.GroupingAggregation;
import com.tcdng.unify.core.database.GroupingAggregation.Grouping;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Chart data source chart details provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(name = ChartModuleNameConstants.CHARTDATASOURCE_PROVIDER, description = "$m{chartdatasource.provider}")
public class ChartDataSourceChartDetailsProvider extends AbstractChartDetailsProvider {

    @Override
    public ChartDetails provide(String rule, Restriction restriction) throws UnifyException {
        ChartDataSourceDef chartDataSourceDef = chart().getChartDataSourceDef(rule);
        return getChartData(chartDataSourceDef, restriction);
    }

    @Override
    public List<? extends Listable> getRuleList(Locale locale) throws UnifyException {
        List<ChartDataSource> sourceList = chart().findChartDataSources(
                (ChartDataSourceQuery) new ChartDataSourceQuery().addSelect("applicationName", "name", "description")
                        .addOrder("description").ignoreEmptyCriteria(true));
        return ApplicationNameUtils.getListableList(sourceList);
    }

    @Override
    public boolean isUsesChartDataSource() {
        return true;
    }

    private ChartDetails getChartData(ChartDataSourceDef chartDataSourceDef, Restriction erestriction)
            throws UnifyException {
        final EntityDef entityDef = chartDataSourceDef.getEntityDef();
        final EntityFieldDef preferredCategoryEntityFieldDef = chartDataSourceDef.getCategoryEntityFieldDef();
        final ChartCategoryDataType chartCategoryType = preferredCategoryEntityFieldDef != null
                && preferredCategoryEntityFieldDef.isTime() && !chartDataSourceDef.isMerged()
                        ? ChartCategoryDataType.DATE
                        : ChartCategoryDataType.STRING;
        ChartDetails.Builder cdb = ChartDetails.newBuilder(chartCategoryType);
        cdb.dynamicCategories(chartDataSourceDef.isWithGroupingFields());
        
        final Date now = au().getNow();
        final PropertySequenceDef series = chartDataSourceDef.getSeries();
        final List<AggregateFunction> aggregateFunction = new ArrayList<AggregateFunction>();
        for (PropertySequenceEntryDef sequenceDef : series.getSequenceList()) {
            EntitySeriesDef entitySeriesDef = entityDef.getEntitySeriesDef(sequenceDef.getProperty());
            aggregateFunction.add(entitySeriesDef.getType().function(entitySeriesDef.getFieldName()));
        }

        Restriction baseRestriction = InputWidgetUtils.getRestriction(au(), entityDef, null, chartDataSourceDef.getCategoryBase(),
                now);
        if (chartDataSourceDef.isWithCategories()) {
            final PropertySequenceDef categories = chartDataSourceDef.getCategories();
            if (!categories.isBlank()) {
                for (PropertySequenceEntryDef propertySequenceEntryDef : categories.getSequenceList()) {
                    final EntityCategoryDef entityCategoryDef = entityDef
                            .getEntityCategorysDef(propertySequenceEntryDef.getProperty());
                    final String cat = entityCategoryDef.getName();
                    final String catlabel = !StringUtils.isBlank(propertySequenceEntryDef.getLabel())
                            ? propertySequenceEntryDef.getLabel()
                            : entityCategoryDef.getLabel();
                    Restriction restriction = InputWidgetUtils.getRestriction(au(), entityDef, null,
                            entityCategoryDef.getFilterDef(), now);
                    if (restriction != null) {
                        if (baseRestriction != null) {
                            restriction = new And().add(baseRestriction).add(restriction);
                        }
                    } else {
                        restriction = baseRestriction;
                    }

                    if (erestriction != null) {
                        if (restriction != null) {
                            restriction = new And().add(restriction).add(erestriction);
                        } else {
                            restriction = erestriction;
                        }
                    }

                    cdb.setCategoryLabel(cat, catlabel);
                    performAggregation(cdb, chartDataSourceDef, aggregateFunction, now, restriction, cat);
                }
            }
        } else {
            final Object cat = preferredCategoryEntityFieldDef != null ? preferredCategoryEntityFieldDef.getFieldName() : null;
            if (erestriction != null) {
                if (baseRestriction != null) {
                    baseRestriction = new And().add(baseRestriction).add(erestriction);
                } else {
                    baseRestriction = erestriction;
                }
            }

            performAggregation(cdb, chartDataSourceDef, aggregateFunction, now, baseRestriction, cat);
        }

        return cdb.build();
    }

    private void performAggregation(ChartDetails.Builder cdb, ChartDataSourceDef chartDataSourceDef,
            List<AggregateFunction> aggregateFunction, Date now, Restriction restriction, Object cat)
            throws UnifyException {
        final EntityDef entityDef = chartDataSourceDef.getEntityDef();
        final PropertySequenceDef series = chartDataSourceDef.getSeries();
        final int slen = series.getSequenceList().size();

        ensureTableSeries(cdb, chartDataSourceDef);
        if (chartDataSourceDef.isWithGroupingFieldsAndOrTimeSeries()) {
            final List<String> groupingFieldNames = chartDataSourceDef.getGroupingFieldNames();
            final ChartTimeSeriesType timeSeriesType = chartDataSourceDef.getTimeSeriesType() != null
                    ? chartDataSourceDef.getTimeSeriesType()
                    : ChartTimeSeriesType.DAY_OVER_WEEK;

            List<GroupingFunction> groupingFunction = new ArrayList<GroupingFunction>();
            final int glen = groupingFieldNames.size();
            for (String fieldName : groupingFieldNames) {
                final EntityFieldDef entityFieldDef = entityDef.getFieldDef(fieldName);
                final GroupingFunction _groupingFunction = entityFieldDef.isTime()
                        ? new GroupingFunction(fieldName, timeSeriesType.type())
                        : new GroupingFunction(fieldName);
                groupingFunction.add(_groupingFunction);
            }

            final boolean isTimeSeries = chartDataSourceDef.isTimeSeries();
            List<GroupingAggregation> gaggregations = environment().aggregate(aggregateFunction,
                    restriction != null
                            ? application().queryOf(entityDef.getLongName()).addRestriction(restriction)
                                    .setMerge(timeSeriesType.merged())
                            : application().queryOf(entityDef.getLongName()).setMerge(timeSeriesType.merged())
                                    .ignoreEmptyCriteria(true),
                    groupingFunction);
            if (isTimeSeries && timeSeriesType.fill()) {
                gaggregations = ChartUtils.fill(entityDef, gaggregations, timeSeriesType);
            }

            final int columns = glen + slen;
            for (GroupingAggregation gaggregation : gaggregations) {
                Object _cat = cat;
                Object[] tableRow = new Object[columns];
                int c = 0;
                for (; c < glen; c++) {
                    final Object _x = gaggregation.isDateGrouping(c) ? gaggregation.getGroupingAsDate(c)
                            : gaggregation.getGroupingAsString(c);
                    tableRow[c] = _x;

                    if (isTimeSeries && c == 0) {
                        _cat = _x;
                    }
                }

                final List<Aggregation> aggregations = gaggregation.getAggregation();
                for (int i = 0; i < slen; i++, c++) {
                    final PropertySequenceEntryDef sequenceDef = series.getSequenceList().get(i);
                    final String seriesName = ensureSeries(cdb, entityDef, sequenceDef, gaggregation.getGroupings(),
                            isTimeSeries ? cat : null);
                    final Number y = aggregations.get(i).getValue(Number.class);
                    tableRow[c] = y;
                    cdb.addSeriesData(seriesName, _cat, y);
                    if (isTimeSeries) {
                        String __cat = String.valueOf(_cat);
                        cdb.setCategoryLabel(__cat, __cat);
                        cdb.addCategoryInclusion(__cat);
                    }
                }

                cdb.addTableSeries(tableRow);
            }
        } else {
            Object[] tableRow = new Object[slen];
            final List<Aggregation> aggregations = environment().aggregate(aggregateFunction,
                    restriction != null ? application().queryOf(entityDef.getLongName()).addRestriction(restriction)
                            : application().queryOf(entityDef.getLongName()).ignoreEmptyCriteria(true));
            for (int i = 0; i < slen; i++) {
                final PropertySequenceEntryDef sequenceDef = series.getSequenceList().get(i);
                final String seriesName = ensureSeries(cdb, entityDef, sequenceDef, null, null);
                final Number y = aggregations.get(i).getValue(Number.class);
                tableRow[i] = y;
                cdb.addSeriesData(seriesName, cat, y);
            }

            cdb.addTableSeries(tableRow);
        }
    }

    private void ensureTableSeries(ChartDetails.Builder cdb, ChartDataSourceDef chartDataSourceDef) {
        if (!cdb.isWithTableSeries()) {
            List<ChartTableColumn> headers = new ArrayList<ChartTableColumn>();
            final EntityDef entityDef = chartDataSourceDef.getEntityDef();
            final PropertySequenceDef series = chartDataSourceDef.getSeries();
            if (chartDataSourceDef.isWithGroupingFieldsAndOrTimeSeries()) {
                final List<String> groupingFieldNames = chartDataSourceDef.getGroupingFieldNames();
                final int len = groupingFieldNames.size();
                for (int i = 0; i < len; i++) {
                    String fieldName = groupingFieldNames.get(i);
                    EntityFieldDef entityFieldDef = entityDef.getFieldDef(fieldName);
                    headers.add(new ChartTableColumn(
                            (i == 0 && chartDataSourceDef.isMerged()) || entityFieldDef.isListOnly()
                                    ? EntityFieldDataType.STRING
                                    : entityFieldDef.getDataType(),
                            fieldName, entityFieldDef.getFieldLabel(), true));
                }
            }

            for (PropertySequenceEntryDef sequenceDef : series.getSequenceList()) {
                final EntitySeriesDef entitySeriesDef = entityDef.getEntitySeriesDef(sequenceDef.getProperty());
                EntityFieldDef entityFieldDef = entityDef.getFieldDef(entitySeriesDef.getFieldName());
                if (entityFieldDef.isWithResolvedTypeFieldDef()) {
                    entityFieldDef = entityFieldDef.getResolvedTypeFieldDef();
                }

                headers.add(new ChartTableColumn(
                        /*entityFieldDef.isListOnly() ? EntityFieldDataType.STRING : */entityFieldDef.getDataType(),
                        entitySeriesDef.getFieldName(), entitySeriesDef.getLabel(), false));
            }

            cdb.createTableSeries(DataUtils.toArray(ChartTableColumn.class, headers));
        }
    }

    private String ensureSeries(ChartDetails.Builder cdb, EntityDef entityDef, PropertySequenceEntryDef sequenceDef,
            List<Grouping> groupings, Object cat) throws UnifyException {
        String seriesName = sequenceDef.getProperty();
        if (!DataUtils.isBlank(groupings)) {
            StringBuilder sb = new StringBuilder(seriesName);
            final int len = groupings.size();
            int i = 0;
            if (cat != null) {
                sb.append(" ");
                sb.append(cat);
                i++;
            }

            for (; i < len; i++) {
                sb.append(" ");
                sb.append(groupings.get(i).getAsString());
            }

            seriesName = sb.toString();
            cdb.addSeriesInclusion(seriesName);
        }

        if (!cdb.isWithSeries(seriesName)) {
            EntityFieldDef entityFieldDef = entityDef
                    .getFieldDef(entityDef.getEntitySeriesDef(sequenceDef.getProperty()).getFieldName());
            cdb.createSeries(entityFieldDef.isDecimal() ? ChartSeriesDataType.DOUBLE : ChartSeriesDataType.INTEGER,
                    seriesName);
        }

        return seriesName;
    }
}
