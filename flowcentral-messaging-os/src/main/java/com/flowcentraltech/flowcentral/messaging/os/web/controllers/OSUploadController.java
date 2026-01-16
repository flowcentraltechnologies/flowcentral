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

import java.io.InputStream;
import java.util.Optional;

import com.flowcentraltech.flowcentral.messaging.os.business.OSMessagingAccessManager;
import com.flowcentraltech.flowcentral.messaging.os.business.OSMessagingModuleService;
import com.flowcentraltech.flowcentral.messaging.os.business.OSUploadProcessor;
import com.flowcentraltech.flowcentral.messaging.os.constants.OSMessagingModuleNameConstants;
import com.flowcentraltech.flowcentral.messaging.os.data.BaseOSMessagingResp;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingError;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingErrorConstants;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingErrorResponse;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingHeader;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingRequestHeaderConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.AbstractHttpUploadController;
import com.tcdng.unify.web.http.HttpRequestHeaders;
import com.tcdng.unify.web.util.ContentDisposition;

/**
 * OS Upload Controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(OSMessagingModuleNameConstants.OSMESSAGING_UPLOAD_CONTROLLER)
public class OSUploadController extends AbstractHttpUploadController {

    @Configurable
    private OSMessagingModuleService osMessagingModuleService;

    @Configurable
    private OSMessagingAccessManager osMessagingAccessManager;

    @SuppressWarnings("unchecked")
    @Override
    protected String handleUpload(HttpRequestHeaders headers, ContentDisposition disposition, InputStream in)
            throws UnifyException {
        logDebug("Executing controller request = [{0}]...", disposition);
        OSMessagingError error = null;
        BaseOSMessagingResp response = null;
        final String correlationId = headers.getHeader(OSMessagingRequestHeaderConstants.CORRELATION_ID);
        if (osMessagingAccessManager == null) {
            error = getOSMessagingError(OSMessagingErrorConstants.ACCESS_MANAGER_NOT_FOUND);
        } else {
            final String authorization = headers.getHeader(OSMessagingRequestHeaderConstants.AUTHORIZATION);
            if (!StringUtils.isBlank(authorization)) {
                try {
                    final OSMessagingHeader header = osMessagingModuleService.getOSMessagingHeader(authorization);
                    if (header.isSourcePresent()) {
                        osMessagingAccessManager.checkAccess(header);
                        final String service = headers.getHeader(OSMessagingRequestHeaderConstants.DELEGATE_SERVICE);
                        if (!StringUtils.isBlank(service)) {
                            logDebug("Relaying controller request to delegate service = [{0}]...", service);
                            final String fileSignature = headers.getHeader(OSMessagingRequestHeaderConstants.FILE_SIGNATURE);
                            final Optional<String> optional = osMessagingModuleService.sendUploadMessageToService(
                                    header, service, correlationId, fileSignature, disposition, in);
                            if (optional.isPresent()) {
                                logDebug("Response message [\n{0}]", optional.get());
                                return optional.get();
                            }

                            error = getOSMessagingError(OSMessagingErrorConstants.DELEGATE_FUNCTION_NOT_RESOLVED);
                        } else {
                            final String function = headers
                                    .getHeader(OSMessagingRequestHeaderConstants.DELEGATE_FUNCTION);
                            if (!StringUtils.isBlank(function)) {
                                logDebug("Relaying controller request to delegate function = [{0}]...", function);
                                final String fileSignature = headers.getHeader(OSMessagingRequestHeaderConstants.FILE_SIGNATURE);
                                final Optional<String> optional = osMessagingModuleService.sendUploadMessageToDelegate(
                                        header, function, correlationId, fileSignature, disposition, in);
                                if (optional.isPresent()) {
                                    logDebug("Response message [\n{0}]", optional.get());
                                    return optional.get();
                                }

                                error = getOSMessagingError(OSMessagingErrorConstants.DELEGATE_FUNCTION_NOT_RESOLVED);
                            } else {
                                if (header.isProcessorPresent()) {
                                    final OSUploadProcessor<BaseOSMessagingResp> _processor = getComponent(
                                            OSUploadProcessor.class, header.getProcessor());
                                    response = _processor.process(headers, disposition, in);
                                } else {
                                    error = getOSMessagingError(OSMessagingErrorConstants.PROCESSOR_NOT_FOUND);
                                }
                            }
                        }
                    } else {
                        error = getOSMessagingError(OSMessagingErrorConstants.PEER_NOT_CONFIGURED);
                    }
                } catch (Exception e) {
                    logError(e);
                    error = new OSMessagingError(OSMessagingErrorConstants.PROCESSING_EXCEPTION,
                            getExceptionMessage(LocaleType.APPLICATION, e));
                }
            } else {
                error = getOSMessagingError(OSMessagingErrorConstants.AUTHORIZATION_REQUIRED);
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
