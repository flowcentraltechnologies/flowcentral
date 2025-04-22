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

package com.flowcentraltech.flowcentral.dashboard.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntity;
import com.flowcentraltech.flowcentral.configuration.constants.DashboardTileType;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Dashboard snapshot entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_DASHBOARDTILE", uniqueConstraints = {
        @UniqueConstraint({ "dashboardId", "name" }),
        @UniqueConstraint({ "dashboardId", "description" }) })
public class DashboardTile extends BaseConfigNamedEntity {

    @ForeignKey(Dashboard.class)
    private Long dashboardId;

    @ForeignKey(name = "DASHBOARDTILE_TY")
    private DashboardTileType type;

    @Column(name = "CHART_NM", length = 128)
    private String chart;

    @Column
    private int section;

    @Column(name = "DISPLAY_INDEX")
    private int index;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    @ListOnly(key = "dashboardId", property = "name")
    private String dashboardName;

    @ListOnly(key = "dashboardId", property = "applicationName")
    private String applicationName;

    public DashboardTile() {

    }

    public Long getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(Long dashboardId) {
        this.dashboardId = dashboardId;
    }

    public DashboardTileType getType() {
        return type;
    }

    public void setType(DashboardTileType type) {
        this.type = type;
    }

    public String getChart() {
        return chart;
    }

    public void setChart(String chart) {
        this.chart = chart;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getDashboardName() {
        return dashboardName;
    }

    public void setDashboardName(String dashboardName) {
        this.dashboardName = dashboardName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

}
