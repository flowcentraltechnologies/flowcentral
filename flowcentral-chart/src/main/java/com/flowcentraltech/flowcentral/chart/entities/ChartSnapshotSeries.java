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

package com.flowcentraltech.flowcentral.chart.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseEntity;
import com.flowcentraltech.flowcentral.configuration.constants.ChartSeriesDataType;
import com.tcdng.unify.common.annotation.ColumnType;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Chart snapshot series entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_CHARTSNAPSHOTSERIES")
public class ChartSnapshotSeries extends BaseEntity {

    @ForeignKey(ChartSnapshot.class)
    private Long chartSnapshotId;

    @ForeignKey
    private ChartSeriesDataType seriesDataType;

    @Column(name = "CHARTSNAPSHOTSERIES_NM", length = 64)
    private String name;

    @Column(type = ColumnType.CLOB,  nullable = true)
    private String series;

    @ListOnly(key = "seriesDataType", property = "description")
    private String seriesDataTypeDesc;

    public ChartSnapshotSeries(ChartSeriesDataType seriesDataType, String name, String series) {
        this.seriesDataType = seriesDataType;
        this.name = name;
        this.series = series;
    }

    public ChartSnapshotSeries() {

    }

    @Override
    public String getDescription() {
        return name;
    }

    public Long getChartSnapshotId() {
        return chartSnapshotId;
    }

    public void setChartSnapshotId(Long chartSnapshotId) {
        this.chartSnapshotId = chartSnapshotId;
    }

    public ChartSeriesDataType getSeriesDataType() {
        return seriesDataType;
    }

    public void setSeriesDataType(ChartSeriesDataType seriesDataType) {
        this.seriesDataType = seriesDataType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getSeriesDataTypeDesc() {
        return seriesDataTypeDesc;
    }

    public void setSeriesDataTypeDesc(String seriesDataTypeDesc) {
        this.seriesDataTypeDesc = seriesDataTypeDesc;
    }

}
