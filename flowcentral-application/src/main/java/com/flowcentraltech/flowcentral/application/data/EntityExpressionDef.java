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

import java.util.List;

import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.common.util.StringToken;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Entity expression definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityExpressionDef implements Listable {

    private String name;

    private String description;

    private String expression;

    private List<StringToken> expressionTokenList;

    public EntityExpressionDef(String name, String description, String expression) {
        this.name = name;
        this.description = description;
        this.expression = expression;
        this.expressionTokenList = StringUtils.breakdownParameterizedString(expression);
    }

    @Override
    public String getListDescription() {
        return description;
    }

    @Override
    public String getListKey() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getExpression() {
        return expression;
    }

    public List<StringToken> getExpressionTokenList() {
        return expressionTokenList;
    }

}
