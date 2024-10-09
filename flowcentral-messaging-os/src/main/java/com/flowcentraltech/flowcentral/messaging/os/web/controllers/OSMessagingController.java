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
package com.flowcentraltech.flowcentral.messaging.os.web.controllers;

import java.util.UUID;

import com.flowcentraltech.flowcentral.messaging.os.business.OSMessagingAccessManager;
import com.flowcentraltech.flowcentral.messaging.os.business.OSMessagingProcessor;
import com.flowcentraltech.flowcentral.messaging.os.constants.OSMessagingModuleNameConstants;
import com.flowcentraltech.flowcentral.messaging.os.constants.OSMessagingRequestHeaderConstants;
import com.flowcentraltech.flowcentral.messaging.os.data.BaseOSMessagingReq;
import com.flowcentraltech.flowcentral.messaging.os.data.BaseOSMessagingResp;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingAccess;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingError;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingErrorResponse;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingResponseConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.AbstractPlainJsonController;
import com.tcdng.unify.web.http.HttpRequestHeaderConstants;
import com.tcdng.unify.web.http.HttpRequestHeaders;

/**
 * OS Messaging Controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(OSMessagingModuleNameConstants.OSMESSAGING_CONTROLLER)
public class OSMessagingController extends AbstractPlainJsonController {

    @Configurable
    private OSMessagingAccessManager oSMessagingAccessManager;

    @SuppressWarnings("unchecked")
    @Override
    protected final String doExecute(String actionName, String requestJson) throws UnifyException {
        long startTime = System.currentTimeMillis();
        OSMessagingError error = null;
        BaseOSMessagingResp response = null;
        HttpRequestHeaders headers = getHttpRequestHeaders();
        final String target = headers.getHeader(OSMessagingRequestHeaderConstants.OS_TARGET_APPLICATION);
        final String source = headers.getHeader(OSMessagingRequestHeaderConstants.OS_SOURCE_APPLICATION);
        final String processor = headers.getHeader(OSMessagingRequestHeaderConstants.OS_MESSAGING_PROCESSOR);
        final String authorization = headers.getHeader(HttpRequestHeaderConstants.AUTHORIZATION);

        OSMessagingAccess oSMessagingAccess = new OSMessagingAccess();
        oSMessagingAccess.setTarget(target);
        oSMessagingAccess.setSource(source);
        oSMessagingAccess.setAuthorization(authorization);
        oSMessagingAccess.setProcessor(processor);
        try {
            if (StringUtils.isBlank(target)) {
                error = new OSMessagingError(OSMessagingResponseConstants.NO_TARGET_SPECIFIED,
                        "No messaging target application specified in request headers.");
            } else if (StringUtils.isBlank(source)) {
                error = new OSMessagingError(OSMessagingResponseConstants.NO_SOURCE_SPECIFIED,
                        "No messaging source application specified in request headers.");
            } else if (StringUtils.isBlank(processor)) {
                error = new OSMessagingError(OSMessagingResponseConstants.NO_PROCESSOR_SPECIFIED,
                        "No messaging processor specified in request headers.");
            } else if (!isComponent(processor)) {
                error = new OSMessagingError(OSMessagingResponseConstants.PROCESSOR_UNKNOWN,
                        "Messaging processor with name is unknown.");
            } else {
                if (oSMessagingAccessManager != null) {
                    error = oSMessagingAccessManager.checkAccess(authorization, source, target, processor);
                }
            }

            if (error == null) {
                final OSMessagingProcessor<BaseOSMessagingResp, BaseOSMessagingReq> _processor = getComponent(
                        OSMessagingProcessor.class, processor);
                BaseOSMessagingReq request = getObjectFromRequestJson(_processor.getRequestClass(), requestJson);
                request.setSource(source);
                request.setTarget(target);
                response = _processor.process((BaseOSMessagingReq) request);
            }
        } catch (UnifyException e) {
            error = new OSMessagingError(OSMessagingResponseConstants.PROCESSING_EXCEPTION,
                    getExceptionMessage(LocaleType.APPLICATION, e));
        }

        if (error != null) {
            response = new OSMessagingErrorResponse(error);
        }
        
        final String reference = UUID.randomUUID().toString();
        response.setReference(reference);
        
        final String responseJson = getResponseJsonFromObject(response);
        if (oSMessagingAccessManager != null) {
            oSMessagingAccess.setReference(reference);
            oSMessagingAccess.setResponseCode(response.getResponseCode());
            oSMessagingAccess.setResponseMessage(response.getResponseMessage());
            oSMessagingAccess.setRequestBody(requestJson);
            oSMessagingAccess.setResponseBody(responseJson);
            oSMessagingAccess.setRuntimeInMilliSec(System.currentTimeMillis() - startTime);
            oSMessagingAccessManager.logAccess(oSMessagingAccess);
        }

        return responseJson;
    }

}
