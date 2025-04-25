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

package com.flowcentraltech.flowcentral.configuration.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
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
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "chartDataSource")
public class AppChartDataSourceConfig extends BaseNameConfig {
    
    @JsonSerialize(using = ChartDataSourceTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = ChartDataSourceTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private ChartDataSourceType type;

    @JsonSerialize(using = ChartTimeSeriesTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = ChartTimeSeriesTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "timeseries")
    private ChartTimeSeriesType timeSeriesType;
    
    @JacksonXmlProperty
    private String entity;
    
    @JacksonXmlProperty(isAttribute = true, localName = "preferred-category-field")
    private String categoryField;
    
    @JacksonXmlProperty(isAttribute = true)
    private Integer limit;

    @JacksonXmlProperty(localName = "category-base")
    private FilterConfig categoryBase;
    
    @JacksonXmlProperty
    private PropertySequenceConfig series;
    
    @JacksonXmlProperty
    private PropertySequenceConfig categories;

    @JacksonXmlProperty
    private FieldSequenceConfig fieldSequence;
   
    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public ChartDataSourceType getType() {
        return type;
    }

    public void setType(ChartDataSourceType type) {
        this.type = type;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public FilterConfig getCategoryBase() {
        return categoryBase;
    }

    public void setCategoryBase(FilterConfig categoryBase) {
        this.categoryBase = categoryBase;
    }

    public PropertySequenceConfig getSeries() {
        return series;
    }

    public void setSeries(PropertySequenceConfig series) {
        this.series = series;
    }

    public PropertySequenceConfig getCategories() {
        return categories;
    }

    public void setCategories(PropertySequenceConfig categories) {
        this.categories = categories;
    }

    public ChartTimeSeriesType getTimeSeriesType() {
        return timeSeriesType;
    }

    public void setTimeSeriesType(ChartTimeSeriesType timeSeriesType) {
        this.timeSeriesType = timeSeriesType;
    }

    public String getCategoryField() {
        return categoryField;
    }

    public void setCategoryField(String categoryField) {
        this.categoryField = categoryField;
    }

    public FieldSequenceConfig getFieldSequence() {
        return fieldSequence;
    }

    public void setFieldSequence(FieldSequenceConfig fieldSequence) {
        this.fieldSequence = fieldSequence;
    }

}
