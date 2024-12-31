/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.integration.rest.web.controllers;

import com.flowcentraltech.flowcentral.integration.data.EndpointDef;
import com.flowcentraltech.flowcentral.integration.data.EndpointPathDef;
import com.flowcentraltech.flowcentral.integration.endpoint.EndpointManager;
import com.flowcentraltech.flowcentral.integration.rest.constants.IntegrationRestModuleNameConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.web.AbstractJsonCRUDController;
import com.tcdng.unify.web.HttpCRUDControllerProcessor;

/**
 * REST JSON CRUD controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(IntegrationRestModuleNameConstants.INTEGRATION_REST_CONTROLLER)
public class RestJsonCRUDController extends AbstractJsonCRUDController {

    @Configurable
    private EndpointManager endpointManager;
    
    private FactoryMap<String, RestJsonCRUDControllerProcessor> processors;
    
    public RestJsonCRUDController() {
        this.processors = new FactoryMap<String, RestJsonCRUDControllerProcessor>() {
            @Override
            protected RestJsonCRUDControllerProcessor create(String key, Object... params) throws Exception {
                return getComponent(RestJsonCRUDControllerProcessor.class, IntegrationRestModuleNameConstants.RESTJSONCRUD_PROCESSOR);
            }            
        };
    }
    
    @Override
    protected HttpCRUDControllerProcessor processor(String basePath, String resource) throws UnifyException {
        logDebug("Resolving CRUD controller processor for base path [{0}] and resource [{1}]...", basePath, resource);
        final EndpointDef endpointDef = endpointManager.getEndpointDef(basePath);
        if (!endpointDef.isPathDef(resource)) {
            throwOperationErrorException(
                    new IllegalArgumentException("Unknown endpoint resource \'" + resource + "\'."));
        }
        
        final EndpointPathDef endpointPathDef = endpointDef.getPathDef(resource);
        final RestJsonCRUDControllerProcessor processor = processors.get(basePath + resource);
        processor.applySettings(endpointDef, endpointPathDef);
        logDebug("CRUD controller processor resolved.");
        return processor;
    }

}
