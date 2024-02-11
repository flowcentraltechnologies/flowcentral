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

package com.flowcentraltech.flowcentral.chart.entities;

import com.flowcentraltech.flowcentral.application.entities.AppFilter;
import com.flowcentraltech.flowcentral.application.entities.AppPropertySequence;
import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.configuration.constants.ChartDataSourceType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartTimeSeriesType;
import com.tcdng.unify.core.annotation.Child;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Chart data source entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_CHARTDATASOURCE")
public class ChartDataSource extends BaseApplicationEntity {

    @ForeignKey(name = "CHARTDATASOURCE_TY")
    private ChartDataSourceType type;

    @ForeignKey(name = "CHARTTIMESERIES_TY", nullable = true)
    private ChartTimeSeriesType timeSeriesType;
    
    @Column(length = 64)
    private String entity;

    @Column(length = 32, nullable = true)
    private String categoryField;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    @ListOnly(key = "timeSeriesType", property = "description")
    private String timeSeriesTypeDesc;
    
    @Child(category = "chart-datasource")
    private AppFilter categoryBase;
    
    @Child(category = "entity-series")
    private AppPropertySequence series;
    
    @Child(category = "entity-category")
    private AppPropertySequence categories;

    public ChartDataSourceType getType() {
        return type;
    }

    public void setType(ChartDataSourceType type) {
        this.type = type;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public AppFilter getCategoryBase() {
        return categoryBase;
    }

    public void setCategoryBase(AppFilter categoryBase) {
        this.categoryBase = categoryBase;
    }

    public AppPropertySequence getSeries() {
        return series;
    }

    public void setSeries(AppPropertySequence series) {
        this.series = series;
    }

    public AppPropertySequence getCategories() {
        return categories;
    }

    public void setCategories(AppPropertySequence categories) {
        this.categories = categories;
    }

    public ChartTimeSeriesType getTimeSeriesType() {
        return timeSeriesType;
    }

    public void setTimeSeriesType(ChartTimeSeriesType timeSeriesType) {
        this.timeSeriesType = timeSeriesType;
    }

    public String getCategoryField() {
        return categoryField;
    }

    public void setCategoryField(String categoryField) {
        this.categoryField = categoryField;
    }

    public String getTimeSeriesTypeDesc() {
        return timeSeriesTypeDesc;
    }

    public void setTimeSeriesTypeDesc(String timeSeriesTypeDesc) {
        this.timeSeriesTypeDesc = timeSeriesTypeDesc;
    }

}
