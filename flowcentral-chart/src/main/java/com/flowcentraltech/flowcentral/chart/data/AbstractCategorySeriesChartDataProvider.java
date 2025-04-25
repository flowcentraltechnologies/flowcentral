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
import java.util.Date;
import java.util.List;

import com.flowcentraltech.flowcentral.configuration.constants.ChartCategoryDataType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartSeriesDataType;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.BeanValueListStore;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient abstract base class for chart data providers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractCategorySeriesChartDataProvider extends AbstractChartDetailsProvider {

    private final ChartCategoryDataType categoryType;

    private final ChartSeriesDataType seriesType;

    private final String entity;

    private final String[] seriesNames;

    private final String categoryValueProperty;

    private final String seriesNameProperty;

    private final String seriesValueProperty;

    protected AbstractCategorySeriesChartDataProvider(ChartCategoryDataType categoryType,
            ChartSeriesDataType seriesType, String entity, String categoryValueProperty,
            String[] seriesNames, String seriesNameProperty, String seriesValueProperty) {
        this.categoryType = categoryType;
        this.seriesType = seriesType;
        this.entity = entity;
        this.seriesNames = seriesNames;
        this.categoryValueProperty = categoryValueProperty;
        this.seriesNameProperty = seriesNameProperty;
        this.seriesValueProperty = seriesValueProperty;
    }

    @Override
    public final ChartDetails provide(String rule, Restriction restriction) throws UnifyException {
        SimpleDateFormat format = getDateFormat();
        ChartDetails.Builder cdb = ChartDetails.newBuilder(categoryType);
        for (String seriesName : seriesNames) {
            cdb.createSeries(seriesType, seriesName);
        }

        List<? extends Entity> statistics = getStatistics(entity);
        ValueStore valueStore = new BeanValueListStore(statistics);
        final int len = valueStore.size();
        for (int i = 0; i < len; i++) {
            valueStore.setDataIndex(i);
            String _seriesNameProperty = valueStore.retrieve(String.class, seriesNameProperty);
            if (cdb.isWithSeries(_seriesNameProperty)) {
                Object categoryValue = valueStore.retrieve(Object.class, categoryValueProperty);
                if ((categoryValue instanceof Date) && ChartCategoryDataType.STRING.equals(categoryType)) {
                    categoryValue = formatDate(format, (Date) categoryValue);
                } else {
                    categoryValue = DataUtils.convert(categoryType.dataType(), categoryValue);
                }

                Number seriesValue = valueStore.retrieve(seriesType.dataType(), seriesValueProperty);
                cdb.addSeriesData(_seriesNameProperty, categoryValue, seriesValue);
            }
        }

        setAdditionalProperties(cdb);
        return cdb.build();
    }

    protected abstract void setAdditionalProperties(ChartDetails.Builder cdb) throws UnifyException;

}
