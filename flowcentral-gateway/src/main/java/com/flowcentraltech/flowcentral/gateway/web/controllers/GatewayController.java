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
package com.flowcentraltech.flowcentral.gateway.web.controllers;

import com.flowcentraltech.flowcentral.gateway.business.GatewayAccessManager;
import com.flowcentraltech.flowcentral.gateway.business.GatewayProcessor;
import com.flowcentraltech.flowcentral.gateway.constants.GatewayRequestHeaderConstants;
import com.flowcentraltech.flowcentral.gateway.constants.GatewayResponseConstants;
import com.flowcentraltech.flowcentral.gateway.data.BaseGatewayRequest;
import com.flowcentraltech.flowcentral.gateway.data.BaseGatewayResponse;
import com.flowcentraltech.flowcentral.gateway.data.GatewayAccess;
import com.flowcentraltech.flowcentral.gateway.data.GatewayError;
import com.flowcentraltech.flowcentral.gateway.data.GatewayErrorResponse;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.AbstractPlainJsonController;
import com.tcdng.unify.web.http.HttpRequestHeaderConstants;
import com.tcdng.unify.web.http.HttpRequestHeaders;

/**
 * Gateway Controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("/flowcentral/gateway")
public class GatewayController extends AbstractPlainJsonController {

    @Configurable
    private GatewayAccessManager gatewayAccessManager;

    @SuppressWarnings("unchecked")
    @Override
    protected final String doExecute(String requestJson) throws UnifyException {
        long startTime = System.currentTimeMillis();
        GatewayError error = null;
        BaseGatewayResponse response = null;
        HttpRequestHeaders headers = getHttpRequestHeaders();
        final String application = headers.getHeader(GatewayRequestHeaderConstants.GATEWAY_APPLICATION);
        final String processor = headers.getHeader(GatewayRequestHeaderConstants.GATEWAY_PROCESSOR);
        final String authorization = headers.getHeader(HttpRequestHeaderConstants.AUTHORIZATION);

        GatewayAccess gatewayAccess = new GatewayAccess();
        gatewayAccess.setApplication(application);
        gatewayAccess.setAuthorization(authorization);
        gatewayAccess.setProcessor(processor);
        try {
            
            if (StringUtils.isBlank(application)) {
                error = new GatewayError(GatewayResponseConstants.NO_APPLICATION_SPECIFIED,
                        "No gateway application specified in request headers.");
            } else if (StringUtils.isBlank(processor)) {
                error = new GatewayError(GatewayResponseConstants.NO_PROCESSOR_SPECIFIED,
                        "No gateway processor specified in request headers.");
            } else if (!isComponent(processor)) {
                error = new GatewayError(GatewayResponseConstants.PROCESSOR_UNKNOWN,
                        "Gateway processor with name is unknown.");
            } else {
                if (gatewayAccessManager != null) {
                    error = gatewayAccessManager.checkAccess(gatewayAccess);
                }
            }

            if (error == null) {
                final GatewayProcessor<BaseGatewayResponse, BaseGatewayRequest> _processor = getComponent(
                        GatewayProcessor.class, processor);
                BaseGatewayRequest request = getObjectFromRequestJson(_processor.getRequestClass(), requestJson);
                request.setApplication(application);
                response = _processor.process((BaseGatewayRequest) request);
            }
        } catch (UnifyException e) {
            error = new GatewayError(GatewayResponseConstants.PROCESSING_EXCEPTION,
                    getExceptionMessage(LocaleType.APPLICATION, e));
        }

        if (error != null) {
            response = new GatewayErrorResponse(error);
        }
        
        final String responseJson = getResponseJsonFromObject(response);
        if (gatewayAccessManager != null) {
            gatewayAccess.setResponseCode(response.getResponseCode());
            gatewayAccess.setResponseMessage(response.getResponseMessage());
            gatewayAccess.setRequestBody(requestJson);
            gatewayAccess.setResponseBody(responseJson);
            gatewayAccess.setRuntimeInMilliSec(System.currentTimeMillis() - startTime);
            gatewayAccessManager.logAccess(gatewayAccess);
        }

        return responseJson;
    }

}
