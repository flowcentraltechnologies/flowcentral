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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Form widget rules policy configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class FormWidgetRulesPolicyConfig extends BaseNameConfig {
    
    @JacksonXmlProperty(isAttribute = true)
    private Integer executionIndex;
    
    @JacksonXmlProperty()
    private FilterConfig onCondition;

    @JacksonXmlProperty(localName = "widgetRules")
    private WidgetRulesConfig widgetRules;

    public Integer getExecutionIndex() {
        return executionIndex;
    }

    public void setExecutionIndex(Integer executionIndex) {
        this.executionIndex = executionIndex;
    }

    public FilterConfig getOnCondition() {
        return onCondition;
    }

    public void setOnCondition(FilterConfig onCondition) {
        this.onCondition = onCondition;
    }

    public WidgetRulesConfig getWidgetRules() {
        return widgetRules;
    }

    public void setWidgetRules(WidgetRulesConfig widgetRules) {
        this.widgetRules = widgetRules;
    }

}
