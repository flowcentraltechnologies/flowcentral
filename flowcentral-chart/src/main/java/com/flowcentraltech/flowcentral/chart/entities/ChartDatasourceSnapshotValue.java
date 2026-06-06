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
import com.tcdng.unify.common.annotation.ColumnType;
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
    private Long cdsSnapshotSeriesId;

    @Column(length = 64)
    private String fieldName;

    @Column(length = 64, nullable = true)
    private String fieldLabel;

    @Column(precision = 20, scale = 2, nullable = true)
    private BigDecimal numberValue;

    @Column(length = 128, nullable = true)
    private String textValue;

    @Column(type = ColumnType.TIMESTAMP, nullable = true)
    private Date dateValue;

    @Column
    private boolean grouping;
    
    @Override
    public String getDescription() {
        return fieldName;
    }

    public Long getCdsSnapshotSeriesId() {
        return cdsSnapshotSeriesId;
    }

    public void setCdsSnapshotSeriesId(Long cdsSnapshotSeriesId) {
        this.cdsSnapshotSeriesId = cdsSnapshotSeriesId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public BigDecimal getNumberValue() {
        return numberValue;
    }

    public void setNumberValue(BigDecimal numberValue) {
        this.numberValue = numberValue;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public boolean isGrouping() {
        return grouping;
    }

    public void setGrouping(boolean grouping) {
        this.grouping = grouping;
    }

}
