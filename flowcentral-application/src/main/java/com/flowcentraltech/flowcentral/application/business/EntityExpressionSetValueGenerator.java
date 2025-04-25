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

package com.flowcentraltech.flowcentral.application.business;

import java.math.BigDecimal;

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityExpressionDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ParameterizedStringGenerator;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.script.ScriptingEngine;

/**
 * Entity expression set-value generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(ApplicationModuleNameConstants.ENTITYEXPRESSION_SETVALUE_GENERATOR)
public class EntityExpressionSetValueGenerator extends AbstractFieldSetValueGenerator {

    @Configurable
    private ScriptingEngine scriptingEngine;

    @Override
    public Object generate(EntityDef entityDef, ValueStore valueStore, String rule) throws UnifyException {
        logDebug("Generating field value from entity [{0}], instance ID [{1}] and expression [{2}]...",
                entityDef.getLongName(), valueStore.retrieve("id"), rule);
        final EntityExpressionDef entityExpressionDef = entityDef.getExpressionDef(rule);
        final ParameterizedStringGenerator pgen = au().getStringGenerator(valueStore.getReader(),
                entityExpressionDef.getExpressionTokenList());
        final String valScript = pgen.generate();
        return scriptingEngine.evaluate(BigDecimal.class, valScript);
    }

}
