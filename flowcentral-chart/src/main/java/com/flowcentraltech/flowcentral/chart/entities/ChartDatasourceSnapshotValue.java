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

import java.math.BigDecimal;
import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;

/**
 * Chart datasource snapshot value entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_DATASOURCESNAPSHOTVALUE")
public class ChartDatasourceSnapshotValue extends BaseEntity {

    @ForeignKey(ChartDatasourceSnapshotSeries.class)
    private Long chartDatasourceSnapshotSeriesId;

    @Column(precision = 20, scale = 2)
    private BigDecimal seriesValue;

    @Column(nullable = true)
    private Long numberCategory;

    @Column(nullable = true)
    private Date dateCategory;
    
    @Override
    public String getDescription() {
        return null;
    }

    public Long getChartDatasourceSnapshotSeriesId() {
        return chartDatasourceSnapshotSeriesId;
    }

    public void setChartDatasourceSnapshotSeriesId(Long chartDatasourceSnapshotSeriesId) {
        this.chartDatasourceSnapshotSeriesId = chartDatasourceSnapshotSeriesId;
    }

    public BigDecimal getSeriesValue() {
        return seriesValue;
    }

    public void setSeriesValue(BigDecimal seriesValue) {
        this.seriesValue = seriesValue;
    }

    public Long getNumberCategory() {
        return numberCategory;
    }

    public void setNumberCategory(Long numberCategory) {
        this.numberCategory = numberCategory;
    }

    public Date getDateCategory() {
        return dateCategory;
    }

    public void setDateCategory(Date dateCategory) {
        this.dateCategory = dateCategory;
    }

}
