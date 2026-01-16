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

import java.io.OutputStream;
import java.util.Optional;

import com.flowcentraltech.flowcentral.messaging.os.business.OSDownloadProcessor;
import com.flowcentraltech.flowcentral.messaging.os.business.OSMessagingAccessManager;
import com.flowcentraltech.flowcentral.messaging.os.business.OSMessagingModuleService;
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
import com.tcdng.unify.web.AbstractHttpDownloadController;
import com.tcdng.unify.web.http.HttpRequestHeaders;

/**
 * OS download Controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(OSMessagingModuleNameConstants.OSMESSAGING_DOWNLOAD_CONTROLLER)
public class OSDownloadController extends AbstractHttpDownloadController {

    @Configurable
    private OSMessagingModuleService osMessagingModuleService;

    @Configurable
    private OSMessagingAccessManager osMessagingAccessManager;

    
    @SuppressWarnings("unchecked")
    @Override
    protected void handleDownload(HttpRequestHeaders headers, OutputStream out) throws UnifyException {
        logDebug("Executing controller request ...");
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
                            final Optional<String> optional = osMessagingModuleService.sendDownloadMessageToService(
                                    header, service, correlationId, fileSignature, out);
                            if (optional.isPresent()) {
                                logDebug("Response message [\n{0}]", optional.get());
                                return;
                            }

                            error = getOSMessagingError(OSMessagingErrorConstants.DELEGATE_FUNCTION_NOT_RESOLVED);
                        } else {
                            final String function = headers
                                    .getHeader(OSMessagingRequestHeaderConstants.DELEGATE_FUNCTION);
                            if (!StringUtils.isBlank(function)) {
                                logDebug("Relaying controller request to delegate function = [{0}]...", function);
                                final String fileSignature = headers.getHeader(OSMessagingRequestHeaderConstants.FILE_SIGNATURE);
                                final Optional<String> optional = osMessagingModuleService.sendDownloadMessageToDelegate(
                                        header, function, correlationId, fileSignature, out);
                                if (optional.isPresent()) {
                                    logDebug("Response message [\n{0}]", optional.get());
                                    return;
                                }

                                error = getOSMessagingError(OSMessagingErrorConstants.DELEGATE_FUNCTION_NOT_RESOLVED);
                            } else {
                                if (header.isProcessorPresent()) {
                                    final OSDownloadProcessor<BaseOSMessagingResp> _processor = getComponent(
                                            OSDownloadProcessor.class, header.getProcessor());
                                    response = _processor.process(headers, out);
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
    }

    private OSMessagingError getOSMessagingError(String messageKey) throws UnifyException {
        return new OSMessagingError(messageKey, getApplicationMessage(messageKey));
    }
}
