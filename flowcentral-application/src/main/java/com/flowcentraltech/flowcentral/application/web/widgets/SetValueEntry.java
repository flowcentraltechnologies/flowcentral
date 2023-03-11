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
package com.flowcentraltech.flowcentral.application.web.widgets;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.common.input.AbstractInput;
import com.flowcentraltech.flowcentral.common.input.StringInput;
import com.flowcentraltech.flowcentral.configuration.constants.SetValueType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Set value entry.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SetValueEntry {

    private EntityDef entityDef;

    private SetValueType type;

    private String fieldName;

    private String paramField;

    private AbstractInput<?> paramInput;

    private boolean fieldChange;

    private boolean typeChange;

    private boolean editable;

    public SetValueEntry(EntityDef entityDef, boolean editable) {
        this.entityDef = entityDef;
        this.editable = editable;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldChange = !DataUtils.equals(this.fieldName, fieldName);
        this.fieldName = fieldName;
    }

    public SetValueType getType() {
        return type;
    }

    public void setType(SetValueType type) {
        this.typeChange = !DataUtils.equals(this.type, type);
        this.type = type;
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public EntityFieldDef getEntityFieldDef(String fieldName) {
        return entityDef.getFieldDef(fieldName);
    }

    public String getParamField() {
        return paramField;
    }

    public void setParamField(String paramField) {
        this.paramField = paramField;
    }

    public AbstractInput<?> getParamInput() {
        return paramInput;
    }

    public boolean isWithFieldName() {
        return fieldName != null;
    }

    public boolean isWithParamInput() {
        return paramInput != null;
    }

    public boolean isEditable() {
        return editable;
    }

    public void normalize() throws UnifyException {
        if (fieldName == null) {
            type = null;
            paramField = null;
            paramInput = null;
        } else {
            if (type == null || type.isNoParam()) {
                paramField = null;
                paramInput = null;
            } else {
                EntityFieldDef entityFieldDef = entityDef.getFieldDef(fieldName);
                paramInput = evalInput(entityFieldDef, paramInput);
                paramField = null;
            }
        }

        fieldChange = false;
        typeChange = false;
    }

    private AbstractInput<?> evalInput(EntityFieldDef entityFieldDef, AbstractInput<?> currentIn)
            throws UnifyException {
        if (currentIn == null) {
            switch (type) {
                case PROCESS_VARIABLE:
                    return new StringInput("!ui-select blankOption:$s{} list:processvariablelist",
                            "!ui-listlabel list:processvariablelist");
                case GENERATOR:
                    return new StringInput(
                            "!ui-select blankOption:$s{} list:fieldsetvaluegenlist listParams:$s{entityDef}",
                            "!ui-label");
                case IMMEDIATE_FIELD:
                    return new StringInput(
                            "!ui-select blankOption:$s{} list:entityfielddeflist listParams:$s{entityDef}",
                            "!ui-label");
                case IMMEDIATE:
                case IMMEDIATE_LINGUAL:
                default:
                    return InputWidgetUtils.newInput(entityFieldDef, type.isLingual(), false);
            }
        }

        if (fieldChange || typeChange) {
            switch (type) {
                case PROCESS_VARIABLE:
                    return new StringInput("!ui-select blankOption:$s{} list:processvariablelist",
                            "!ui-listlabel list:processvariablelist");
                case GENERATOR:
                    return new StringInput(
                            "!ui-select blankOption:$s{} list:fieldsetvaluegenlist listParams:$s{entityDef}",
                            "!ui-label");
                case IMMEDIATE_FIELD:
                    return new StringInput(
                            "!ui-select blankOption:$s{} list:entityfielddeflist listParams:$s{entityDef}",
                            "!ui-label");
                case IMMEDIATE:
                case IMMEDIATE_LINGUAL:
                default:
                    AbstractInput<?> newIn = InputWidgetUtils.newInput(entityFieldDef, type.isLingual(), false);
                    if (!newIn.compatible(currentIn)) {
                        return newIn;
                    }
            }
        }

        return currentIn;
    }

    @Override
    public String toString() {
        return "SetValueEntry [entityDef=" + entityDef + ", type=" + type + ", fieldName=" + fieldName + ", paramField="
                + paramField + ", paramInput=" + paramInput + ", fieldChange=" + fieldChange + ", typeChange="
                + typeChange + ", editable=" + editable + "]";
    }
}
