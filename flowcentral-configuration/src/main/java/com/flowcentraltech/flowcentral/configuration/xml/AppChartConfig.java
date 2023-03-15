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

package com.flowcentraltech.flowcentral.configuration.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.flowcentraltech.flowcentral.configuration.constants.ChartPaletteType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.ChartPaletteTypeXmlAdapter;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.ChartTypeXmlAdapter;

/**
 * Chart configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@XmlRootElement(name = "chart")
public class AppChartConfig extends BaseNameConfig {

    private ChartType type;

    private ChartPaletteType paletteType;

    private String title;

    private String subTitle;

    private String provider;

    private String rule;

    private int width;

    private int height;

    private boolean stacked;

    private boolean showGrid;

    private boolean showDataLabels;

    private boolean formatDataLabels;

    private boolean formatYLabels;

    private boolean smooth;

    public AppChartConfig() {
        paletteType = ChartPaletteType.PALETTE1;
    }

    public ChartType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(ChartTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setType(ChartType type) {
        this.type = type;
    }

    public ChartPaletteType getPaletteType() {
        return paletteType;
    }

    @XmlJavaTypeAdapter(ChartPaletteTypeXmlAdapter.class)
    @XmlAttribute(name = "palette")
    public void setPaletteType(ChartPaletteType paletteType) {
        this.paletteType = paletteType;
    }

    public String getTitle() {
        return title;
    }

    @XmlAttribute
    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    @XmlAttribute
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getProvider() {
        return provider;
    }

    @XmlAttribute(required = true)
    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getRule() {
        return rule;
    }

    @XmlAttribute
    public void setRule(String rule) {
        this.rule = rule;
    }

    public int getWidth() {
        return width;
    }

    @XmlAttribute
    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    @XmlAttribute
    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isStacked() {
        return stacked;
    }

    @XmlAttribute
    public void setStacked(boolean stacked) {
        this.stacked = stacked;
    }

    public boolean isShowGrid() {
        return showGrid;
    }

    @XmlAttribute
    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
    }

    public boolean isShowDataLabels() {
        return showDataLabels;
    }

    @XmlAttribute
    public void setShowDataLabels(boolean showDataLabels) {
        this.showDataLabels = showDataLabels;
    }

    public boolean isSmooth() {
        return smooth;
    }

    @XmlAttribute
    public void setSmooth(boolean smooth) {
        this.smooth = smooth;
    }

    public boolean isFormatDataLabels() {
        return formatDataLabels;
    }

    @XmlAttribute
    public void setFormatDataLabels(boolean formatDataLabels) {
        this.formatDataLabels = formatDataLabels;
    }

    public boolean isFormatYLabels() {
        return formatYLabels;
    }

    @XmlAttribute
    public void setFormatYLabels(boolean formatYLabels) {
        this.formatYLabels = formatYLabels;
    }

}
