/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseEntity;
import com.flowcentraltech.flowcentral.configuration.constants.ChartCategoryDataType;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Chart datasource snapshot series entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_DATASOURCESNAPSHOTSERIES")
public class ChartDatasourceSnapshotSeries extends BaseEntity {

    @ForeignKey(ChartDatasourceSnapshot.class)
    private Long chartDatasourceSnapshotId;

    @Column(length = 64)
    private String seriesName;

    @Column(length = 96)
    private String seriesLabel;

    @Column(length = 64)
    private String categoryName;

    @Column(length = 96)
    private String categoryLabel;

    @Column(length = 64, nullable =  true)
    private String groupingName;

    @ListOnly(key = "chartDatasourceSnapshotId", property = "chartDataSourceId")
    private Long chartDataSourceId;
    
    @ListOnly(key = "chartDatasourceSnapshotId", property = "categoryDataType")
    private ChartCategoryDataType categoryDataType;
    
    @ListOnly(key = "chartDatasourceSnapshotId", property = "snapshotExpiresOn")
    private Date snapshotExpiresOn;

    @ListOnly(key = "chartDatasourceSnapshotId", property = "active")
    private boolean snapshotActive;
    
    @Override
    public String getDescription() {
        return categoryLabel + " - " + seriesLabel;
    }

    public Long getChartDatasourceSnapshotId() {
        return chartDatasourceSnapshotId;
    }

    public void setChartDatasourceSnapshotId(Long chartDatasourceSnapshotId) {
        this.chartDatasourceSnapshotId = chartDatasourceSnapshotId;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getSeriesLabel() {
        return seriesLabel;
    }

    public void setSeriesLabel(String seriesLabel) {
        this.seriesLabel = seriesLabel;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryLabel() {
        return categoryLabel;
    }

    public void setCategoryLabel(String categoryLabel) {
        this.categoryLabel = categoryLabel;
    }

    public Long getChartDataSourceId() {
        return chartDataSourceId;
    }

    public void setChartDataSourceId(Long chartDataSourceId) {
        this.chartDataSourceId = chartDataSourceId;
    }

    public ChartCategoryDataType getCategoryDataType() {
        return categoryDataType;
    }

    public void setCategoryDataType(ChartCategoryDataType categoryDataType) {
        this.categoryDataType = categoryDataType;
    }

    public String getGroupingName() {
        return groupingName;
    }

    public void setGroupingName(String groupingName) {
        this.groupingName = groupingName;
    }

    public Date getSnapshotExpiresOn() {
        return snapshotExpiresOn;
    }

    public void setSnapshotExpiresOn(Date snapshotExpiresOn) {
        this.snapshotExpiresOn = snapshotExpiresOn;
    }

    public boolean isSnapshotActive() {
        return snapshotActive;
    }

    public void setSnapshotActive(boolean snapshotActive) {
        this.snapshotActive = snapshotActive;
    }

}
