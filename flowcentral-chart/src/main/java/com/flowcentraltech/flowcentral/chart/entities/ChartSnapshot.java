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

import java.util.List;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.flowcentraltech.flowcentral.configuration.constants.ChartCategoryDataType;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Chart snapshot entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_CHARTSNAPSHOT",
        uniqueConstraints = { @UniqueConstraint({ "name" }), @UniqueConstraint({ "description" }) })
public class ChartSnapshot extends BaseAuditEntity {

    @ForeignKey
    private ChartCategoryDataType categoryDataType;

    @Column(name = "CHARTSNAPSHOT_NM", length = 64)
    private String name;

    @Column(name = "CHARTSNAPSHOT_DESC", length = 96)
    private String description;

    @Column(length = 512)
    private String categories;

    @ListOnly(key = "categoryDataType", property = "description")
    private String categoryDataTypeDesc;

    @ChildList
    private List<ChartSnapshotSeries> seriesList;

    public ChartSnapshot(ChartCategoryDataType categoryDataType, String name, String description, String categories) {
        this.categoryDataType = categoryDataType;
        this.name = name;
        this.description = description;
        this.categories = categories;
    }

    public ChartSnapshot() {

    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getListKey() {
        return name;
    }

    public ChartCategoryDataType getCategoryDataType() {
        return categoryDataType;
    }

    public void setCategoryDataType(ChartCategoryDataType categoryDataType) {
        this.categoryDataType = categoryDataType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public List<ChartSnapshotSeries> getSeriesList() {
        return seriesList;
    }

    public void setSeriesList(List<ChartSnapshotSeries> seriesList) {
        this.seriesList = seriesList;
    }

    public String getCategoryDataTypeDesc() {
        return categoryDataTypeDesc;
    }

    public void setCategoryDataTypeDesc(String categoryDataTypeDesc) {
        this.categoryDataTypeDesc = categoryDataTypeDesc;
    }

}
