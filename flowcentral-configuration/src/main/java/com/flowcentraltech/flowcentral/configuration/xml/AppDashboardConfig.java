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

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Application dashboard configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppDashboardConfig extends BaseNameConfig {

    @JacksonXmlProperty(isAttribute = true)
    private Boolean allowSecondaryTenants;

    @JacksonXmlProperty(isAttribute = true)
    private int sections;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "dashboard-section")
    private List<DashboardSectionConfig> sectionList;
    
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "dashboard-tile")
    private List<DashboardTileConfig> tileList;
    
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "dashboard-option")
    private List<DashboardOptionConfig> optionsList;

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

    public List<DashboardSectionConfig> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<DashboardSectionConfig> sectionList) {
        this.sectionList = sectionList;
    }

    public List<DashboardTileConfig> getTileList() {
        return tileList;
    }

    public void setTileList(List<DashboardTileConfig> tileList) {
        this.tileList = tileList;
    }

    public List<DashboardOptionConfig> getOptionsList() {
        return optionsList;
    }

    public void setOptionsList(List<DashboardOptionConfig> optionsList) {
        this.optionsList = optionsList;
    }

}
