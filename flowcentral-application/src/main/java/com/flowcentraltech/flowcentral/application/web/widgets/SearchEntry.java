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
import com.flowcentraltech.flowcentral.application.data.WidgetTypeDef;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.common.business.SearchInputRestrictionGenerator;
import com.flowcentraltech.flowcentral.common.data.EntityFieldAttributes;
import com.flowcentraltech.flowcentral.common.input.AbstractInput;
import com.flowcentraltech.flowcentral.common.util.LingualDateUtils;
import com.flowcentraltech.flowcentral.configuration.constants.LingualDateType;
import com.flowcentraltech.flowcentral.configuration.constants.SearchConditionType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Search entry.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class SearchEntry implements EntityFieldAttributes {

    private final AppletUtilities au;

    private final EntityDef entityDef;

    private SearchConditionType conditionType;

    final private String label;

    private String fieldName;

    private String sessionAttributeName;

    private String generator;

    private String defVal;

    private boolean fixed;

    private final String preferredEvent;

    private AbstractInput<?> paramInput;

    public SearchEntry(AppletUtilities au, EntityDef entityDef, String label, String fieldName,
            SearchConditionType conditionType, String defVal, boolean fixed, String preferredEvent) {
        this.au = au;
        this.entityDef = entityDef;
        this.label = label;
        if (conditionType.isSession()) {
            this.sessionAttributeName = fieldName;
        } else {
            this.fieldName = fieldName;
        }

        this.conditionType = conditionType;
        this.preferredEvent = preferredEvent;

        this.defVal = defVal;
        this.fixed = fixed;
    }

    public SearchEntry(AppletUtilities au, EntityDef entityDef, String label, String generator, String defVal,
            boolean fixed, String preferredEvent) {
        this.au = au;
        this.entityDef = entityDef;
        this.label = label;
        this.generator = generator;
        this.defVal = defVal;
        this.fixed = fixed;
        this.preferredEvent = preferredEvent;
    }

    @Override
    public String getSuggestionType() throws UnifyException {
        return isGeneratorEntry()
                ? au.getComponent(SearchInputRestrictionGenerator.class, generator).getEntryAttributes()
                        .getSuggestionType()
                : null;
    }

    @Override
    public String getReferences() throws UnifyException {
        return isGeneratorEntry()
                ? au.getComponent(SearchInputRestrictionGenerator.class, generator).getEntryAttributes().getReferences()
                : null;
    }

    @Override
    public int getMinLen() throws UnifyException {
        return isGeneratorEntry()
                ? au.getComponent(SearchInputRestrictionGenerator.class, generator).getEntryAttributes().getMinLen()
                : 0;
    }

    @Override
    public int getMaxLen() throws UnifyException {
        return isGeneratorEntry()
                ? au.getComponent(SearchInputRestrictionGenerator.class, generator).getEntryAttributes().getMaxLen()
                : 0;
    }

    @Override
    public int getPrecision() throws UnifyException {
        return isGeneratorEntry()
                ? au.getComponent(SearchInputRestrictionGenerator.class, generator).getEntryAttributes().getPrecision()
                : 0;
    }

    @Override
    public int getScale() throws UnifyException {
        return isGeneratorEntry()
                ? au.getComponent(SearchInputRestrictionGenerator.class, generator).getEntryAttributes().getScale()
                : 0;
    }

    @Override
    public boolean isBranchScoping() throws UnifyException {
        return isGeneratorEntry()
                ? au.getComponent(SearchInputRestrictionGenerator.class, generator).getEntryAttributes()
                        .isBranchScoping()
                : false;
    }

    @Override
    public boolean isTrim() throws UnifyException {
        return isGeneratorEntry()
                ? au.getComponent(SearchInputRestrictionGenerator.class, generator).getEntryAttributes().isTrim()
                : false;
    }

    @Override
    public boolean isAllowNegative() throws UnifyException {
        return isGeneratorEntry()
                ? au.getComponent(SearchInputRestrictionGenerator.class, generator).getEntryAttributes()
                        .isAllowNegative()
                : false;
    }

    public String getLabel() {
        return label;
    }

    public String getDefVal() {
        return defVal;
    }
    
    public boolean isWithDefVal() {
        return !StringUtils.isBlank(defVal);
    }

    public boolean isFixed() {
        return fixed;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getSessionAttributeName() {
        return sessionAttributeName;
    }

    public String getGenerator() {
        return generator;
    }

    public SearchConditionType getConditionType() {
        return conditionType;
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public EntityFieldDef getEntityFieldDef() {
        return entityDef.getFieldDef(fieldName);
    }

    public AbstractInput<?> getParamInput() {
        return paramInput;
    }

    public boolean isWithParamInput() {
        return paramInput != null;
    }

    public String getPreferredEvent() {
        return preferredEvent;
    }

    public boolean isPseudoFieldEntry() throws UnifyException {
        return !StringUtils.isBlank(fieldName) && !entityDef.isField(fieldName);
    }

    public boolean isFieldEntry() throws UnifyException {
        return !StringUtils.isBlank(fieldName) && entityDef.isField(fieldName);
    }

    public boolean isSessionEntry() throws UnifyException {
        return !StringUtils.isBlank(sessionAttributeName);
    }

    public boolean isGeneratorEntry() {
        return !StringUtils.isBlank(generator);
    }

    public void normalize() throws UnifyException {
        normalize(null);
    }

    @SuppressWarnings("unchecked")
    public void normalize(WidgetTypeDef widgetTypeDef) throws UnifyException {
        EntityFieldDef entityFieldDef = isFieldEntry()
                ? (getEntityFieldDef().isWithResolvedTypeFieldDef() ? getEntityFieldDef().getResolvedTypeFieldDef()
                        : getEntityFieldDef())
                : null;
        if (widgetTypeDef != null) {
            if (isFieldEntry()) {
                paramInput = InputWidgetUtils.newInput(widgetTypeDef, entityFieldDef);
            } else {
                paramInput = InputWidgetUtils.newInput(widgetTypeDef, this);
            }
        } else {
            paramInput = evalInput(entityFieldDef);
        }

        if (defVal != null && isFieldEntry()) {
            entityFieldDef = entityDef.getFieldDef(fieldName);
            entityFieldDef = entityFieldDef.isWithResolvedTypeFieldDef() ? entityFieldDef.getResolvedTypeFieldDef()
                    : entityFieldDef;
            if (entityFieldDef.isDate() || entityFieldDef.isTimestamp()) {
                ((AbstractInput<Object>) getParamInput())
                        .setValue(LingualDateUtils.getDateFromNow(au.getNow(), LingualDateType.fromCode(defVal)));
            } else {
                ((AbstractInput<Object>) getParamInput()).setStringValue(defVal);
            }
        }
    }

    private AbstractInput<?> evalInput(EntityFieldDef entityFieldDef) throws UnifyException {
        return InputWidgetUtils.newInput(au, entityFieldDef, false, true);
    }

}
