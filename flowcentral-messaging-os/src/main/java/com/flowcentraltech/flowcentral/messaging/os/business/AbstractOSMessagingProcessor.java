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
package com.flowcentraltech.flowcentral.messaging.os.business;

import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.flowcentraltech.flowcentral.messaging.os.constants.OSMessagingMode;
import com.flowcentraltech.flowcentral.messaging.os.data.BaseOSMessagingReq;
import com.flowcentraltech.flowcentral.messaging.os.data.BaseOSMessagingResp;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingError;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingErrorConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Convenient abstract base class for OS messaging processors.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractOSMessagingProcessor<T extends BaseOSMessagingResp, U extends BaseOSMessagingReq>
        extends AbstractFlowCentralComponent implements OSMessagingProcessor<T, U> {

    protected static final OSMessagingError NO_ERROR = new OSMessagingError();

    private static final Object[] SUMMARY_PARAMS = new Object[0];

    @Configurable
    private OSMessagingModuleService osMessagingModuleService;

    private final String summary;

    private final Class<T> responseClass;

    private final Class<U> requestClass;

    private final OSMessagingMode messagingMode;

    protected AbstractOSMessagingProcessor(Class<T> responseClass, Class<U> requestClass, String summary) {
        this(responseClass, requestClass, summary, OSMessagingMode.SYNCHRONOUS);
    }

    protected AbstractOSMessagingProcessor(Class<T> responseClass, Class<U> requestClass, String summary,
            OSMessagingMode messagingMode) {
        this.responseClass = responseClass;
        this.requestClass = requestClass;
        this.summary = summary;
        this.messagingMode = messagingMode;
    }

    @Override
    public final Class<? extends BaseOSMessagingReq> getRequestClass() {
        return requestClass;
    }

    @Override
    public final T process(U request) throws UnifyException {
        OSMessagingError error = null;
        T resp = null;
        try {
            error = validateRequest(request);
            if (error == null || !error.isErrorPresent()) {
                resp = doProcess(request);
            }
        } catch (Exception e) {
            logError(e);
            error = createError(OSMessagingErrorConstants.PROCESSOR_EXCEPTION, e);
        }

        if (resp == null) {
            resp = ReflectUtils.newInstance(responseClass);
            resp.setResponseCode(error.getErrorCode());
            resp.setResponseMessage(error.getErrorMessage());
        }

        osMessagingModuleService.logProcessing(messagingMode,
                !StringUtils.isBlank(request.getOriginSource()) ? request.getOriginSource() : request.getSource(),
                request.getProcessor(), resolveApplicationMessage(summary, getSummaryParameters(request)),
                resp.getResponseCode(), resp.getResponseMessage());
        return resp;
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    protected Object[] getSummaryParameters(U request) {
        return SUMMARY_PARAMS;
    }

    protected final OSMessagingModuleService osmessaging() {
        return osMessagingModuleService;
    }

    protected final OSMessagingError createError(String errorCode) throws UnifyException {
        return createError(errorCode, getApplicationMessage(errorCode));
    }

    protected final OSMessagingError createError(String errorCode, Exception e) throws UnifyException {
        return createError(errorCode, getExceptionMessage(LocaleType.APPLICATION, e));
    }

    protected final OSMessagingError createError(String errorCode, Object... params) throws UnifyException {
        return new OSMessagingError(errorCode, getApplicationMessage(errorCode, params));
    }

    protected abstract OSMessagingError validateRequest(U request) throws UnifyException;

    protected abstract T doProcess(U request) throws UnifyException;
}
