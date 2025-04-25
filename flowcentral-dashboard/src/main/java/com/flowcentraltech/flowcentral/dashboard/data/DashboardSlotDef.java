/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.dashboard.data;

import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.chart.data.ChartConfiguration;

/**
 * Dashboard slot definition object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class DashboardSlotDef implements ChartConfiguration {

    private final DashboardTileDef tileDef;

    private final DashboardOptionDef optionDef;

    public DashboardSlotDef(DashboardTileDef tileDef, DashboardOptionDef optionDef) {
        this.tileDef = tileDef;
        this.optionDef = optionDef;
    }

    public DashboardTileDef getTileDef() {
        return tileDef;
    }

    @Override
    public String getChart() {
        return tileDef.getChart();
    }

    @Override
    public FilterDef getCatBase(String dataSourceName) {
        return optionDef != null && optionDef.isWithCatBase(dataSourceName) ? optionDef.getCatBase(dataSourceName)
                : null;
    }

}