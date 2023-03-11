/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.report.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.constant.VAlignType;
import com.tcdng.unify.core.constant.XOffsetType;
import com.tcdng.unify.core.constant.YOffsetType;
import com.tcdng.unify.core.report.ReportPlacementType;

/**
 * Report placement.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_REPORTPLACEMENT")
public class ReportPlacement extends BaseEntity {

    @ForeignKey(ReportConfiguration.class)
    private Long reportConfigurationId;

    @ForeignKey(name = "PLACEMENT_TYPE")
    private ReportPlacementType type;

    @ForeignKey(nullable = true)
    private HAlignType horizAlignType;

    @ForeignKey(nullable = true)
    private VAlignType vertAlignType;

    @ForeignKey
    private XOffsetType xOffsetType;

    @ForeignKey
    private YOffsetType yOffsetType;

    @Column(name = "FIELD_NM", nullable = true)
    private String fieldName;

    @Column(name = "PLACEMENT_TEXT", length = 512, nullable = true)
    private String text;

    @Column(name = "FORMATTER", length = 64, nullable = true)
    private String formatter;

    @Column(name = "PLACEMENT_X")
    private int x;

    @Column(name = "PLACEMENT_Y")
    private int y;

    @Column(name = "PLACEMENT_WIDTH")
    private int width;

    @Column(name = "PLACEMENT_HEIGHT")
    private int height;

    @Column(name = "BOLD_FG")
    private boolean bold;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    @ListOnly(key = "horizAlignType", property = "description")
    private String horizAlignTypeDesc;

    @ListOnly(key = "vertAlignType", property = "description")
    private String vertAlignTypeDesc;

    @ListOnly(key = "xOffsetType", property = "description")
    private String xOffsetTypeDesc;

    @ListOnly(key = "yOffsetType", property = "description")
    private String yOffsetTypeDesc;

    public Long getReportConfigurationId() {
        return reportConfigurationId;
    }

    public void setReportConfigurationId(Long reportConfigurationId) {
        this.reportConfigurationId = reportConfigurationId;
    }

    @Override
    public String getDescription() {
        return fieldName != null ? fieldName : text;
    }

    public ReportPlacementType getType() {
        return type;
    }

    public void setType(ReportPlacementType type) {
        this.type = type;
    }

    public HAlignType getHorizAlignType() {
        return horizAlignType;
    }

    public void setHorizAlignType(HAlignType horizAlignType) {
        this.horizAlignType = horizAlignType;
    }

    public VAlignType getVertAlignType() {
        return vertAlignType;
    }

    public void setVertAlignType(VAlignType vertAlignType) {
        this.vertAlignType = vertAlignType;
    }

    public XOffsetType getXOffsetType() {
        return xOffsetType;
    }

    public void setXOffsetType(XOffsetType xOffsetType) {
        this.xOffsetType = xOffsetType;
    }

    public YOffsetType getYOffsetType() {
        return yOffsetType;
    }

    public void setYOffsetType(YOffsetType yOffsetType) {
        this.yOffsetType = yOffsetType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFormatter() {
        return formatter;
    }

    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getHorizAlignTypeDesc() {
        return horizAlignTypeDesc;
    }

    public void setHorizAlignTypeDesc(String horizAlignTypeDesc) {
        this.horizAlignTypeDesc = horizAlignTypeDesc;
    }

    public String getVertAlignTypeDesc() {
        return vertAlignTypeDesc;
    }

    public void setVertAlignTypeDesc(String vertAlignTypeDesc) {
        this.vertAlignTypeDesc = vertAlignTypeDesc;
    }

    public String getXOffsetTypeDesc() {
        return xOffsetTypeDesc;
    }

    public void setXOffsetTypeDesc(String xOffsetTypeDesc) {
        this.xOffsetTypeDesc = xOffsetTypeDesc;
    }

    public String getYOffsetTypeDesc() {
        return yOffsetTypeDesc;
    }

    public void setYOffsetTypeDesc(String yOffsetTypeDesc) {
        this.yOffsetTypeDesc = yOffsetTypeDesc;
    }

}
