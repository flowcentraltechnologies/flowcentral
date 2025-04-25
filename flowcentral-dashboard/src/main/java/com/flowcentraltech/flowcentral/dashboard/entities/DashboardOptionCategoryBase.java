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

import com.flowcentraltech.flowcentral.application.entities.AppFilter;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Child;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;

/**
 * Dashboard option category base entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_DASHBOARDOPTIONCATBASE")
public class DashboardOptionCategoryBase extends BaseAuditEntity {

    @ForeignKey(DashboardOption.class)
    private Long dashboardOptionId;

    @Column(name = "CHART_DATASOURCE", length = 2048)
    private String chartDataSource;

    @Column(length = 64)
    private String entity;
    
    @Child(category = "dashboard-option")
    private AppFilter categoryBase;

    @Override
    public String getDescription() {
        return chartDataSource;
    }

    public Long getDashboardOptionId() {
        return dashboardOptionId;
    }

    public void setDashboardOptionId(Long dashboardOptionId) {
        this.dashboardOptionId = dashboardOptionId;
    }

    public String getChartDataSource() {
        return chartDataSource;
    }

    public void setChartDataSource(String chartDataSource) {
        this.chartDataSource = chartDataSource;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public AppFilter getCategoryBase() {
        return categoryBase;
    }

    public void setCategoryBase(AppFilter categoryBase) {
        this.categoryBase = categoryBase;
    }

}
