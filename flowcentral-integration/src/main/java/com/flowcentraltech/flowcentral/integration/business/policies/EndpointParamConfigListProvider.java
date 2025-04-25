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
package com.flowcentraltech.flowcentral.integration.business.policies;

import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.common.business.policies.AbstractParamConfigListProvider;
import com.flowcentraltech.flowcentral.integration.business.IntegrationModuleService;
import com.flowcentraltech.flowcentral.integration.constants.IntegrationModuleNameConstants;
import com.flowcentraltech.flowcentral.integration.entities.EndpointConfig;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ParamConfig;
import com.tcdng.unify.core.util.StringUtils;

/**
 * End-point parameter configuration list provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(IntegrationModuleNameConstants.ENDPOINT_PARAMCONFIGPROVIDER)
public class EndpointParamConfigListProvider extends AbstractParamConfigListProvider {

    @Configurable
    private IntegrationModuleService integrationModuleService;

    public void setIntegrationModuleService(IntegrationModuleService integrationModuleService) {
        this.integrationModuleService = integrationModuleService;
    }

    @Override
    public String getCategory(Entity entityInst) throws UnifyException {
        return "endpointconfig";
    }

    @Override
    public List<ParamConfig> getParamConfigList(Entity entityInst) throws UnifyException {
        String endpointName = entityInst != null ? ((EndpointConfig) entityInst).getEndpoint() : null;
        if (!StringUtils.isBlank(endpointName)) {
            return integrationModuleService.getEndpointParamConfigs(endpointName);
        }

        return Collections.emptyList();
    }

}
