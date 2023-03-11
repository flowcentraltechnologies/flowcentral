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

package com.flowcentraltech.flowcentral.chart.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.configuration.constants.ChartCategoryDataType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartSeriesDataType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.BeanValueListStore;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient abstract base class for category mapped series chart data
 * providers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractCategoryMappedSeriesChartDataProvider extends AbstractChartDataProvider {

    private final ChartCategoryDataType categoryType;

    private final ChartSeriesDataType seriesType;

    private final String entity;

    private final String categoryValueProperty;

    private final Map<String, String> seriesValuePropertyByNameMap;

    protected AbstractCategoryMappedSeriesChartDataProvider(ChartCategoryDataType categoryType,
            ChartSeriesDataType seriesType, String entity, String categoryValueProperty,
            Map<String, String> seriesValuePropertyByNameMap) {
        this.categoryType = categoryType;
        this.seriesType = seriesType;
        this.entity = entity;
        this.categoryValueProperty = categoryValueProperty;
        this.seriesValuePropertyByNameMap = seriesValuePropertyByNameMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final ChartData provide(String rule) throws UnifyException {
        SimpleDateFormat format = getDateFormat();
        List<Object> categories = new ArrayList<Object>();
        Map<String, List<Number>> series = new LinkedHashMap<String, List<Number>>();
        for (String seriesName : seriesValuePropertyByNameMap.keySet()) {
            series.put(seriesName, new ArrayList<Number>());
        }

        EntityClassDef entityClassDef = application().getEntityClassDef(entity);
        List<? extends Entity> statistics = environment()
                .findAll(Query.of((Class<? extends Entity>) entityClassDef.getEntityClass()));
        ValueStore valueStore = new BeanValueListStore(statistics);
        final int len = valueStore.size();
        for (int i = 0; i < len; i++) {
            valueStore.setDataIndex(i);
            Object categoryValue = valueStore.retrieve(Object.class, categoryValueProperty);
            if ((categoryValue instanceof Date) && ChartCategoryDataType.STRING.equals(categoryType)) {
                categoryValue = formatDate(format, (Date) categoryValue);
            } else {
                categoryValue = DataUtils.convert(categoryType.dataType(), categoryValue);
            }

            if (!categories.contains(categoryValue)) {
                categories.add(categoryValue);
            }

            for (Map.Entry<String, String> entry : seriesValuePropertyByNameMap.entrySet()) {
                Number seriesValue = valueStore.retrieve(seriesType.dataType(), entry.getValue());
                series.get(entry.getKey()).add(seriesValue);
            }
        }

        ChartData.Builder cdb = ChartData.newBuilder();
        setAdditionalProperties(cdb, Collections.unmodifiableList(categories), Collections.unmodifiableMap(series));
        cdb.categories(categoryType, categories);
        for (Map.Entry<String, List<Number>> entry : series.entrySet()) {
            cdb.addSeries(seriesType, entry.getKey(), entry.getValue());
        }

        return cdb.build();
    }

    protected abstract void setAdditionalProperties(ChartData.Builder cdb, List<Object> categories,
            Map<String, List<Number>> series) throws UnifyException;
}
