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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.flowcentraltech.flowcentral.configuration.constants.ChartDataSourceType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartTimeSeriesType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.ChartDataSourceTypeXmlAdapter;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.ChartTimeSeriesTypeXmlAdapter;

/**
 * Chart data source configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@XmlRootElement(name = "chartDataSource")
public class AppChartDataSourceConfig extends BaseNameConfig {
    
    private ChartDataSourceType type;

    private ChartTimeSeriesType timeSeriesType;
    
    private String entity;
    
    private String categoryField;

    private FilterConfig categoryBase;
    
    private PropertySequenceConfig series;
    
    private PropertySequenceConfig categories;
    
    public String getEntity() {
        return entity;
    }

    @XmlAttribute
    public void setEntity(String entity) {
        this.entity = entity;
    }

    public ChartDataSourceType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(ChartDataSourceTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setType(ChartDataSourceType type) {
        this.type = type;
    }

    public FilterConfig getCategoryBase() {
        return categoryBase;
    }

    @XmlElement(name = "category-base", required = true)
    public void setCategoryBase(FilterConfig categoryBase) {
        this.categoryBase = categoryBase;
    }

    public PropertySequenceConfig getSeries() {
        return series;
    }

    @XmlElement(required = true)
    public void setSeries(PropertySequenceConfig series) {
        this.series = series;
    }

    public PropertySequenceConfig getCategories() {
        return categories;
    }

    @XmlElement(required = true)
    public void setCategories(PropertySequenceConfig categories) {
        this.categories = categories;
    }

    public ChartTimeSeriesType getTimeSeriesType() {
        return timeSeriesType;
    }

    @XmlJavaTypeAdapter(ChartTimeSeriesTypeXmlAdapter.class)
    @XmlAttribute(name = "timeseries")
    public void setTimeSeriesType(ChartTimeSeriesType timeSeriesType) {
        this.timeSeriesType = timeSeriesType;
    }

    public String getCategoryField() {
        return categoryField;
    }

    @XmlAttribute(name = "preferred-category-field")
    public void setCategoryField(String categoryField) {
        this.categoryField = categoryField;
    }

}
