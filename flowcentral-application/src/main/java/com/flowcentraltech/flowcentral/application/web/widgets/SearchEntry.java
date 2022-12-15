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
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
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
public class SearchEntry {

    private EntityDef entityDef;

    private SearchConditionType conditionType;

    private String label;

    private String fieldName;

    private String paramField;

    private AbstractInput<?> paramInput;

    public SearchEntry(EntityDef entityDef, String label, String fieldName, SearchConditionType conditionType) {
        this.entityDef = entityDef;
        this.label = label;
        this.fieldName = fieldName;
        this.conditionType = conditionType;
    }

    public String getLabel() {
        return label;
    }

    public String getFieldName() {
        return fieldName;
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

    public boolean isField() {
        return !StringUtils.isBlank(fieldName);
    }
    
    public void normalize() throws UnifyException {
        normalize(null);
    }
    
    public void normalize(String widget) throws UnifyException {
        // TODO Handle when widget is not null
        EntityFieldDef entityFieldDef = entityDef.getFieldDef(fieldName);
        paramInput = evalInput(entityFieldDef);
    }

    private AbstractInput<?> evalInput(EntityFieldDef entityFieldDef) throws UnifyException {
        return InputWidgetUtils.newInput(entityFieldDef, false, true);
    }

}
