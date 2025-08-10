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
package com.flowcentraltech.flowcentral.messaging.os.web.controllers;

import java.util.Optional;

import com.flowcentraltech.flowcentral.messaging.os.business.OSMessagingAccessManager;
import com.flowcentraltech.flowcentral.messaging.os.business.OSMessagingProcessor;
import com.flowcentraltech.flowcentral.messaging.os.constants.OSMessagingModuleNameConstants;
import com.flowcentraltech.flowcentral.messaging.os.data.BaseOSMessagingReq;
import com.flowcentraltech.flowcentral.messaging.os.data.BaseOSMessagingResp;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingAccess;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingError;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingErrorConstants;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingErrorResponse;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingHeader;
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
 * @since 4.1
 */
@Component(OSMessagingModuleNameConstants.OSMESSAGING_CONTROLLER)
public class OSMessagingController extends AbstractPlainJsonController {

    @Configurable
    private OSMessagingAccessManager osMessagingAccessManager;

    @SuppressWarnings("unchecked")
    @Override
    protected final String doExecute(String actionName, String requestJson) throws UnifyException {
        long startTime = System.currentTimeMillis();
        OSMessagingAccess access = null;
        OSMessagingError error = null;
        BaseOSMessagingResp response = null;
        HttpRequestHeaders headers = getHttpRequestHeaders();
        final String authorization = headers.getHeader(HttpRequestHeaderConstants.AUTHORIZATION);
        if (StringUtils.isBlank(authorization)) {
            error = getOSMessagingError(OSMessagingErrorConstants.AUTHORIZATION_REQUIRED);
        } else {
            try {
                final Optional<OSMessagingHeader> hoption = osMessagingAccessManager.resolveAccess(authorization);
                if (hoption.isPresent()) {
                    final OSMessagingHeader header = hoption.get();
                    access = new OSMessagingAccess(header);
                    access.setAuthorization(authorization);
                    final OSMessagingProcessor<BaseOSMessagingResp, BaseOSMessagingReq> _processor = getComponent(
                            OSMessagingProcessor.class, header.getProcessor());
                    BaseOSMessagingReq request = getObjectFromRequestJson(_processor.getRequestClass(), requestJson);                    
                    response = _processor.process((BaseOSMessagingReq) request);
                } else {
                    error = getOSMessagingError(OSMessagingErrorConstants.NOT_AUTHORIZED);
                }
            } catch (Exception e) {
                error = new OSMessagingError(OSMessagingErrorConstants.PROCESSING_EXCEPTION,
                        getExceptionMessage(LocaleType.APPLICATION, e));
            }
        }

        if (error != null) {
            response = new OSMessagingErrorResponse(error);
        }

        final String responseJson = getResponseJsonFromObject(response);
        
        if (access == null) {
            access = new OSMessagingAccess();
        }
        
        access.setResponseCode(response.getResponseCode());
        access.setResponseMessage(response.getResponseMessage());
        access.setRequestBody(requestJson);
        access.setResponseBody(responseJson);
        access.setRuntimeInMilliSec(System.currentTimeMillis() - startTime);
        osMessagingAccessManager.logAccess(access);

        return responseJson;
    }

    private OSMessagingError getOSMessagingError(String messageKey) throws UnifyException {
        return new OSMessagingError(messageKey, getApplicationMessage(messageKey));
    }
}
