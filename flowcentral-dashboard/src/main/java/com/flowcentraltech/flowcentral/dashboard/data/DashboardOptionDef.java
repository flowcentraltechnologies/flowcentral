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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.tcdng.unify.core.data.Listable;

/**
 * Dashboard option definition object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class DashboardOptionDef implements Listable {

    private String name;

    private String description;

    private String label;

    private Map<String, DashboardOptionCatBaseDef> catBases;

    public DashboardOptionDef(String name, String description, String label,
            List<DashboardOptionCatBaseDef> catBaseList) {
        this.name = name;
        this.description = description;
        this.label = label;
        this.catBases = new HashMap<String, DashboardOptionCatBaseDef>();
        for (DashboardOptionCatBaseDef dashboardOptionCatBaseDef : catBaseList) {
            catBases.put(dashboardOptionCatBaseDef.getDataSourceName(), dashboardOptionCatBaseDef);
        }
    }

    @Override
    public String getListKey() {
        return name;
    }

    @Override
    public String getListDescription() {
        return label;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLabel() {
        return label;
    }

    public FilterDef getCatBase(String dataSourceName) {
        return catBases.get(dataSourceName).getCatBaseDef();
    }

    public boolean isWithCatBase(String dataSourceName) {
        return catBases.containsKey(dataSourceName);
    }

}
