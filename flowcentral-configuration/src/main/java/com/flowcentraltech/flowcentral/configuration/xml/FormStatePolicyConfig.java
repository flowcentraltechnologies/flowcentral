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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.flowcentraltech.flowcentral.configuration.constants.FormStatePolicyType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.FormStatePolicyTypeXmlAdapter;

/**
 * Form state policy configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormStatePolicyConfig extends BaseNameConfig {

    private FormStatePolicyType type;
    
    private FilterConfig onCondition;

    private SetStatesConfig setStates;

    private SetValuesConfig setValues;

    private String valueGenerator;

    private String trigger;

    public FormStatePolicyType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(FormStatePolicyTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setType(FormStatePolicyType type) {
        this.type = type;
    }

    public FilterConfig getOnCondition() {
        return onCondition;
    }

    @XmlElement(required = true)
    public void setOnCondition(FilterConfig onCondition) {
        this.onCondition = onCondition;
    }

    public SetStatesConfig getSetStates() {
        return setStates;
    }

    @XmlElement(name = "setStates")
    public void setSetStates(SetStatesConfig setStates) {
        this.setStates = setStates;
    }

    public SetValuesConfig getSetValues() {
        return setValues;
    }

    @XmlElement(name = "setValues")
    public void setSetValues(SetValuesConfig setValues) {
        this.setValues = setValues;
    }

    public String getValueGenerator() {
        return valueGenerator;
    }

    @XmlAttribute
    public void setValueGenerator(String valueGenerator) {
        this.valueGenerator = valueGenerator;
    }

    public String getTrigger() {
        return trigger;
    }

    @XmlAttribute
    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

}
