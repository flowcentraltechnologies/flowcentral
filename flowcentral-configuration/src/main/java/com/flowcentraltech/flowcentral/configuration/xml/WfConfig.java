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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Workflow configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JacksonXmlRootElement(localName = "workflow")
public class WfConfig extends BaseNameConfig {

    @JacksonXmlProperty(localName = "steps")
    private WfStepsConfig stepsConfig;

    @JacksonXmlProperty(isAttribute = true)
    private String entity;

    @JacksonXmlProperty(isAttribute = true)
    private String loadingTable;
    
    @JacksonXmlProperty(isAttribute = true)
    private String descFormat;
    
    @JacksonXmlProperty(isAttribute = true)
    private Boolean supportMultiItemAction;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "filter")
    private List<WfFilterConfig> filterList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "setValues")
    private List<WfSetValuesConfig> setValuesList;
    
    public WfConfig() {
        this.supportMultiItemAction = Boolean.FALSE;
    }

    public WfStepsConfig getStepsConfig() {
        return stepsConfig;
    }

    public void setStepsConfig(WfStepsConfig stepsConfig) {
        this.stepsConfig = stepsConfig;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getLoadingTable() {
        return loadingTable;
    }

    public void setLoadingTable(String loadingTable) {
        this.loadingTable = loadingTable;
    }

    public String getDescFormat() {
        return descFormat;
    }

    public void setDescFormat(String descFormat) {
        this.descFormat = descFormat;
    }

    public Boolean getSupportMultiItemAction() {
        return supportMultiItemAction;
    }

    public void setSupportMultiItemAction(Boolean supportMultiItemAction) {
        this.supportMultiItemAction = supportMultiItemAction;
    }

    public List<WfFilterConfig> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<WfFilterConfig> filterList) {
        this.filterList = filterList;
    }

    public List<WfSetValuesConfig> getSetValuesList() {
        return setValuesList;
    }

    public void setSetValuesList(List<WfSetValuesConfig> setValuesList) {
        this.setValuesList = setValuesList;
    }

}
