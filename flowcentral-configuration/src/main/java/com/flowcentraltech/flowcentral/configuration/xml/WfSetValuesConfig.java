/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.configuration.constants.WorkflowSetValuesType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.WorkflowSetValuesTypeXmlAdapter;

/**
 * Workflow set values configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WfSetValuesConfig extends BaseNameConfig {

    private WorkflowSetValuesType type;
    
    private FilterConfig onCondition;

    private SetValuesConfig setValues;

    public WorkflowSetValuesType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(WorkflowSetValuesTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setType(WorkflowSetValuesType type) {
        this.type = type;
    }

    public FilterConfig getOnCondition() {
        return onCondition;
    }

    @XmlElement(required = true)
    public void setOnCondition(FilterConfig onCondition) {
        this.onCondition = onCondition;
    }

    public SetValuesConfig getSetValues() {
        return setValues;
    }

    @XmlElement(name = "setValue")
    public void setSetValues(SetValuesConfig setValues) {
        this.setValues = setValues;
    }

}
