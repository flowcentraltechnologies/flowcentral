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
import com.tcdng.unify.core.UnifyException;

/**
 * Convenient abstract base class for OS asynchronous messaging error processors.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractOSAsyncMessagingErrorProcessor extends AbstractFlowCentralComponent
        implements OSAsyncMessagingErrorProcessor {

    @Override
    public final void handleError(String target, String processor, String correlationId, String errorCode,
            String errorMsg) throws UnifyException {
        logDebug(
                "Handling asynchronous messaging error with code [{0}], error message [{1}], processor [{2}] and correlation ID [{3}] ...",
                errorCode, errorMsg, processor, correlationId);
        doHandleError(target, processor, correlationId, errorCode, errorMsg);
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    protected abstract void doHandleError(String target, String processor, String correlationId, String errorCode,
            String errorMsg) throws UnifyException;
}
