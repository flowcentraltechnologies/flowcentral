/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.integration.rest.business;

import java.util.Set;

import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.integration.endpoint.EndpointManager;
import com.flowcentraltech.flowcentral.integration.rest.constants.IntegrationRestModuleNameConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Broadcast;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Periodic;
import com.tcdng.unify.core.annotation.PeriodicType;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.application.InstallationContext;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ControllerFinder;

/**
 * Implementation of integration REST module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Transactional
@Component(IntegrationRestModuleNameConstants.INTEGRATION_REST_MODULE_SERVICE)
public class IntegrationRestModuleServiceImpl extends AbstractFlowCentralService implements IntegrationRestModuleService {

    @Configurable
    private EndpointManager endpointManager;

    @Configurable
    private ControllerFinder controllerFinder;

    private boolean init;
    
    @Periodic(PeriodicType.FAST)
    public void checkRestEndpointChange(TaskMonitor taskMonitor) throws UnifyException {
        logDebug("Checking REST endpoint change...");
        if (init) {
            Set<String> restEndpointNames = endpointManager.getAndClearChangedRestEndpoint();
            if (!DataUtils.isBlank(restEndpointNames)) {
                updateAllRestEndpointCache();
            }
        } else {
            updateAllRestEndpointCache();
            init = true;
        }
    }
    
    @Broadcast
    public void updateAllRestEndpointCache() throws UnifyException {
        logDebug("Updating REST endpoints...");
        Set<String> activeRestEndpointPaths = endpointManager.getActiveRestEndpointPaths();
        controllerFinder.setControllerAliases(IntegrationRestModuleNameConstants.INTEGRATION_REST_CONTROLLER,
                activeRestEndpointPaths);
        logDebug("REST endpoints successfully updated.");
    }
 
    @Override
    protected void doInstallModuleFeatures(final InstallationContext ctx,ModuleInstall moduleInstall) throws UnifyException {

    }

}
