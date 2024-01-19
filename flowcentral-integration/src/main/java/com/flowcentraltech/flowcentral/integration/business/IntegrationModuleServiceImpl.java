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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.data.SearchInput;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.integration.constants.IntegrationModuleNameConstants;
import com.flowcentraltech.flowcentral.integration.endpoint.Endpoint;
import com.flowcentraltech.flowcentral.integration.endpoint.EndpointManager;
import com.flowcentraltech.flowcentral.integration.entities.EndpointConfig;
import com.flowcentraltech.flowcentral.integration.entities.EndpointConfigQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.data.ParamConfig;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Implementation of integration module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(IntegrationModuleNameConstants.INTEGRATION_MODULE_SERVICE)
public class IntegrationModuleServiceImpl extends AbstractFlowCentralService implements IntegrationModuleService {

    private static final int MAX_SEARCH_LIMIT = 50;

    @Configurable
    private EndpointManager endpointManager;

    private final FactoryMap<String, List<ParamConfig>> endpointParamConfigByTypeMap;

    public IntegrationModuleServiceImpl() {
        endpointParamConfigByTypeMap = new FactoryMap<String, List<ParamConfig>>()
        {

            @Override
            protected List<ParamConfig> create(String endpointName, Object... arg1) throws Exception {
                return DataUtils.unmodifiableList(getComponentParamConfigs(Endpoint.class, endpointName));
            }

        };
    }
    
    @Override
    public Listable getConfig(String configName) throws UnifyException {
        EndpointConfig config = environment()
                .find(new EndpointConfigQuery().name(configName).addSelect("name", "description"));
        return config != null ? new ListData(config.getName(), config.getDescription()) : null;
    }

    @Override
    public List<? extends Listable> getConfigList(SearchInput searchInput) throws UnifyException {
        final String input = searchInput != null && !StringUtils.isBlank(searchInput.getInput())
                ? searchInput.getInput()
                : null;
        final int limit = StringUtils.isBlank(input) ? MAX_SEARCH_LIMIT
                : (searchInput != null ? searchInput.getLimit() : 0);
        EndpointConfigQuery query = new EndpointConfigQuery();
        if (!StringUtils.isBlank(input)) {
            query.nameStartsWith(input);
        }

        query.setLimit(limit);
        List<EndpointConfig> configs = environment()
                .findAll(query.addSelect("name", "description").addOrder("description"));
        if (!DataUtils.isBlank(configs)) {
            List<ListData> result = new ArrayList<ListData>();
            for (EndpointConfig config : configs) {
                result.add(new ListData(config.getName(), config.getDescription()));
            }

            return result;
        }

        return Collections.emptyList();
    }

    @Override
    public void sendMessage(String endpointConfigName, String destination, String text) throws UnifyException {
        Endpoint endpoint = endpointManager.getEndpoint(endpointConfigName);
        endpoint.sendMessage(destination, text);
    }

    @Override
    public String receiveMessage(String endpointConfigName, String source) throws UnifyException {
        Endpoint endpoint = endpointManager.getEndpoint(endpointConfigName);
        return endpoint.receiveMessage(source);
    }
    
    @Override
    public List<ParamConfig> getEndpointParamConfigs(String endpointComponentName) throws UnifyException {
        return endpointParamConfigByTypeMap.get(endpointComponentName);
    }

    @Override
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {

    }

}
