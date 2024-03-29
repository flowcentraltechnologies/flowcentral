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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tcdng.unify.core.util.xml.MarshalFalseToNullXmlAdapter;

/**
 * Workflow configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@XmlRootElement(name = "workflow")
public class WfConfig extends BaseNameConfig {

    private WfStepsConfig stepsConfig;

    private String entity;

    private String loadingTable;
    
    private String descFormat;
    
    private Boolean supportMultiItemAction;

    private List<WfFilterConfig> filterList;

    private List<WfSetValuesConfig> setValuesList;
    
    public WfConfig() {
        this.supportMultiItemAction = Boolean.FALSE;
    }

    public WfStepsConfig getStepsConfig() {
        return stepsConfig;
    }

    @XmlElement(name = "steps")
    public void setStepsConfig(WfStepsConfig stepsConfig) {
        this.stepsConfig = stepsConfig;
    }

    public String getEntity() {
        return entity;
    }

    @XmlAttribute(required = true)
    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getLoadingTable() {
        return loadingTable;
    }

    @XmlAttribute
    public void setLoadingTable(String loadingTable) {
        this.loadingTable = loadingTable;
    }

    public String getDescFormat() {
        return descFormat;
    }

    @XmlAttribute
    public void setDescFormat(String descFormat) {
        this.descFormat = descFormat;
    }

    public Boolean getSupportMultiItemAction() {
        return supportMultiItemAction;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setSupportMultiItemAction(Boolean supportMultiItemAction) {
        this.supportMultiItemAction = supportMultiItemAction;
    }

    public List<WfFilterConfig> getFilterList() {
        return filterList;
    }

    @XmlElement(name = "filter", required = true)
    public void setFilterList(List<WfFilterConfig> filterList) {
        this.filterList = filterList;
    }

    public List<WfSetValuesConfig> getSetValuesList() {
        return setValuesList;
    }

    @XmlElement(name = "setValues")
    public void setSetValuesList(List<WfSetValuesConfig> setValuesList) {
        this.setValuesList = setValuesList;
    }

}
