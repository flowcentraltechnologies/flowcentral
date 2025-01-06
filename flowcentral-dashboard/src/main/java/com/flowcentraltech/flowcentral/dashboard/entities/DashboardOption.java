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

import java.util.List;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Dashboard option entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_DASHBOARDOPTION")
public class DashboardOption extends BaseAuditEntity {

    @ForeignKey(Dashboard.class)
    private Long dashboardId;

    @Column(name = "OPTION_NM", length = 64)
    private String name;

    @Column(name = "OPTION_DESC", length = 128)
    private String description;

    @Column(name = "OPTION_LABEL", length = 96)
    private String label;
    
    @ListOnly(key = "dashboardId", property = "applicationName")
    private String applicationName;
    
    @ListOnly(key = "dashboardId", property = "name")
    private String dashboardName;
        
    @ChildList
    private List<DashboardOptionCategoryBase> baseList;

    public Long getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(Long dashboardId) {
        this.dashboardId = dashboardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getDashboardName() {
        return dashboardName;
    }

    public void setDashboardName(String dashboardName) {
        this.dashboardName = dashboardName;
    }

    public List<DashboardOptionCategoryBase> getBaseList() {
        return baseList;
    }

    public void setBaseList(List<DashboardOptionCategoryBase> baseList) {
        this.baseList = baseList;
    }

}
