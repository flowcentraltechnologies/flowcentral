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
package com.flowcentraltech.flowcentral.application.web.widgets;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.common.input.AbstractInput;
import com.flowcentraltech.flowcentral.configuration.constants.SearchConditionType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Search input entry.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class SearchInputEntry {

    private final AppletUtilities au;

    private EntityDef entityDef;

    private SearchConditionType condition;

    private String fieldName;

    private String widget;

    private String label;

    private String defVal;

    private AbstractInput<?> defValInput;

    private boolean fixed;

    private boolean fieldChange;

    private boolean editable;

    public SearchInputEntry(AppletUtilities au, EntityDef entityDef, boolean editable) {
        this.au = au;
        this.entityDef = entityDef;
        this.editable = editable;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        fieldChange = !DataUtils.equals(this.fieldName, fieldName);
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

    public AbstractInput<?> getDefValInput() {
        return defValInput;
    }

    public boolean isWithDefValInput() {
        return defValInput != null;
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

    public String getDefVal() {
        return defVal;
    }

    public void setDefVal(String defVal) {
        this.defVal = defVal;
    }

    public boolean isWithDefVal() {
        return defVal != null;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public boolean isFieldInput() {
        return fieldName != null && fieldName.startsWith("f:");
    }

    public boolean isValidEntry() {
        return fieldName != null && label != null && widget != null && (!isFieldInput() || condition != null);
    }

    public void normalize(EntityDef entityDef) throws UnifyException {
        if (fieldName == null) {
            label = null;
        } else {
            if (label == null && fieldName.startsWith("f:")) {
                label = entityDef.getFieldDef(fieldName.substring("f:".length())).getFieldLabel();
            }
        }

        if (label == null) {
            widget = null;
        }

        if (widget == null) {
            defValInput = null;
            condition = null;
            defVal = null;
            fixed = false;
        } else {
            defValInput = evalInput(
                    entityDef.getFieldDef(fieldName.startsWith("f:") ? fieldName.substring("f:".length()) : fieldName),
                    defValInput);
        }
        
        fieldChange = false;
    }

    private AbstractInput<?> evalInput(EntityFieldDef entityFieldDef, AbstractInput<?> currentIn)
            throws UnifyException {
        final boolean search = false;
        if (currentIn == null) {
            return InputWidgetUtils.newInput(au, entityFieldDef, condition.filterType().isLingual(), search);
        }

        if (fieldChange) {
            AbstractInput<?> newIn = InputWidgetUtils.newInput(au, entityFieldDef, condition.filterType().isLingual(), search);
            if (!newIn.compatible(currentIn)) {
                return newIn;
            }
        }

        return currentIn;
    }

    @Override
    public String toString() {
        return StringUtils.toXmlString(this);
    }

}
