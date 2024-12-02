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
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.HttpCRUDControllerProcessor;

/**
 * REST JSON CRUD controller processor.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface RestJsonCRUDControllerProcessor extends HttpCRUDControllerProcessor {

    /**
     * Applies settings to processor.
     * 
     * @param endpointDef
     *                        the endpoint definition
     * @param endpointPathDef
     *                        the endpoint path definitions
     * @throws UnifyException
     *                        if an error occiurs
     */
    void applySettings(EndpointDef endpointDef, EndpointPathDef endpointPathDef) throws UnifyException;
    
}
