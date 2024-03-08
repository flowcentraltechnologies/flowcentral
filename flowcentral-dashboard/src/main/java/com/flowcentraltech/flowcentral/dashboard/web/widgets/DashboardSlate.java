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

package com.flowcentraltech.flowcentral.dashboard.web.widgets;

import com.flowcentraltech.flowcentral.dashboard.data.DashboardDef;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Dashboard slate.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class DashboardSlate {

    private DashboardDef dashboardDef;

    private String option;
    
    public DashboardSlate(DashboardDef dashboardDef) {
        this(dashboardDef, null);
    }
   
    public DashboardSlate(DashboardDef dashboardDef, String option) {
        this.dashboardDef = dashboardDef;
        this.option = option;
    }

    public DashboardDef getDashboardDef() {
        return dashboardDef;
    }

    public String getOption() {
        return option;
    }

    public boolean isWithOption() {
        return !StringUtils.isBlank(option);
    }
}
