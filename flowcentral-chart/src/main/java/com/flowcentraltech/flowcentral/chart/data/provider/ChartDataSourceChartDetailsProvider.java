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

package com.flowcentraltech.flowcentral.chart.data.provider;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.flowcentraltech.flowcentral.chart.data.ChartDetails;
import com.flowcentraltech.flowcentral.chart.data.ChartTableColumn;
import com.flowcentraltech.flowcentral.chart.data.ChartDataSourceDef;
import com.flowcentraltech.flowcentral.chart.entities.ChartDataSource;
import com.flowcentraltech.flowcentral.chart.entities.ChartDataSourceQuery;
import com.flowcentraltech.flowcentral.configuration.constants.ChartCategoryDataType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartSeriesDataType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartTimeSeriesType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.criterion.AggregateFunction;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.GroupingFunction;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.database.Aggregation;
import com.tcdng.unify.core.database.GroupingAggregation;
import com.tcdng.unify.core.database.GroupingAggregation.Grouping;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Chart data source chart details provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(name = ChartModuleNameConstants.CHARTDATASOURCE_PROVIDER, description = "$m{chartdatasource.provider}")
public class ChartDataSourceChartDetailsProvider extends AbstractChartDetailsProvider {

    @Override
    public ChartDetails provide(String rule) throws UnifyException {
        ChartDataSourceDef chartDataSourceDef = chart().getChartDataSourceDef(rule);
        return getChartData(chartDataSourceDef);
    }

    @Override
    public List<? extends Listable> getRuleList(Locale locale) throws UnifyException {
        List<ChartDataSource> sourceList = chart().findChartDataSources(
                (ChartDataSourceQuery) new ChartDataSourceQuery().addSelect("applicationName", "name", "description")
                        .addOrder("description").ignoreEmptyCriteria(true));
        return ApplicationNameUtils.getListableList(sourceList);
    }

    protected ChartDetails getChartData(ChartDataSourceDef chartDataSourceDef) throws UnifyException {
        final EntityDef entityDef = chartDataSourceDef.getEntityDef();
        final EntityFieldDef categoryEntityFieldDef = chartDataSourceDef.getCategoryEntityFieldDef();
        final ChartCategoryDataType chartCategoryType = categoryEntityFieldDef != null
                && (categoryEntityFieldDef.isDate() || categoryEntityFieldDef.isTimestamp())
                        ? ChartCategoryDataType.DATE
                        : ChartCategoryDataType.STRING;
        ChartDetails.Builder cdb = ChartDetails.newBuilder(chartCategoryType);

        final Date now = au().getNow();
        final PropertySequenceDef series = chartDataSourceDef.getSeries();
        final List<AggregateFunction> aggregateFunction = new ArrayList<AggregateFunction>();
        for (PropertySequenceEntryDef sequenceDef : series.getSequenceList()) {
            EntitySeriesDef entitySeriesDef = entityDef.getEntitySeriesDef(sequenceDef.getProperty());
            aggregateFunction.add(entitySeriesDef.getType().function(entitySeriesDef.getFieldName()));
        }

        final int slen = series.getSequenceList().size();
        final Restriction baseRestriction = InputWidgetUtils.getRestriction(entityDef,
                chartDataSourceDef.getCategoryBase(), au().specialParamProvider(), now);
        if (chartDataSourceDef.isWithCategories()) {
            final PropertySequenceDef categories = chartDataSourceDef.getCategories();
            if (!categories.isBlank()) {
                for (PropertySequenceEntryDef propertySequenceEntryDef : categories.getSequenceList()) {
                    final EntityCategoryDef entityCategoryDef = entityDef
                            .getEntityCategorysDef(propertySequenceEntryDef.getProperty());
                    final String cat = !StringUtils.isBlank(propertySequenceEntryDef.getLabel())
                            ? propertySequenceEntryDef.getLabel()
                            : entityCategoryDef.getLabel();

                    Restriction restriction = InputWidgetUtils.getRestriction(entityDef,
                            entityCategoryDef.getFilterDef(), au().specialParamProvider(), now);
                    if (restriction != null) {
                        if (baseRestriction != null) {
                            restriction = new And().add(baseRestriction).add(restriction);
                        }
                    } else {
                        restriction = baseRestriction;
                    }

                    if (entityCategoryDef.isWithGroupingFields()) {
                        List<String> grouingFieldNames = entityCategoryDef.getGroupingFieldSequenceDef()
                                .getFieldNames();
                        provideGroupAggregation(cdb, chartDataSourceDef, grouingFieldNames, aggregateFunction, now,
                                baseRestriction, cat);
                    } else {
                        final List<Aggregation> aggregations = environment().aggregate(aggregateFunction,
                                restriction != null
                                        ? application().queryOf(entityDef.getLongName()).addRestriction(restriction)
                                        : application().queryOf(entityDef.getLongName()).ignoreEmptyCriteria(true));
                        for (int i = 0; i < slen; i++) {
                            final PropertySequenceEntryDef sequenceDef = series.getSequenceList().get(i);
                            final String seriesName = ensureSeries(cdb, entityDef, sequenceDef, null);
                            final Number y = aggregations.get(i).getValue(Number.class);
                            cdb.addSeriesData(seriesName, cat, y);
                        }
                    }
                }
            }
        } else if (categoryEntityFieldDef != null) {
            final Object cat = categoryEntityFieldDef.getFieldName();
            provideGroupAggregation(cdb, chartDataSourceDef, Arrays.asList(categoryEntityFieldDef.getFieldName()),
                    aggregateFunction, now, baseRestriction, cat);
        }

        return cdb.build();
    }

