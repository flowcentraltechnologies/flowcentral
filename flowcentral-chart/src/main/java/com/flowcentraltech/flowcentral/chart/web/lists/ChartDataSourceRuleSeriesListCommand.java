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

package com.flowcentraltech.flowcentral.chart.web.lists;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.chart.business.ChartModuleService;
import com.flowcentraltech.flowcentral.chart.data.ChartDataSourceDef;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.list.AbstractListCommand;
import com.tcdng.unify.core.list.StringParam;

/**
 * Chart data source rule series list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("chartdatasourceruleserieslist")
public class ChartDataSourceRuleSeriesListCommand extends AbstractListCommand<StringParam> {

    @Configurable
    private ChartModuleService chartModuleService;

    public ChartDataSourceRuleSeriesListCommand() {
        super(StringParam.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, StringParam param) throws UnifyException {
        if (param.isPresent()) {
            ChartDataSourceDef chartDataSourceDef = chartModuleService.getChartDataSourceDef(param.getValue());
            return chartDataSourceDef.getSeries().getSequenceList();
        }

        return Collections.emptyList();
    }

}
