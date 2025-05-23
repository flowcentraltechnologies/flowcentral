/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.chart.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.configuration.constants.ChartCategoryDataType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartSeriesDataType;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.BeanValueListStore;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient abstract base class for category summary series chart data
 * providers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractCategorySummarySeriesChartDataProvider extends AbstractChartDetailsProvider {

    private final ChartCategoryDataType categoryType;

    private final ChartSeriesDataType seriesType;

    private final String entity;

    private final Object[] categories;

    private final String seriesName;

    private final String[] seriesValueProperties;

    protected AbstractCategorySummarySeriesChartDataProvider(ChartCategoryDataType categoryType,
            ChartSeriesDataType seriesType, String entity, Object[] categories, String seriesName,
            String[] seriesValueProperties) {
        this.categoryType = categoryType;
        this.seriesType = seriesType;
        this.entity = entity;
        this.categories = categories;
        this.seriesName = seriesName;
        this.seriesValueProperties = seriesValueProperties;
    }

    @Override
    public final ChartDetails provide(String rule, Restriction restriction) throws UnifyException {
        SimpleDateFormat format = getDateFormat();
        List<Object> _categories = new ArrayList<Object>();
        for (Object categoryValue : categories) {
            if ((categoryValue instanceof Date) && ChartCategoryDataType.STRING.equals(categoryType)) {
                categoryValue = formatDate(format, (Date) categoryValue);
            } else {
                categoryValue = DataUtils.convert(categoryType.dataType(), categoryValue);
            }

            _categories.add(categoryValue);
        }

        Map<String, Number> summary = new LinkedHashMap<String, Number>();
        for (String seriesValueProperty : seriesValueProperties) {
            summary.put(seriesValueProperty, seriesType.zero());
        }

        List<? extends Entity> statistics = getStatistics(entity);
        ValueStore valueStore = new BeanValueListStore(statistics);
        final int len = valueStore.size();
        for (int i = 0; i < len; i++) {
            valueStore.setDataIndex(i);
            for (String seriesValueProperty : seriesValueProperties) {
                Number val = valueStore.retrieve(seriesType.dataType(), seriesValueProperty);
                if (val != null) {
                    val = seriesType.add(summary.get(seriesValueProperty), val);
                    summary.put(seriesValueProperty, val);
                }
            }
        }

        List<Number> series = new ArrayList<Number>(summary.values());

        ChartDetails.Builder cdb = ChartDetails.newBuilder(categoryType);
        cdb.createSeries(seriesType, seriesName);
        for (int i = 0; i < categories.length; i++) {
            cdb.addSeriesData(seriesName, _categories.get(i), series.get(i));
        }

        setAdditionalProperties(cdb);
        return cdb.build();
    }

    protected abstract void setAdditionalProperties(ChartDetails.Builder cdb) throws UnifyException;

}
