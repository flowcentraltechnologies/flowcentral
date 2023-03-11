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

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.WidgetTypeDef;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.common.business.SearchInputRestrictionGenerator;
import com.flowcentraltech.flowcentral.common.data.EntityFieldAttributes;
import com.flowcentraltech.flowcentral.common.input.AbstractInput;
import com.flowcentraltech.flowcentral.configuration.constants.SearchConditionType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Search entry.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SearchEntry implements EntityFieldAttributes {

    private final AppletUtilities au;

    private EntityDef entityDef;

    private SearchConditionType conditionType;

    private String label;

    private String fieldName;

    private String generator;

    private String paramField;

    private AbstractInput<?> paramInput;

    public SearchEntry(AppletUtilities au, EntityDef entityDef, String label, String fieldName,
            SearchConditionType conditionType) {
        this.au = au;
        this.entityDef = entityDef;
        this.label = label;
        this.fieldName = fieldName;
        this.conditionType = conditionType;
    }

    public SearchEntry(AppletUtilities au, EntityDef entityDef, String label, String generator) {
        this.au = au;
        this.entityDef = entityDef;
        this.label = label;
        this.generator = generator;
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
    public boolean isAllowNegative() throws UnifyException {
        return isGeneratorEntry()
                ? au.getComponent(SearchInputRestrictionGenerator.class, generator).getEntryAttributes()
                        .isAllowNegative()
                : false;
    }

    public String getLabel() {
        return label;
    }

    public String getFieldName() {
        return fieldName;
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

    public String getParamField() {
        return paramField;
    }

    public AbstractInput<?> getParamInput() {
        return paramInput;
    }

    public boolean isWithParamInput() {
        return paramInput != null;
    }

    public boolean isFieldEntry() {
        return !StringUtils.isBlank(fieldName);
    }

    public boolean isGeneratorEntry() {
        return !StringUtils.isBlank(generator);
    }

    public void normalize() throws UnifyException {
        normalize(null);
    }

    public void normalize(WidgetTypeDef widgetTypeDef) throws UnifyException {
        if (widgetTypeDef != null) {
            EntityFieldAttributes efa = isFieldEntry() ? getEntityFieldDef() : this;
            paramInput = InputWidgetUtils.newInput(widgetTypeDef, efa);
        } else {
            paramInput = evalInput(getEntityFieldDef());
        }
    }

    private AbstractInput<?> evalInput(EntityFieldDef entityFieldDef) throws UnifyException {
        return InputWidgetUtils.newInput(entityFieldDef, false, true);
    }

}
