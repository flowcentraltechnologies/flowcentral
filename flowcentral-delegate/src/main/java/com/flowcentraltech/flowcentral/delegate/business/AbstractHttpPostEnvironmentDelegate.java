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

package com.flowcentraltech.flowcentral.delegate.business;

import com.flowcentraltech.flowcentral.connect.common.constants.FlowCentralInterconnectConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.IOUtils;

/**
 * Convenient abstract base class for HTTP Post environment delegates.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractHttpPostEnvironmentDelegate extends AbstractJsonEnvironmentDelegate {

    @Override
    protected String listEntities(String jsonReq) throws UnifyException {
        return IOUtils.postJsonToEndpoint(
                getEndpointNode() + FlowCentralInterconnectConstants.INTERCONNECT_CONTROLLER + "/listEntities", jsonReq);
    }

    @Override
    protected String getEntity(String jsonReq) throws UnifyException {
        return IOUtils.postJsonToEndpoint(
                getEndpointNode() + FlowCentralInterconnectConstants.INTERCONNECT_CONTROLLER + "/getEntity", jsonReq);
    }

    @Override
    protected String sendToDelegateProcedureService(String jsonReq) throws UnifyException {
        return IOUtils.postJsonToEndpoint(
                getEndpointNode() + FlowCentralInterconnectConstants.INTERCONNECT_CONTROLLER + "/procedure", jsonReq);
    }

    @Override
    protected String sendToDelegateDatasourceService(String jsonReq) throws UnifyException {
        return IOUtils.postJsonToEndpoint(
                getEndpointNode() + FlowCentralInterconnectConstants.INTERCONNECT_CONTROLLER + "/datasource", jsonReq);
    }

    protected abstract String getEndpointNode() throws UnifyException;
}
