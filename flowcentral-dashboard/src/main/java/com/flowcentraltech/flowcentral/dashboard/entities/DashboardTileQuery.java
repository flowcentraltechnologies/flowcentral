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

package com.flowcentraltech.flowcentral.dashboard.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntityQuery;

/**
 * Dashboard tile query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class DashboardTileQuery extends BaseConfigNamedEntityQuery<DashboardTile> {

    public DashboardTileQuery() {
        super(DashboardTile.class);
    }

    public DashboardTileQuery dashboardId(Long dashboardId) {
        return (DashboardTileQuery) addEquals("dashboardId", dashboardId);
    }

    public DashboardTileQuery applicationNameNot(String applicationName) {
        return (DashboardTileQuery) addNotEquals("applicationName", applicationName);
    }

    public DashboardTileQuery chartBeginsWith(String chart) {
        return (DashboardTileQuery) addBeginsWith("chart", chart);
    }

}
