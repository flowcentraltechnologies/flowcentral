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

package com.flowcentraltech.flowcentral.chart.data.provider;

import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.chart.constants.ChartModuleNameConstants;
import com.flowcentraltech.flowcentral.chart.data.AbstractChartDetailsProvider;
import com.flowcentraltech.flowcentral.chart.data.ChartDataSourceDef;
import com.flowcentraltech.flowcentral.chart.data.ChartDetails;
import com.flowcentraltech.flowcentral.chart.data.ChartViewOption;
import com.flowcentraltech.flowcentral.chart.entities.ChartDataSource;
import com.flowcentraltech.flowcentral.chart.entities.ChartDataSourceQuery;
import com.flowcentraltech.flowcentral.configuration.constants.ChartCategoryDataType;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
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
    public ChartDetails provide(String rule, ChartViewOption chartViewOption)
            throws UnifyException {
        try {
            if (!StringUtils.isBlank(rule)) {
                ChartDataSourceDef chartDataSourceDef = chart().getChartDataSourceDef(rule);
                return getChartData(chartDataSourceDef, chartViewOption);
            }
        } catch (Exception e) {
            logError(e);
        }

        return ChartDetails.newBuilder(ChartCategoryDataType.STRING).build();
    }

    @Override
    public ChartDetails provide(ChartDataSourceDef chartDataSourceDef) throws UnifyException {
        try {
            return getChartData(chartDataSourceDef, ChartViewOption.DEFAULT);
        } catch (Exception e) {
            logError(e);
        }

        return ChartDetails.newBuilder(ChartCategoryDataType.STRING).build();
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

    private ChartDetails getChartData(ChartDataSourceDef chartDataSourceDef, ChartViewOption chartViewOption) throws UnifyException {
        final ChartCategoryDataType chartCategoryType = null;// TODO
        ChartDetails.Builder cdb = ChartDetails.newBuilder(chartCategoryType);

        return cdb.build();
    }

}
