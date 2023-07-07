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

import com.flowcentraltech.flowcentral.gateway.business.GatewayProcessor;
import com.flowcentraltech.flowcentral.gateway.constants.GatewayRequestHeaderConstants;
import com.flowcentraltech.flowcentral.gateway.constants.GatewayResponseConstants;
import com.flowcentraltech.flowcentral.gateway.data.BaseGatewayRequest;
import com.flowcentraltech.flowcentral.gateway.data.BaseGatewayResponse;
import com.flowcentraltech.flowcentral.gateway.data.GatewayErrorResponse;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.AbstractPlainJsonController;
import com.tcdng.unify.web.http.HttpRequestHeaders;

/**
 * Gateway Controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("/flowcentral/gateway")
public class GatewayController extends AbstractPlainJsonController {

    @SuppressWarnings("unchecked")
    @Override
    protected final Object doExecute(String requestJson) throws UnifyException {
        try {
            HttpRequestHeaders headers = getHttpRequestHeaders();
            String processorName = headers.getHeader(GatewayRequestHeaderConstants.GATEWAY_PROCESSOR);
            if (StringUtils.isBlank(processorName)) {
                return new GatewayErrorResponse(GatewayResponseConstants.NO_PROCESSOR_SPECIFIED,
                        "No gateway processor specified in request headers.");
            }

            if (!isComponent(processorName)) {
                return new GatewayErrorResponse(GatewayResponseConstants.PROCESSOR_UNKNOWN,
                        "Gateway processor with name is unknown.");
            }

            GatewayProcessor<? extends BaseGatewayResponse, ? extends BaseGatewayRequest> processor = getComponent(
                    GatewayProcessor.class, processorName);
            return processor.processFromJson(requestJson);
        } catch (UnifyException e) {
            return new GatewayErrorResponse(GatewayResponseConstants.PROCESSING_EXCEPTION,
                    getExceptionMessage(LocaleType.APPLICATION, e));
        }
    }

}
