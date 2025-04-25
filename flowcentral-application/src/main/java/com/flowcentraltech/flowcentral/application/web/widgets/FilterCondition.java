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
package com.flowcentraltech.flowcentral.application.web.widgets;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.LabelSuggestionDef;
import com.flowcentraltech.flowcentral.application.data.WidgetTypeDef;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.common.input.AbstractInput;
import com.flowcentraltech.flowcentral.configuration.constants.SysParamType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.FilterConditionListType;
import com.tcdng.unify.core.criterion.FilterConditionType;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Filter condition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FilterCondition {

    private final AppletUtilities au;

    private final String sessionParamWidget;

    private EntityDef entityDef;

    private LabelSuggestionDef labelSuggestionDef;

    private FilterConditionType type;

    private FilterConditionListType listType;

    private String typeSelector;

    private String fieldName;

    private String paramFieldA;

    private String paramFieldB;

    private AbstractInput<?> paramInputA;

    private AbstractInput<?> paramInputB;

    private Long ownerInstId;

    private int depth;

    private boolean typeChange;

    private boolean fieldChange;

    private boolean editable;

    public FilterCondition(AppletUtilities au, EntityDef entityDef, LabelSuggestionDef labelSuggestionDef,
            String sessionParamWidget, Long ownerInstId, FilterConditionType type, FilterConditionListType listType,
            int depth, boolean editable) {
        this.au = au;
        this.entityDef = entityDef;
        this.labelSuggestionDef = labelSuggestionDef;
        this.sessionParamWidget = sessionParamWidget;
        this.ownerInstId = ownerInstId;
        this.type = type;
        this.listType = listType;
        this.depth = depth;
        this.editable = editable;
    }

    public FilterConditionType getType() {
        return type;
    }

    public void setType(FilterConditionType type) {
        this.typeChange = !DataUtils.equals(this.type, type);
        this.type = type;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldChange = !DataUtils.equals(this.fieldName, fieldName);
        this.fieldName = fieldName;
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public LabelSuggestionDef getLabelSuggestionDef() {
        return labelSuggestionDef;
    }

    public Long getOwnerInstId() {
        return ownerInstId;
    }

    public EntityFieldDef getEntityFieldDef() {
        return entityDef.getFieldDef(fieldName);
    }

    public String getTypeSelector() {
        return typeSelector;
    }

    public String getParamFieldA() {
        return paramFieldA;
    }

    public void setParamFieldA(String paramFieldA) {
        this.paramFieldA = paramFieldA;
    }

    public String getParamFieldB() {
        return paramFieldB;
    }

    public void setParamFieldB(String paramFieldB) {
        this.paramFieldB = paramFieldB;
    }

    public AbstractInput<?> getParamInputA() {
        return paramInputA;
    }

    public AbstractInput<?> getParamInputB() {
        return paramInputB;
    }

    public boolean isWithParamInputA() {
        return paramInputA != null;
    }

    public boolean isWithParamInputB() {
        return paramInputB != null;
    }

    public int getDepth() {
        return depth;
    }

    public boolean isEditable() {
        return editable;
    }

    public boolean isValidState() {
        return type != null;
    }

    public void swapLogic() {
        if (FilterConditionType.AND.equals(type)) {
            type = FilterConditionType.OR;
        } else if (FilterConditionType.OR.equals(type)) {
            type = FilterConditionType.AND;
        }
    }

    public void normalize() throws UnifyException {
        if (fieldName == null) {
            typeSelector = null;
            paramFieldA = null;
            paramFieldB = null;
            paramInputA = null;
            paramInputB = null;
        } else {
            SysParamType sysParamType = SysParamType.isSysParam(fieldName) ? SysParamType.fromEncoded(fieldName) : null;
            EntityFieldDef entityFieldDef = sysParamType != null ? InputWidgetUtils.getEntityFieldDef(sysParamType)
                    : entityDef.getFieldDef(fieldName);

            typeSelector = InputWidgetUtils.getFilterConditionTypeSelectDescriptior(entityFieldDef, listType);
            if (type != null) {
                if (!InputWidgetUtils.isSupportedFilterConditionType(entityFieldDef, type, listType)) {
                    type = null;
                }
            }

            if (type == null) {
                paramFieldA = null;
                paramFieldB = null;
                paramInputA = null;
                paramInputB = null;
            } else {
                if (type.isFieldVal() || type.isParameterVal()) {
                    paramInputA = null;
                    paramInputB = null;
                } else if (type.isSessionParameterVal()) {
                    final WidgetTypeDef sessionParamWidgetTypeDef = au.getWidgetTypeDef(sessionParamWidget);
                    paramInputA = evalInput(sessionParamWidgetTypeDef, entityFieldDef, paramInputA);
                } else {
                    if (type.isSingleParam()) {
                        paramInputA = evalInput(entityFieldDef, paramInputA);
                        paramInputB = null;
                    } else if (type.isAmongst()) {
                        paramInputA = evalMultiInput(entityFieldDef, paramInputA);
                        paramInputB = null;
                    } else if (type.isRange()) {
                        paramInputA = evalInput(entityFieldDef, paramInputA);
                        paramInputB = evalInput(entityFieldDef, paramInputB);
                    } else {
                        paramInputA = null;
                        paramInputB = null;
                    }

                    paramFieldA = null;
                    paramFieldB = null;
                }
            }
        }

        typeChange = false;
        fieldChange = false;
    }

    private AbstractInput<?> evalInput(WidgetTypeDef widgetTypeDef, EntityFieldDef entityFieldDef,
            AbstractInput<?> currentIn) throws UnifyException {
        if (currentIn == null) {
            return InputWidgetUtils.newInput(widgetTypeDef, entityFieldDef);
        }

        if (fieldChange || typeChange) {
            AbstractInput<?> newIn = InputWidgetUtils.newInput(widgetTypeDef, entityFieldDef);
            if (!newIn.compatible(currentIn)) {
                return newIn;
            }
        }

        return currentIn;
    }

    private AbstractInput<?> evalInput(EntityFieldDef entityFieldDef, AbstractInput<?> currentIn)
            throws UnifyException {
        final boolean search = false;
        if (currentIn == null) {
            return InputWidgetUtils.newInput(au, entityFieldDef, type.isLingual(), search);
        }

        if (fieldChange || typeChange) {
            AbstractInput<?> newIn = InputWidgetUtils.newInput(au, entityFieldDef, type.isLingual(), search);
            if (!newIn.compatible(currentIn)) {
                return newIn;
            }
        }

        return currentIn;
    }

    private AbstractInput<?> evalMultiInput(EntityFieldDef entityFieldDef, AbstractInput<?> currentIn)
            throws UnifyException {
        if (currentIn == null) {
            return InputWidgetUtils.newMultiInput(entityFieldDef);
        }

        if (fieldChange || typeChange) {
            AbstractInput<?> newIn = InputWidgetUtils.newMultiInput(entityFieldDef);
            if (!newIn.compatible(currentIn)) {
                return newIn;
            }
        }

        return currentIn;
    }

}
