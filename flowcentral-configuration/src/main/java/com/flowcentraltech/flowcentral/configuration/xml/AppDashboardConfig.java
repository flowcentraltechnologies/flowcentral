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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Application dashboard configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class AppDashboardConfig extends BaseClassifiedConfig {

    @JacksonXmlProperty(isAttribute = true)
    private Boolean allowSecondaryTenants;

    @JacksonXmlProperty(isAttribute = true)
    private int sections;

    @JacksonXmlProperty(localName = "dashboard-sections")
    private DashboardSectionsConfig sectionList;
    
    @JacksonXmlProperty(localName = "dashboard-tiles")
    private DashboardTilesConfig tiles;
    
    @JacksonXmlProperty(localName = "dashboard-options")
    private DashboardOptionsConfig options;

    public AppDashboardConfig() {
        this.allowSecondaryTenants = Boolean.FALSE;
    }
    
    public Boolean getAllowSecondaryTenants() {
        return allowSecondaryTenants;
    }

    public void setAllowSecondaryTenants(Boolean allowSecondaryTenants) {
        this.allowSecondaryTenants = allowSecondaryTenants;
    }

    public int getSections() {
        return sections;
    }

    public void setSections(int sections) {
        this.sections = sections;
    }

    public DashboardSectionsConfig getSectionList() {
        return sectionList;
    }

    public void setSectionList(DashboardSectionsConfig sectionList) {
        this.sectionList = sectionList;
    }

    public DashboardTilesConfig getTiles() {
        return tiles;
    }

    public void setTiles(DashboardTilesConfig tiles) {
        this.tiles = tiles;
    }

    public DashboardOptionsConfig getOptions() {
        return options;
    }

    public void setOptions(DashboardOptionsConfig options) {
        this.options = options;
    }

}
