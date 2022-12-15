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
package com.flowcentraltech.flowcentral.application.web.widgets;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.configuration.constants.SearchConditionType;
import com.tcdng.unify.core.UnifyException;

/**
 * Search input entry.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SearchInputEntry {

    private EntityDef entityDef;

    private SearchConditionType condition;

    private String fieldName;

    private String widget;

    private String label;

    private boolean editable;

    public SearchInputEntry(EntityDef entityDef, boolean editable) {
        this.entityDef = entityDef;
        this.editable = editable;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public SearchConditionType getCondition() {
        return condition;
    }

    public void setCondition(SearchConditionType condition) {
        this.condition = condition;
    }

    public String getWidget() {
        return widget;
    }

    public void setWidget(String widget) {
        this.widget = widget;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public EntityFieldDef getEntityFieldDef(String fieldName) {
        return entityDef != null ? entityDef.getFieldDef(fieldName) : null;
    }

    public boolean isWithLabel() {
        return label != null;
    }

    public boolean isWithWidget() {
        return widget != null;
    }

    public boolean isWithFieldName() {
        return fieldName != null;
    }

    public boolean isEditable() {
        return editable;
    }

    public void normalize() throws UnifyException {
        if (label == null) {
            fieldName = null;
        }
        
        if (fieldName == null) {
            widget = null;
        }
        
        if (widget == null) {
            condition = null;
        }
    }

}
