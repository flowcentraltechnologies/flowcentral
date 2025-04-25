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
package com.flowcentraltech.flowcentral.application.data;

import java.util.Date;
import java.util.Map;

import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.filter.ObjectFilter;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Form widget rules policy definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormWidgetRulesPolicyDef implements Listable {

    private FilterDef onCondition;

    private WidgetRulesDef widgetRulesDef;

    private Map<String, String> ruleEditors;

    private String name;

    private String description;

    public FormWidgetRulesPolicyDef(String name, String description, FilterDef onCondition,
            WidgetRulesDef widgetRulesDef, Map<String, String> ruleEditors) {
        this.name = name;
        this.description = description;
        this.onCondition = onCondition;
        this.widgetRulesDef = widgetRulesDef;
        this.ruleEditors = DataUtils.unmodifiableMap(ruleEditors);
    }

    @Override
    public String getListDescription() {
        return description;
    }

    @Override
    public String getListKey() {
        return name;
    }

    public boolean match(FormDef formDef, Object inst, Date now) throws UnifyException {
        return match(formDef, new BeanValueStore(inst).getReader(), now);
    }

    public boolean match(FormDef formDef, ValueStore valueStore, Date now) throws UnifyException {
        return match(formDef, valueStore.getReader(), now);
    }

    public boolean match(FormDef formDef, ValueStoreReader reader, Date now) throws UnifyException {
        ObjectFilter objectFilter = onCondition != null
                ? onCondition.getObjectFilter(formDef.getEntityDef(), reader, now)
                : null;
        return objectFilter == null || objectFilter.matchReader(reader);
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

    public boolean isWithRuleEditor(String fieldName) {
        return ruleEditors.containsKey(fieldName);
    }

    public String getRuleEditor(String fieldName) {
        return ruleEditors.get(fieldName);
    }

    public Map<String, String> getRuleEditors() {
        return ruleEditors;
    }
}
