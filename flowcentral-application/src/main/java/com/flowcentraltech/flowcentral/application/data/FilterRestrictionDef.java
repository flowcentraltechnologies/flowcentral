/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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
 * Filter restriction definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FilterRestrictionDef {

    private FilterConditionType type;

    private String fieldName;

    private String paramA;

    private String paramB;

    private int depth;

    public FilterRestrictionDef(FilterConditionType type, String fieldName, String paramA, String paramB, int depth) {
        this.type = type;
        this.fieldName = fieldName;
        this.paramA = paramA;
        this.paramB = paramB;
        this.depth = depth;
    }

    public FilterRestrictionDef(FilterConditionType type, int depth) {
        this.type = type;
        this.depth = depth;
    }

    public FilterConditionType getType() {
        return type;
    }

    public boolean isSingleParam() {
        return type.isSingleParam();
    }

    public boolean isRange() {
        return type.isRange();
    }

    public boolean isFieldVal() {
        return type.isFieldVal();
    }

    public boolean isParameterVal() {
        return type.isParameterVal();
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getParamA() {
        return paramA;
    }

    public String getParamB() {
        return paramB;
    }

    public int getDepth() {
        return depth;
    }

    public boolean isCompound() {
        return type.isCompound();
    }

    @Override
    public String toString() {
        return "FilterRestrictionDef [type=" + type + ", fieldName=" + fieldName + ", paramA=" + paramA + ", paramB="
                + paramB + ", depth=" + depth + "]";
    }

}
