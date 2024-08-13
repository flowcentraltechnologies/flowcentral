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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.configuration.constants.FormStatePolicyType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.FormStatePolicyTypeXmlAdapter;

/**
 * Form state policy configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class FormStatePolicyConfig extends BaseNameConfig {

    @JsonSerialize(using = FormStatePolicyTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = FormStatePolicyTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private FormStatePolicyType type;
    
    @JacksonXmlProperty
    private FilterConfig onCondition;

    @JacksonXmlProperty(localName = "setStates")
    private SetStatesConfig setStates;

    @JacksonXmlProperty(localName = "setValues")
    private SetValuesConfig setValues;

    @JacksonXmlProperty(isAttribute = true)
    private String valueGenerator;

    @JacksonXmlProperty(isAttribute = true)
    private String trigger;

    @JacksonXmlProperty(isAttribute = true)
    private Integer executionIndex;

    public FormStatePolicyType getType() {
        return type;
    }

    public void setType(FormStatePolicyType type) {
        this.type = type;
    }

    public FilterConfig getOnCondition() {
        return onCondition;
    }

    public void setOnCondition(FilterConfig onCondition) {
        this.onCondition = onCondition;
    }

    public SetStatesConfig getSetStates() {
        return setStates;
    }

    public void setSetStates(SetStatesConfig setStates) {
        this.setStates = setStates;
    }

    public SetValuesConfig getSetValues() {
        return setValues;
    }

    public void setSetValues(SetValuesConfig setValues) {
        this.setValues = setValues;
    }

    public String getValueGenerator() {
        return valueGenerator;
    }

    public void setValueGenerator(String valueGenerator) {
        this.valueGenerator = valueGenerator;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public Integer getExecutionIndex() {
        return executionIndex;
    }

    public void setExecutionIndex(Integer executionIndex) {
        this.executionIndex = executionIndex;
    }

}
