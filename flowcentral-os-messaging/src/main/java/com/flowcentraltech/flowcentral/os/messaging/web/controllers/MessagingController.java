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
package com.flowcentraltech.flowcentral.os.messaging.web.controllers;

import com.flowcentraltech.flowcentral.os.messaging.business.MessagingAccessManager;
import com.flowcentraltech.flowcentral.os.messaging.business.MessagingProcessor;
import com.flowcentraltech.flowcentral.os.messaging.constants.MessagingModuleNameConstants;
import com.flowcentraltech.flowcentral.os.messaging.constants.MessagingRequestHeaderConstants;
import com.flowcentraltech.flowcentral.os.messaging.constants.MessagingResponseConstants;
import com.flowcentraltech.flowcentral.os.messaging.data.BaseMessagingRequest;
import com.flowcentraltech.flowcentral.os.messaging.data.BaseMessagingResponse;
import com.flowcentraltech.flowcentral.os.messaging.data.MessagingAccess;
import com.flowcentraltech.flowcentral.os.messaging.data.MessagingError;
import com.flowcentraltech.flowcentral.os.messaging.data.MessagingErrorResponse;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.AbstractPlainJsonController;
import com.tcdng.unify.web.http.HttpRequestHeaderConstants;
import com.tcdng.unify.web.http.HttpRequestHeaders;

/**
 * Messaging Controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(MessagingModuleNameConstants.MESSAGING_CONTROLLER)
public class MessagingController extends AbstractPlainJsonController {

    @Configurable
    private MessagingAccessManager messagingAccessManager;

    @SuppressWarnings("unchecked")
    @Override
    protected final String doExecute(String actionName, String requestJson) throws UnifyException {
        long startTime = System.currentTimeMillis();
        MessagingError error = null;
        BaseMessagingResponse response = null;
        HttpRequestHeaders headers = getHttpRequestHeaders();
        final String application = headers.getHeader(MessagingRequestHeaderConstants.OS_APPLICATION);
        final String processor = headers.getHeader(MessagingRequestHeaderConstants.OS_MESSAGING_PROCESSOR);
        final String authorization = headers.getHeader(HttpRequestHeaderConstants.AUTHORIZATION);

        MessagingAccess messagingAccess = new MessagingAccess();
        messagingAccess.setApplication(application);
        messagingAccess.setAuthorization(authorization);
        messagingAccess.setProcessor(processor);
        try {
            if (StringUtils.isBlank(application)) {
                error = new MessagingError(MessagingResponseConstants.NO_APPLICATION_SPECIFIED,
                        "No messaging application specified in request headers.");
            } else if (StringUtils.isBlank(processor)) {
                error = new MessagingError(MessagingResponseConstants.NO_PROCESSOR_SPECIFIED,
                        "No messaging processor specified in request headers.");
            } else if (!isComponent(processor)) {
                error = new MessagingError(MessagingResponseConstants.PROCESSOR_UNKNOWN,
                        "Messaging processor with name is unknown.");
            } else {
                if (messagingAccessManager != null) {
                    error = messagingAccessManager.checkAccess(authorization, application, processor);
                }
            }

            if (error == null) {
                final MessagingProcessor<BaseMessagingResponse, BaseMessagingRequest> _processor = getComponent(
                        MessagingProcessor.class, processor);
                BaseMessagingRequest request = getObjectFromRequestJson(_processor.getRequestClass(), requestJson);
                request.setApplication(application);
                response = _processor.process((BaseMessagingRequest) request);
            }
        } catch (UnifyException e) {
            error = new MessagingError(MessagingResponseConstants.PROCESSING_EXCEPTION,
                    getExceptionMessage(LocaleType.APPLICATION, e));
        }

        if (error != null) {
            response = new MessagingErrorResponse(error);
        }
        
        final String responseJson = getResponseJsonFromObject(response);
        if (messagingAccessManager != null) {
            messagingAccess.setResponseCode(response.getResponseCode());
            messagingAccess.setResponseMessage(response.getResponseMessage());
            messagingAccess.setRequestBody(requestJson);
            messagingAccess.setResponseBody(responseJson);
            messagingAccess.setRuntimeInMilliSec(System.currentTimeMillis() - startTime);
            messagingAccessManager.logAccess(messagingAccess);
        }

        return responseJson;
    }

}
