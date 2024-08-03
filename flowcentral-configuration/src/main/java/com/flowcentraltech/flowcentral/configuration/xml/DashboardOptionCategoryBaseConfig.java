/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Dashboard option category base configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class DashboardOptionCategoryBaseConfig extends BaseConfig  {

    @JacksonXmlProperty(isAttribute = true)
    private String chartDataSource;

    @JacksonXmlProperty(isAttribute = true)
    private String entity;

    @JacksonXmlProperty(localName = "base-condition")
    private FilterConfig categoryBase;

    public String getChartDataSource() {
        return chartDataSource;
    }

    public void setChartDataSource(String chartDataSource) {
        this.chartDataSource = chartDataSource;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public FilterConfig getCategoryBase() {
        return categoryBase;
    }

    public void setCategoryBase(FilterConfig categoryBase) {
        this.categoryBase = categoryBase;
    }

}
