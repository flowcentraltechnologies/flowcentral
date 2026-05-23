/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.configuration.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.flowcentraltech.flowcentral.configuration.constants.ChartPaletteType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.ChartPaletteTypeXmlAdapter;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.ChartTypeXmlAdapter;

/**
 * Chart configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "chart")
public class AppChartConfig extends BaseClassifiedConfig {

    @JsonSerialize(using = ChartTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = ChartTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private ChartType type;

    @JsonSerialize(using = ChartPaletteTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = ChartPaletteTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "palette")
    private ChartPaletteType paletteType;

    @JacksonXmlProperty(isAttribute = true)
    private String title;

    @JacksonXmlProperty(isAttribute = true)
    private String subTitle;

    @JacksonXmlProperty(isAttribute = true)
    private String provider;

    @JacksonXmlProperty(isAttribute = true)
    private String category;

    @JacksonXmlProperty(isAttribute = true)
    private String series;

    @JacksonXmlProperty(isAttribute = true)
    private String color;

    @JacksonXmlProperty(isAttribute = true)
    private String rule;

    @JacksonXmlProperty(isAttribute = true)
    private Integer width;

    @JacksonXmlProperty(isAttribute = true)
    private Integer height;

    @JacksonXmlProperty(isAttribute = true)
    private boolean stacked;

    @JacksonXmlProperty(isAttribute = true)
    private boolean showGrid;

    @JacksonXmlProperty(isAttribute = true)
    private boolean showDataLabels;

    @JacksonXmlProperty(isAttribute = true)
    private boolean formatDataLabels;

    @JacksonXmlProperty(isAttribute = true)
    private boolean formatYLabels;

    @JacksonXmlProperty(isAttribute = true)
    private boolean smooth;

    public AppChartConfig() {
        paletteType = ChartPaletteType.PALETTE1;
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
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

    public boolean isFormatDataLabels() {
        return formatDataLabels;
    }

    public void setFormatDataLabels(boolean formatDataLabels) {
        this.formatDataLabels = formatDataLabels;
    }

    public boolean isFormatYLabels() {
        return formatYLabels;
    }

    public void setFormatYLabels(boolean formatYLabels) {
        this.formatYLabels = formatYLabels;
    }

}
