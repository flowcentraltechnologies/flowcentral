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

/**
 * Property rule configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class PropertyRuleConfig extends BaseNameConfig {

    private String entity;

    private String choiceField;

    private String listField;

    private String propNameField;

    private String propValField;

    private String defaultList;

    private boolean ignoreCase;

    private List<ChoiceConfig> choiceList;

    public String getEntity() {
        return entity;
    }

    @XmlAttribute(required = true)
    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getChoiceField() {
        return choiceField;
    }

    @XmlAttribute(required = true)
    public void setChoiceField(String choiceField) {
        this.choiceField = choiceField;
    }

    public String getListField() {
        return listField;
    }

    @XmlAttribute(required = true)
    public void setListField(String listField) {
        this.listField = listField;
    }

    public String getPropNameField() {
        return propNameField;
    }

    @XmlAttribute(required = true)
    public void setPropNameField(String propNameField) {
        this.propNameField = propNameField;
    }

    public String getPropValField() {
        return propValField;
    }

    @XmlAttribute(required = true)
    public void setPropValField(String propValField) {
        this.propValField = propValField;
    }

    public String getDefaultList() {
        return defaultList;
    }

    @XmlAttribute
    public void setDefaultList(String defaultList) {
        this.defaultList = defaultList;
    }

    @XmlAttribute
    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public List<ChoiceConfig> getChoiceList() {
        return choiceList;
    }

    @XmlElement(name = "choice")
    public void setChoiceList(List<ChoiceConfig> choiceList) {
        this.choiceList = choiceList;
    }

}