    private String ensureSeries(ChartDetails.Builder cdb, EntityDef entityDef, PropertySequenceEntryDef sequenceDef,
            List<Grouping> groupings) throws UnifyException {
        String seriesName = !StringUtils.isBlank(sequenceDef.getLabel()) ? sequenceDef.getLabel()
                : entityDef.getEntitySeriesDef(sequenceDef.getProperty()).getLabel();
        if (!DataUtils.isBlank(groupings)) {
            StringBuilder sb = new StringBuilder();
            for (Grouping grouping : groupings) {
                sb.append(" ");
                sb.append(grouping.getAsString());
            }

            seriesName = sb.toString();
        }

        if (!cdb.isSeries(seriesName)) {
            EntityFieldDef entityFieldDef = entityDef
                    .getFieldDef(entityDef.getEntitySeriesDef(sequenceDef.getProperty()).getFieldName());
            cdb.createSeries(entityFieldDef.isDecimal() ? ChartSeriesDataType.DOUBLE : ChartSeriesDataType.INTEGER,
                    seriesName);
        }

        return seriesName;
    }

    private void provideGroupAggregation(ChartDetails.Builder cdb, ChartDataSourceDef chartDataSourceDef,
            List<String> grouingFieldNames, List<AggregateFunction> aggregateFunction, Date now,
            Restriction baseRestriction, Object cat) throws UnifyException {
        final ChartTimeSeriesType timeSeriesType = chartDataSourceDef.getTimeSeriesType() != null
                ? chartDataSourceDef.getTimeSeriesType()
                : ChartTimeSeriesType.DAY_OVER_WEEK;
        final EntityDef entityDef = chartDataSourceDef.getEntityDef();
        final PropertySequenceDef series = chartDataSourceDef.getSeries();

        List<GroupingFunction> groupingFunction = new ArrayList<GroupingFunction>();
        List<ChartTableColumn> headers = new ArrayList<ChartTableColumn>();
        final int glen = grouingFieldNames.size();
        for (String fieldName : grouingFieldNames) {
            final EntityFieldDef entityFieldDef = entityDef.getFieldDef(fieldName);
            ChartCategoryDataType chartCategoryType = entityFieldDef.isDate() || entityFieldDef.isTimestamp()
                    ? ChartCategoryDataType.DATE
                    : ChartCategoryDataType.STRING;
            final GroupingFunction _groupingFunction = chartCategoryType.isString() ? new GroupingFunction(fieldName)
                    : new GroupingFunction(fieldName, timeSeriesType.type());
            groupingFunction.add(_groupingFunction);

            headers.add(new ChartTableColumn(entityFieldDef.getDataType(), fieldName, entityFieldDef.getFieldLabel()));
        }

        for (PropertySequenceEntryDef sequenceDef : series.getSequenceList()) {
            final EntitySeriesDef entitySeriesDef = entityDef.getEntitySeriesDef(sequenceDef.getProperty());
            final EntityFieldDef entityFieldDef = entityDef.getFieldDef(entitySeriesDef.getFieldName());
            headers.add(new ChartTableColumn(entityFieldDef.getDataType(), entitySeriesDef.getFieldName(),
                    entitySeriesDef.getLabel()));
        }

        cdb.createTableSeries(DataUtils.toArray(ChartTableColumn.class, headers));

        final List<GroupingAggregation> gaggregations = environment().aggregate(aggregateFunction,
                baseRestriction != null ? application().queryOf(entityDef.getLongName()).addRestriction(baseRestriction)
                        : application().queryOf(entityDef.getLongName()).ignoreEmptyCriteria(true),
                groupingFunction);
        final int slen = series.getSequenceList().size();
        final int columns = glen + slen;
        for (GroupingAggregation gaggregation : gaggregations) {
            Object[] tableRow = new Object[columns];
            int c = 0;
            for (; c < glen; c++) {
                final Object _x = gaggregation.isDateGrouping(c) ? gaggregation.getGroupingAsDate(c)
                        : gaggregation.getGroupingAsString(c);
                tableRow[c] = _x;
            }

            final List<Aggregation> aggregations = gaggregation.getAggregation();
            for (int i = 0; i < slen; i++, c++) {
                final PropertySequenceEntryDef sequenceDef = series.getSequenceList().get(i);
                final String seriesName = ensureSeries(cdb, entityDef, sequenceDef, gaggregation.getGroupings());
                final Number y = aggregations.get(i).getValue(Number.class);
                tableRow[c] = y;
                cdb.addSeriesData(seriesName, cat, y);
            }

            cdb.addTableSeries(tableRow);
        }
    }
}
