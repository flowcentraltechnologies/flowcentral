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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Property rule configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class PropertyRuleConfig extends BaseNameConfig {

    @JacksonXmlProperty(isAttribute = true)
    private String entity;

    @JacksonXmlProperty(isAttribute = true)
    private String choiceField;

    @JacksonXmlProperty(isAttribute = true)
    private String listField;

    @JacksonXmlProperty(isAttribute = true)
    private String propNameField;

    @JacksonXmlProperty(isAttribute = true)
    private String propValField;

    @JacksonXmlProperty(isAttribute = true)
    private String defaultList;

    @JacksonXmlProperty(isAttribute = true)
    private boolean ignoreCase;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "choice")
    private List<ChoiceConfig> choiceList;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getChoiceField() {
        return choiceField;
    }

    public void setChoiceField(String choiceField) {
        this.choiceField = choiceField;
    }

    public String getListField() {
        return listField;
    }

    public void setListField(String listField) {
        this.listField = listField;
    }

    public String getPropNameField() {
        return propNameField;
    }

    public void setPropNameField(String propNameField) {
        this.propNameField = propNameField;
    }

    public String getPropValField() {
        return propValField;
    }

    public void setPropValField(String propValField) {
        this.propValField = propValField;
    }

    public String getDefaultList() {
        return defaultList;
    }

    public void setDefaultList(String defaultList) {
        this.defaultList = defaultList;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public List<ChoiceConfig> getChoiceList() {
        return choiceList;
    }

    public void setChoiceList(List<ChoiceConfig> choiceList) {
        this.choiceList = choiceList;
    }

}
