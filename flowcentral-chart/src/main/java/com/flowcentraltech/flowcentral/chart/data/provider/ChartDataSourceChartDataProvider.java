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

package com.flowcentraltech.flowcentral.chart.data.provider;

import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.chart.constants.ChartModuleNameConstants;
import com.flowcentraltech.flowcentral.chart.data.AbstractChartDataProvider;
import com.flowcentraltech.flowcentral.chart.data.ChartData;
import com.flowcentraltech.flowcentral.chart.data.ChartDataSourceDef;
import com.flowcentraltech.flowcentral.chart.entities.ChartDataSource;
import com.flowcentraltech.flowcentral.chart.entities.ChartDataSourceQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.Listable;

/**
 * Chart data source chart data provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(name = ChartModuleNameConstants.CHARTDATASOURCE_PROVIDER, description = "$m{chartdatasource.provider}")
public class ChartDataSourceChartDataProvider extends AbstractChartDataProvider {

    @Override
    public ChartData provide(String rule) throws UnifyException {
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

    protected ChartData getChartData(ChartDataSourceDef chartDataSourceDef) throws UnifyException {
        // TODO
        return null;
    }

}
