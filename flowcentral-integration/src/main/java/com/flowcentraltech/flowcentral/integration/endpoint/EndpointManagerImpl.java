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
import java.util.List;

import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.data.ParamValuesDef;
import com.flowcentraltech.flowcentral.common.util.CommonInputUtils;
import com.flowcentraltech.flowcentral.integration.constants.IntegrationModuleErrorConstants;
import com.flowcentraltech.flowcentral.integration.constants.IntegrationModuleNameConstants;
import com.flowcentraltech.flowcentral.integration.data.EndpointDef;
import com.flowcentraltech.flowcentral.integration.entities.EndpointConfig;
import com.flowcentraltech.flowcentral.integration.entities.EndpointConfigQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.ParamConfig;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Default end-point manager implementation.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(IntegrationModuleNameConstants.ENDPOINT_MANAGER)
public class EndpointManagerImpl extends AbstractEndpointManager {

    private final FactoryMap<String, EndpointDef> endpointDefFactoryMap;

    private final FactoryMap<String, List<ParamConfig>> endpointParamConfigByTypeMap;

    private final FactoryMap<String, EndpointInst> endpointInstFactoryMap;

    @Configurable
    private EnvironmentService environmentService;

    public EndpointManagerImpl() {

        this.endpointDefFactoryMap = new FactoryMap<String, EndpointDef>(true)
            {
                @Override
                protected boolean stale(String endpointConfigName, EndpointDef endpointDef) throws Exception {
                    try {
                        return (environmentService.value(long.class, "versionNo",
                                new EndpointConfigQuery().id(endpointDef.getId())) > endpointDef.getVersion());
                    } catch (UnifyException e) {
                        logError(e);
                    }
                    
                    return true;
                }

                @Override
                protected EndpointDef create(String endpointConfigName, Object... arg1) throws Exception {
                    EndpointConfig endpointConfig = environmentService
                            .list(new EndpointConfigQuery().name(endpointConfigName));
                    if (endpointConfig == null) {
                        throw new UnifyException(IntegrationModuleErrorConstants.CANNOT_FIND_ENDPOINT_CONFIG,
                                endpointConfigName);
                    }

                    ParamValuesDef pvd = CommonInputUtils.getParamValuesDef(
                            endpointParamConfigByTypeMap.get(endpointConfig.getEndpoint()),
                            endpointConfig.getEndpointParams());
                    return new EndpointDef(endpointConfigName, endpointConfig.getEndpoint(), endpointConfig.getId(),
                            endpointConfig.getVersionNo(), pvd);
                }

            };

        endpointParamConfigByTypeMap = new FactoryMap<String, List<ParamConfig>>()
            {

                @Override
                protected List<ParamConfig> create(String endpointName, Object... arg1) throws Exception {
                    return DataUtils.unmodifiableList(getComponentParamConfigs(Endpoint.class, endpointName));
                }

            };
            
        endpointInstFactoryMap = new FactoryMap<String, EndpointInst>(true)
            {
                @Override
                protected boolean stale(String endpointName, EndpointInst endpointInst) throws Exception {
                    return getEndpointDef(endpointName).getVersion() > endpointInst
                            .getEndpointDef().getVersion();
                }

                @Override
                protected EndpointInst create(String endpointName, Object... arg1) throws Exception {
                    EndpointDef endpointDef = getEndpointDef(endpointName);
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

    public final void setEnvironmentService(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    @Override
    public EndpointDef getEndpointDef(String endpointConfigName) throws UnifyException {
        return endpointDefFactoryMap.get(endpointConfigName);
    }

    @Override
    public Endpoint getEndpoint(String endpointName) throws UnifyException {
        return endpointInstFactoryMap.get(endpointName).getEndpoint();
    }

    @Override
    protected void onTerminate() throws UnifyException {
        for (String endpointName : new ArrayList<String>(endpointInstFactoryMap.keySet())) {
            try {
                endpointInstFactoryMap.remove(endpointName);
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
