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
package com.flowcentraltech.flowcentral.integration.business;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.data.ParamValuesDef;
import com.flowcentraltech.flowcentral.common.util.CommonInputUtils;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.integration.constants.IntegrationModuleErrorConstants;
import com.flowcentraltech.flowcentral.integration.constants.IntegrationModuleNameConstants;
import com.flowcentraltech.flowcentral.integration.data.EndpointDef;
import com.flowcentraltech.flowcentral.integration.endpoint.Endpoint;
import com.flowcentraltech.flowcentral.integration.entities.EndpointConfig;
import com.flowcentraltech.flowcentral.integration.entities.EndpointConfigQuery;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.ParamConfig;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Implementation of integration module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(IntegrationModuleNameConstants.INTEGRATION_MODULE_SERVICE)
public class IntegrationModuleServiceImpl extends AbstractFlowCentralService implements IntegrationModuleService {

    @Configurable
    private SystemModuleService systemModuleService;

    private final Map<String, TaskMonitor> endpointReaderMonitors;

    private final FactoryMap<String, EndpointDef> endpointDefFactoryMap;

    private final FactoryMap<String, List<ParamConfig>> endpointParamConfigByTypeMap;

    public IntegrationModuleServiceImpl() {
        this.endpointReaderMonitors = new HashMap<String, TaskMonitor>();

        this.endpointDefFactoryMap = new FactoryMap<String, EndpointDef>(true)
            {
                @Override
                protected boolean stale(String endpointConfigName, EndpointDef endpointDef) throws Exception {
                    return (environment().value(long.class, "versionNo",
                            new EndpointConfigQuery().id(endpointDef.getId())) > endpointDef.getVersion());
                }

                @Override
                protected EndpointDef create(String endpointConfigName, Object... arg1) throws Exception {
                    EndpointConfig endpointConfig = environment()
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
    }

    public final void setSystemModuleService(SystemModuleService systemModuleService) {
        this.systemModuleService = systemModuleService;
    }

    @Override
    public EndpointDef getEndpointDef(String endpointConfigName) throws UnifyException {
        return endpointDefFactoryMap.get(endpointConfigName);
    }

    @Override
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {
        // Kill all transport readers
        Iterator<Map.Entry<String, TaskMonitor>> it = endpointReaderMonitors.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, TaskMonitor> entry = it.next();
            entry.getValue().cancel();
            it.remove();
        }

        super.onTerminate();
    }

}
