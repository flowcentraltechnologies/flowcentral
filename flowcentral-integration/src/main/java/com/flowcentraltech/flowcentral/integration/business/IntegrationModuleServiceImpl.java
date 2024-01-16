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
import java.util.Map;

import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.integration.constants.IntegrationModuleNameConstants;
import com.flowcentraltech.flowcentral.integration.endpoint.Endpoint;
import com.flowcentraltech.flowcentral.integration.endpoint.EndpointManager;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.task.TaskMonitor;

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
    private EndpointManager endpointManager;

    private final Map<String, TaskMonitor> endpointReaderMonitors;

    public IntegrationModuleServiceImpl() {
        this.endpointReaderMonitors = new HashMap<String, TaskMonitor>();
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
