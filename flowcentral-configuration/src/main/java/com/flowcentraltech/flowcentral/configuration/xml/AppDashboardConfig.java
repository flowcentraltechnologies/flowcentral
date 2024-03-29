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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tcdng.unify.core.util.xml.MarshalFalseToNullXmlAdapter;

/**
 * Application dashboard configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppDashboardConfig extends BaseNameConfig {

    private Boolean allowSecondaryTenants;

    private int sections;

    private List<DashboardSectionConfig> sectionList;
    
    private List<DashboardTileConfig> tileList;
    
    private List<DashboardOptionConfig> optionsList;

    public AppDashboardConfig() {
        this.allowSecondaryTenants = Boolean.FALSE;
    }
    
    public Boolean getAllowSecondaryTenants() {
        return allowSecondaryTenants;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setAllowSecondaryTenants(Boolean allowSecondaryTenants) {
        this.allowSecondaryTenants = allowSecondaryTenants;
    }

    public int getSections() {
        return sections;
    }

    @XmlAttribute(required = true)
    public void setSections(int sections) {
        this.sections = sections;
    }

    public List<DashboardSectionConfig> getSectionList() {
        return sectionList;
    }

    @XmlElement(name = "dashboard-section", required = true)
    public void setSectionList(List<DashboardSectionConfig> sectionList) {
        this.sectionList = sectionList;
    }

    public List<DashboardTileConfig> getTileList() {
        return tileList;
    }

    @XmlElement(name = "dashboard-tile", required = true)
    public void setTileList(List<DashboardTileConfig> tileList) {
        this.tileList = tileList;
    }

    public List<DashboardOptionConfig> getOptionsList() {
        return optionsList;
    }

    @XmlElement(name = "dashboard-option", required = true)
    public void setOptionsList(List<DashboardOptionConfig> optionsList) {
        this.optionsList = optionsList;
    }

}
