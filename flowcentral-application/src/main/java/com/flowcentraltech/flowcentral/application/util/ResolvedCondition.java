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

package com.flowcentraltech.flowcentral.application.util;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.CriteriaBuilder;
import com.tcdng.unify.core.criterion.FilterConditionType;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Resolve condition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ResolvedCondition {

    private final String fieldName;

    private final FilterConditionType type;

    private final Object paramA;

    private final Object paramB;

    public ResolvedCondition(String fieldName, FilterConditionType type, Object paramA, Object paramB) {
        this.fieldName = fieldName;
        this.type = type;
        this.paramA = paramA;
        this.paramB = paramB;
    }

    public FilterConditionType getType() {
        return type;
    }

    public Object getParamA() {
        return paramA;
    }

    public Object getParamB() {
        return paramB;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void addSimpleCriteria(CriteriaBuilder cb) throws UnifyException {
        type.addSimpleCriteria(cb, fieldName, paramA, paramB);
    }
    
    public Restriction createSimpleCriteria() throws UnifyException {
        return type.createSimpleCriteria(fieldName, paramA, paramB);
    }

    @Override
    public String toString() {
        return StringUtils.toXmlString(this);
    }
}
