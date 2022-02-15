/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.configuration.constants.ChartPaletteType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartType;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Chart entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_CHART")
public class Chart extends BaseApplicationEntity {

    @ForeignKey(name = "CHART_TY")
    private ChartType type;

    @ForeignKey(name = "PALETTE_TY")
    private ChartPaletteType paletteType;

    @Column(length = 96, nullable = true)
    private String title;

    @Column(length = 96, nullable = true)
    private String subTitle;

    @Column(length = 64)
    private String provider;

    @Column(length = 64, nullable = true)
    private String rule;

    @Column
    private int width;

    @Column
    private int height;

    @Column
    private boolean stacked;

    @Column
    private boolean showGrid;

    @Column
    private boolean showDataLabels;

    @Column
    private boolean smooth;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    @ListOnly(key = "paletteType", property = "description")
    private String paletteTypeDesc;

    public ChartType getType() {
        return type;
    }

    public void setType(ChartType type) {
        this.type = type;
    }

    public ChartPaletteType getPaletteType() {
        return paletteType;
    }

    public void setPaletteType(ChartPaletteType paletteType) {
        this.paletteType = paletteType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
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

    public boolean isStacked() {
        return stacked;
    }

    public void setStacked(boolean stacked) {
        this.stacked = stacked;
    }

    public boolean isShowGrid() {
        return showGrid;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
    }

    public boolean isShowDataLabels() {
        return showDataLabels;
    }

    public void setShowDataLabels(boolean showDataLabels) {
        this.showDataLabels = showDataLabels;
    }

    public boolean isSmooth() {
        return smooth;
    }

    public void setSmooth(boolean smooth) {
        this.smooth = smooth;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getPaletteTypeDesc() {
        return paletteTypeDesc;
    }

    public void setPaletteTypeDesc(String paletteTypeDesc) {
        this.paletteTypeDesc = paletteTypeDesc;
    }

}
