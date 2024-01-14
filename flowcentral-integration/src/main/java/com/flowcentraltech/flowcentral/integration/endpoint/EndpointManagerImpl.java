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
package com.flowcentraltech.flowcentral.integration.endpoint;

import java.util.ArrayList;

import com.flowcentraltech.flowcentral.integration.business.IntegrationModuleService;
import com.flowcentraltech.flowcentral.integration.constants.IntegrationModuleNameConstants;
import com.flowcentraltech.flowcentral.integration.data.EndpointDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.FactoryMap;

/**
 * Default end-point manager implementation.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(IntegrationModuleNameConstants.ENDPOINT_MANAGER)
public class EndpointManagerImpl extends AbstractEndpointManager {

    @Configurable
    private IntegrationModuleService integrationModuleService;

    private final FactoryMap<String, EndpointInst> endpointDefFactoryMap;

    public EndpointManagerImpl() {
        this.endpointDefFactoryMap = new FactoryMap<String, EndpointInst>(true)
            {
                @Override
                protected boolean stale(String endpointName, EndpointInst endpointInst) throws Exception {
                    return integrationModuleService.getEndpointDef(endpointName).getVersion() > endpointInst
                            .getEndpointDef().getVersion();
                }

                @Override
                protected EndpointInst create(String endpointName, Object... arg1) throws Exception {
                    EndpointDef endpointDef = integrationModuleService.getEndpointDef(endpointName);
                    Endpoint endpoint = (Endpoint) getComponent(endpointDef.getEndpointName());
                    endpoint.setup(endpointDef);
                    return new EndpointInst(endpointDef, endpoint);
                }

                @Override
                protected void onRemove(EndpointInst endpointInst) throws Exception {
                    endpointInst.getEndpoint().terminate();
                }

            };

    }

    public final void setIntegrationModuleService(IntegrationModuleService integrationModuleService) {
        this.integrationModuleService = integrationModuleService;
    }

    @Override
    public Endpoint getEndpoint(String endpointName) throws UnifyException {
        return endpointDefFactoryMap.get(endpointName).getEndpoint();
    }

    @Override
    protected void onTerminate() throws UnifyException {
        for (String endpointName : new ArrayList<String>(endpointDefFactoryMap.keySet())) {
            try {
                endpointDefFactoryMap.remove(endpointName);
            } catch (Exception e) {
                logError(e);
            }
        }
    }

    private class EndpointInst {

        private EndpointDef endpointDef;

        private Endpoint endpoint;

        public EndpointInst(EndpointDef endpointDef, Endpoint endpoint) {
            this.endpointDef = endpointDef;
            this.endpoint = endpoint;
        }

        public EndpointDef getEndpointDef() {
            return endpointDef;
        }

        public Endpoint getEndpoint() {
            return endpoint;
        }
    }
}
