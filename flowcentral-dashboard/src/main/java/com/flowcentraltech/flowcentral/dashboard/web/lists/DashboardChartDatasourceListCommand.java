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

package com.flowcentraltech.flowcentral.dashboard.web.lists;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.dashboard.business.DashboardModuleService;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.list.StringParam;

/**
 * Dashboard chart data source list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("dashboardchartdatasourcelist")
public class DashboardChartDatasourceListCommand extends AbstractDashboardListCommand<StringParam> {

    @Configurable
    private DashboardModuleService dashboardModuleService;

    public DashboardChartDatasourceListCommand() {
        super(StringParam.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, StringParam param) throws UnifyException {
        if (param.isPresent()) {
            return dashboardModuleService.getDashboardOptionChartDataSourceList(param.getValue());
        }

        return Collections.emptyList();
    }

}
