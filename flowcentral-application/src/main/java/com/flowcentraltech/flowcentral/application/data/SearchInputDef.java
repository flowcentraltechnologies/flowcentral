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

import com.tcdng.unify.core.criterion.FilterConditionType;

/**
 * Search input definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SearchInputDef {

    private FilterConditionType type;

    private String fieldName;

    private String widget;

    private String label;

    public SearchInputDef(FilterConditionType type, String fieldName, String widget, String label) {
        this.type = type;
        this.fieldName = fieldName;
        this.widget = widget;
        this.label = label;
    }

    public FilterConditionType getType() {
        return type;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getWidget() {
        return widget;
    }

    public String getLabel() {
        return label;
    }

}
