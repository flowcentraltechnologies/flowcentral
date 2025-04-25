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
import com.flowcentraltech.flowcentral.configuration.constants.DashboardTileType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.DashboardTileTypeXmlAdapter;

/**
 * Dashboard tile configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class DashboardTileConfig extends BaseNameConfig {

    @JsonSerialize(using = DashboardTileTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = DashboardTileTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private DashboardTileType type;

    @JacksonXmlProperty(isAttribute = true)
    private String chart;

    @JacksonXmlProperty(isAttribute = true)
    private int section;

    @JacksonXmlProperty(isAttribute = true)
    private int index;

    public DashboardTileConfig() {
        type = DashboardTileType.SIMPLE;
    }

    public DashboardTileType getType() {
        return type;
    }

    public void setType(DashboardTileType type) {
        this.type = type;
    }

    public String getChart() {
        return chart;
    }

    public void setChart(String chart) {
        this.chart = chart;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
