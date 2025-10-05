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

import com.flowcentraltech.flowcentral.messaging.os.business.OSMessagingAccessManager;
import com.flowcentraltech.flowcentral.messaging.os.business.OSMessagingModuleService;
import com.flowcentraltech.flowcentral.messaging.os.business.OSMessagingProcessor;
import com.flowcentraltech.flowcentral.messaging.os.constants.OSMessagingModuleNameConstants;
import com.flowcentraltech.flowcentral.messaging.os.data.BaseOSMessagingReq;
import com.flowcentraltech.flowcentral.messaging.os.data.BaseOSMessagingResp;
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

/**
 * OS Messaging Controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(OSMessagingModuleNameConstants.OSMESSAGING_CONTROLLER)
public class OSMessagingController extends AbstractPlainJsonController {

    @Configurable
    private OSMessagingModuleService osMessagingModuleService;

    @Configurable
    private OSMessagingAccessManager osMessagingAccessManager;

    @SuppressWarnings("unchecked")
    @Override
    protected final String doExecute(String actionName, String requestJson) throws UnifyException {
        logDebug("Executing controller request = [{0}]...", requestJson);
        OSMessagingError error = null;
        BaseOSMessagingResp response = null;
        String correlationId = null;
        if (osMessagingAccessManager == null) {
            error = getOSMessagingError(OSMessagingErrorConstants.ACCESS_MANAGER_NOT_FOUND);
        } else {
            final String authorization = getHttpRequestHeaders().getHeader(HttpRequestHeaderConstants.AUTHORIZATION);
            if (StringUtils.isBlank(authorization)) {
                error = getOSMessagingError(OSMessagingErrorConstants.AUTHORIZATION_REQUIRED);
            } else {
                try {
                    final OSMessagingHeader header = osMessagingModuleService.getOSMessagingHeader(authorization);
                    if (header.isPresent()) {
                        osMessagingAccessManager.checkAccess(header);
                        final OSMessagingProcessor<BaseOSMessagingResp, BaseOSMessagingReq> _processor = getComponent(
                                OSMessagingProcessor.class, header.getProcessor());
                        BaseOSMessagingReq request = getObjectFromRequestJson(_processor.getRequestClass(),
                                requestJson);
                        correlationId = request.getCorrelationId();
                        response = _processor.process((BaseOSMessagingReq) request);
                    } else {
                        error = getOSMessagingError(OSMessagingErrorConstants.PEER_NOT_CONFIGURED);
                    }
                } catch (Exception e) {
                    logError(e);
                    error = new OSMessagingError(OSMessagingErrorConstants.PROCESSING_EXCEPTION,
                            getExceptionMessage(LocaleType.APPLICATION, e));
                }
            }
        }

        if (response == null) {
            response = new OSMessagingErrorResponse(error);
        }

        response.setCorrelationId(correlationId);
        
        final String respJson = getResponseJsonFromObject(response);
        logDebug("Response message [\n{0}]", respJson);
        return respJson;
    }

    private OSMessagingError getOSMessagingError(String messageKey) throws UnifyException {
        return new OSMessagingError(messageKey, getApplicationMessage(messageKey));
    }
}
