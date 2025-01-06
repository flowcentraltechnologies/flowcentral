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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.chart.business.ChartModuleService;
import com.flowcentraltech.flowcentral.chart.entities.Chart;
import com.flowcentraltech.flowcentral.chart.entities.ChartQuery;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.list.AbstractZeroParamsListCommand;
import com.tcdng.unify.core.list.ZeroParams;

/**
 * Chart list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("chartlist")
public class ChartListCommand extends AbstractZeroParamsListCommand {

    @Configurable
    private ChartModuleService chartModuleService;

    @Override
    public List<? extends Listable> execute(Locale locale, ZeroParams param) throws UnifyException {
        List<ListData> resultList = new ArrayList<ListData>();
        for (Chart chart : chartModuleService.findCharts((ChartQuery) new ChartQuery()
                .addSelect("applicationName", "name", "description").ignoreEmptyCriteria(true))) {
            resultList.add(new ListData(
                    ApplicationNameUtils.getApplicationEntityLongName(chart.getApplicationName(), chart.getName()),
                    chart.getDescription()));
        }

        return resultList;
    }

}
