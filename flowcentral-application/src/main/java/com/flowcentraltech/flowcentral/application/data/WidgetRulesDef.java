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

package com.flowcentraltech.flowcentral.application.data;

import java.util.ArrayList;
import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Application widget rules definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class WidgetRulesDef {

    private List<WidgetRuleEntryDef> widgetRuleEntryList;

    private String name;

    private String description;

    private WidgetRulesDef(List<WidgetRuleEntryDef> widgetRuleEntryList, String name, String description) {
        this.widgetRuleEntryList = widgetRuleEntryList;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<WidgetRuleEntryDef> getWidgetRuleEntryList() {
        return widgetRuleEntryList;
    }

    public boolean isBlank() {
        return widgetRuleEntryList == null || widgetRuleEntryList.isEmpty();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private String name;

        private String description;

        private List<WidgetRuleEntryDef> widgetRuleEntryList;

        public Builder() {
            widgetRuleEntryList = new ArrayList<WidgetRuleEntryDef>();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder addWidgetRuleEntryDef(String fieldName, String widget) {
            widgetRuleEntryList.add(new WidgetRuleEntryDef(fieldName, widget));
            return this;
        }

        public WidgetRulesDef build() throws UnifyException {
            return new WidgetRulesDef(DataUtils.unmodifiableList(widgetRuleEntryList), name, description);
        }
    }
}
