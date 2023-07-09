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
package com.flowcentraltech.flowcentral.gateway.business;

import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.flowcentraltech.flowcentral.gateway.constants.GatewayResponseConstants;
import com.flowcentraltech.flowcentral.gateway.data.BaseGatewayRequest;
import com.flowcentraltech.flowcentral.gateway.data.BaseGatewayResponse;
import com.flowcentraltech.flowcentral.gateway.data.GatewayErrorResponse;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.core.util.ReflectUtils;

/**
 * Convenient abstract base class for gateway processors.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractGatewayProcessor<T extends BaseGatewayResponse, U extends BaseGatewayRequest>
        extends AbstractFlowCentralComponent implements GatewayProcessor<T, U> {

    private final Class<T> responseClass;

    private final Class<U> requestClass;

    public AbstractGatewayProcessor(Class<T> responseClass, Class<U> requestClass) {
        this.responseClass = responseClass;
        this.requestClass = requestClass;
    }

    @Override
    public final Class<? extends BaseGatewayRequest> getRequestClass() {
        return requestClass;
    }

    @Override
    public final T process(U request) throws UnifyException {
        GatewayErrorResponse error = null;
        try {
            error = validateRequest(request);
            if (error == null) {
                return doProcess(request);
            }
        } catch (Exception e) {
            error = new GatewayErrorResponse(GatewayResponseConstants.PROCESSING_EXCEPTION,
                    getExceptionMessage(LocaleType.APPLICATION, e));
        }

        T resp = ReflectUtils.newInstance(responseClass);
        resp.setResponseCode(error.getResponseCode());
        resp.setResponseMessage(error.getResponseMessage());
        return resp;
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    protected abstract GatewayErrorResponse validateRequest(U request) throws UnifyException;

    protected abstract T doProcess(U request) throws UnifyException;
}
