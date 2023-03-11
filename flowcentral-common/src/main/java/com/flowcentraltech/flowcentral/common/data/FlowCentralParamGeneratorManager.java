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

package com.flowcentraltech.flowcentral.common.data;

import com.flowcentraltech.flowcentral.common.constants.CommonGeneratorComponentConstants;
import com.flowcentraltech.flowcentral.common.constants.CommonModuleNameConstants;
import com.tcdng.unify.common.util.ParamToken;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.AbstractParamGeneratorManager;
import com.tcdng.unify.core.data.ParamGenerator;

/**
 * FlowCentral parameter generator manager.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(CommonModuleNameConstants.PARAMGENERATORMANAGER)
public class FlowCentralParamGeneratorManager extends AbstractParamGeneratorManager {

    @Configurable(CommonModuleNameConstants.SYS_PARAM_GENERATOR)
    private ParamGenerator sysParamGenerator;

    @Configurable(CommonModuleNameConstants.PROCESS_VARIABLE_GENERATOR)
    private ParamGenerator processVariableGenerator;

    public final void setSysParamGenerator(ParamGenerator sysParamGenerator) {
        this.sysParamGenerator = sysParamGenerator;
    }

    public final void setProcessVariableGenerator(ParamGenerator processVariableGenerator) {
        this.processVariableGenerator = processVariableGenerator;
    }

    @Override
    protected ParamGenerator resolveParamGenerator(ParamToken token) throws UnifyException {
        if (CommonGeneratorComponentConstants.SYSTEM_PARAMETER.equals(token.getComponent())) {
            return sysParamGenerator;
        }

        if (CommonGeneratorComponentConstants.PROCESS_VARIABLE.equals(token.getComponent())) {
            return processVariableGenerator;
        }
        
        return null;
    }

}
