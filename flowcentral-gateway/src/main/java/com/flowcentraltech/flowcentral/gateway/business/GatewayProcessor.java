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
package com.flowcentraltech.flowcentral.gateway.business;

import com.flowcentraltech.flowcentral.gateway.data.BaseGatewayRequest;
import com.flowcentraltech.flowcentral.gateway.data.BaseGatewayResponse;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Gateway processor.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface GatewayProcessor<T extends BaseGatewayResponse, U extends BaseGatewayRequest> extends UnifyComponent {

    /**
     * Processes a gateway request.
     * 
     * @param request
     *                the request to process
     * @return the gateway response
     * @throws UnifyException
     *                        if an error occurs
     */
    T process(U request) throws UnifyException;
}
