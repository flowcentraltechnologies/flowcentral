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

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.flowcentraltech.flowcentral.configuration.constants.ChartCategoryDataType;
import com.tcdng.unify.common.annotation.ColumnType;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;

/**
 * Chart datasource snapshot entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_DATASOURCESNAPSHOT")
public class ChartDatasourceSnapshot extends BaseAuditEntity {

    @ForeignKey(ChartDataSource.class)
    private Long chartDataSourceId;
    
    @ForeignKey
    private ChartCategoryDataType categoryDataType;
    
    @Column(type = ColumnType.TIMESTAMP)
    private Date snapshotExpiresOn;

    @Column(name = "ACTIVE_FG")
    private boolean active;
    
    @Override
    public String getDescription() {
        return null;
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

    public Date getSnapshotExpiresOn() {
        return snapshotExpiresOn;
    }

    public void setSnapshotExpiresOn(Date snapshotExpiresOn) {
        this.snapshotExpiresOn = snapshotExpiresOn;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
