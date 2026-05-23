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

import com.flowcentraltech.flowcentral.configuration.constants.SearchConditionType;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Search input definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class SearchInputDef {

    private SearchConditionType type;

    private String fieldName;

    private String widget;

    private String label;

    private String defVal;

    private boolean fixed;

    public SearchInputDef(SearchConditionType type, String fieldName, String widget, String label, String defVal,
            boolean fixed) {
        this.type = type;
        this.fieldName = fieldName;
        this.widget = widget;
        this.label = label;
        this.defVal = defVal;
        this.fixed = fixed;
    }

    public SearchConditionType getType() {
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

    public String getDefVal() {
        return defVal;
    }

    public boolean isFixed() {
        return fixed;
    }

    @Override
    public String toString() {
        return StringUtils.toXmlString(this);
    }

}
