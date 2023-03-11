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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.flowcentraltech.flowcentral.configuration.constants.DashboardTileType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.DashboardTileTypeXmlAdapter;

/**
 * Dashboard tile configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class DashboardTileConfig extends BaseNameConfig {

    private DashboardTileType type;

    private String chart;

    private int section;

    private int index;

    public DashboardTileConfig() {
        type = DashboardTileType.SIMPLE;
    }

    public DashboardTileType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(DashboardTileTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setType(DashboardTileType type) {
        this.type = type;
    }

    public String getChart() {
        return chart;
    }

    @XmlAttribute(required = true)
    public void setChart(String chart) {
        this.chart = chart;
    }

    public int getSection() {
        return section;
    }

    @XmlAttribute(required = true)
    public void setSection(int section) {
        this.section = section;
    }

    public int getIndex() {
        return index;
    }

    @XmlAttribute
    public void setIndex(int index) {
        this.index = index;
    }

}
