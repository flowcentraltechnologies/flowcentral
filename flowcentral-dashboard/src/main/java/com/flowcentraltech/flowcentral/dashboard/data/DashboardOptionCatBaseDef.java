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

package com.flowcentraltech.flowcentral.dashboard.data;

import com.flowcentraltech.flowcentral.application.data.FilterDef;

/**
 * Dashboard option category base definition object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class DashboardOptionCatBaseDef {

    private String dataSourceName;

    private FilterDef catBaseDef;

    public DashboardOptionCatBaseDef(String dataSourceName, FilterDef catBaseDef) {
        this.dataSourceName = dataSourceName;
        this.catBaseDef = catBaseDef;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public FilterDef getCatBaseDef() {
        return catBaseDef;
    }

    @Override
    public String toString() {
        return "DashboardOptionCatBaseDef [dataSourceName=" + dataSourceName + ", catBaseDef=" + catBaseDef + "]";
    }

}
