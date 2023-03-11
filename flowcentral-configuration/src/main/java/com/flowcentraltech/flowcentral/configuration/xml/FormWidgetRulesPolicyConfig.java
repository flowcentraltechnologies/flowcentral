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

import javax.xml.bind.annotation.XmlElement;

/**
 * Form widget rules policy configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormWidgetRulesPolicyConfig extends BaseNameConfig {
    
    private FilterConfig onCondition;

    private WidgetRulesConfig widgetRules;

    public FilterConfig getOnCondition() {
        return onCondition;
    }

    @XmlElement(required = true)
    public void setOnCondition(FilterConfig onCondition) {
        this.onCondition = onCondition;
    }

    public WidgetRulesConfig getWidgetRules() {
        return widgetRules;
    }

    @XmlElement(name = "widgetRules")
    public void setWidgetRules(WidgetRulesConfig widgetRules) {
        this.widgetRules = widgetRules;
    }

}
