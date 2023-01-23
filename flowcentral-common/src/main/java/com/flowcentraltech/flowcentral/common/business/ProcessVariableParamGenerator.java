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

package com.flowcentraltech.flowcentral.common.business;

import java.util.Map;

import com.flowcentraltech.flowcentral.common.constants.CommonModuleNameConstants;
import com.flowcentraltech.flowcentral.common.constants.CommonTempValueNameConstants;
import com.tcdng.unify.common.util.ParamToken;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.AbstractParamGenerator;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Process variable generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(CommonModuleNameConstants.PROCESS_VARIABLE_GENERATOR)
public class ProcessVariableParamGenerator extends AbstractParamGenerator {

    @SuppressWarnings("unchecked")
    @Override
    public Object generate(ValueStoreReader itemReader, ValueStoreReader parentReader, ParamToken token)
            throws UnifyException {
        Map<String, Object> variables = (Map<String, Object>) itemReader
                .getTempValue(CommonTempValueNameConstants.PROCESS_VARIABLES);
        return variables != null ? variables.get(token.getParam()) : null;
    }

}