/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowSetValuesType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.WorkflowSetValuesTypeXmlAdapter;

/**
 * Workflow set values configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class WfSetValuesConfig extends BaseNameConfig {

    @JsonSerialize(using = WorkflowSetValuesTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = WorkflowSetValuesTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private WorkflowSetValuesType type;
    
    @JacksonXmlProperty()
    private FilterConfig onCondition;

    @JacksonXmlProperty
    private SetValuesConfig setValues;

    @JacksonXmlProperty(isAttribute = true)
    private String valueGenerator;

    public WorkflowSetValuesType getType() {
        return type;
    }

    public void setType(WorkflowSetValuesType type) {
        this.type = type;
    }

    public FilterConfig getOnCondition() {
        return onCondition;
    }

    public void setOnCondition(FilterConfig onCondition) {
        this.onCondition = onCondition;
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

}
