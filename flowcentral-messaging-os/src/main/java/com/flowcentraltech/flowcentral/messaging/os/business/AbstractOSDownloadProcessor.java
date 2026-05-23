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

import java.io.OutputStream;

import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.flowcentraltech.flowcentral.messaging.os.data.BaseOSMessagingResp;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingError;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.web.http.HttpRequestHeaders;

/**
 * Convenient abstract base class for OS download processors.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractOSDownloadProcessor<T extends BaseOSMessagingResp> extends AbstractFlowCentralComponent
        implements OSDownloadProcessor<T> {

    protected static final OSMessagingError NO_ERROR = new OSMessagingError();

    @Configurable
    private OSMessagingModuleService osMessagingModuleService;

    private final Class<T> responseClass;

    protected AbstractOSDownloadProcessor(Class<T> responseClass) {
        this.responseClass = responseClass;
    }

    @Override
    public final Class<? extends BaseOSMessagingResp> getResponseClass() {
        return responseClass;
    }

    @Override
    public T process(HttpRequestHeaders headers, OutputStream out) throws UnifyException {
        return doProcess(headers, out);
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

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

    protected abstract T doProcess(HttpRequestHeaders headers, OutputStream out) throws UnifyException;
}
