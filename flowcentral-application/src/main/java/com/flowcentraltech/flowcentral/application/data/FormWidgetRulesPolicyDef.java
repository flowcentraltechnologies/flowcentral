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
package com.flowcentraltech.flowcentral.application.data;

import com.tcdng.unify.core.data.Listable;

/**
 * Form widget rules policy definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormWidgetRulesPolicyDef implements Listable {

    private FilterDef onCondition;

    private WidgetRulesDef widgetRulesDef;

    private String name;

    private String description;

    public FormWidgetRulesPolicyDef(String name, String description, FilterDef onCondition,
            WidgetRulesDef widgetRulesDef) {
        this.name = name;
        this.description = description;
        this.onCondition = onCondition;
        this.widgetRulesDef = widgetRulesDef;
    }

    @Override
    public String getListDescription() {
        return description;
    }

    @Override
    public String getListKey() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public FilterDef getOnCondition() {
        return onCondition;
    }

    public boolean isWithCondition() {
        return onCondition != null;
    }

    public WidgetRulesDef getWidgetRulesDef() {
        return widgetRulesDef;
    }

    public boolean isWidgetRules() {
        return widgetRulesDef != null;
    }
}
