/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import java.util.ArrayList;
import java.util.HashMap;
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

/**
 * Convenient abstract base class for chart data providers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractCategorySeriesChartDataProvider extends AbstractChartDataProvider {

    private final ChartCategoryDataType categoryType;

    private final ChartSeriesDataType seriesType;

    private final String categoryValueProperty;

    private final String seriesNameProperty;

    private final String seriesValueProperty;
    
    protected AbstractCategorySeriesChartDataProvider(ChartCategoryDataType categoryType,
            ChartSeriesDataType seriesType, String categoryValueProperty, String seriesNameProperty, String seriesValueProperty) {
        this.categoryType = categoryType;
        this.seriesType = seriesType;
        this.categoryValueProperty = categoryValueProperty;
        this.seriesNameProperty = seriesNameProperty;
        this.seriesValueProperty = seriesValueProperty;
    }

    @SuppressWarnings("unchecked")
    protected ChartData provide(String entity, String... seriesNames) throws UnifyException {
        List<Object> categories = new ArrayList<Object>();
        Map<String, List<Object>> series = new HashMap<String, List<Object>>();
        for (String seriesName : seriesNames) {
            series.put(seriesName, new ArrayList<Object>());
        }

        EntityClassDef entityClassDef = application().getEntityClassDef(entity);
        List<? extends Entity> statistics = environment()
                .findAll(Query.of((Class<? extends Entity>) entityClassDef.getEntityClass()));
        ValueStore valueStore = new BeanValueListStore(statistics);
        final int len = valueStore.size();
        for (int i = 0; i < len; i++) {
            valueStore.setDataIndex(i);
            String _seriesNameProperty = valueStore.retrieve(String.class, seriesNameProperty);
            if (series.containsKey(_seriesNameProperty)) {
                Object _categoryValueProperty = valueStore.retrieve(categoryType.dataType(), categoryValueProperty);
                Object _seriesValueProperty = valueStore.retrieve(seriesType.dataType(), seriesValueProperty);

                if (!categories.contains(_categoryValueProperty)) {
                    categories.add(_categoryValueProperty);
                }

                series.get(_seriesNameProperty).add(_seriesValueProperty);
            }
        }

        ChartData.Builder cdb = ChartData.newBuilder();
        cdb.categories(categoryType, categories);
        for (Map.Entry<String, List<Object>> entry : series.entrySet()) {
            cdb.addSeries(seriesType, entry.getKey(), entry.getValue());
        }

        return cdb.build();
    }

}
