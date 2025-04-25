/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.connect.common.data.BaseResponse;
import com.flowcentraltech.flowcentral.connect.common.data.DataSourceRequest;
import com.flowcentraltech.flowcentral.connect.common.data.EntityListingRequest;
import com.flowcentraltech.flowcentral.connect.common.data.EntityListingResponse;
import com.flowcentraltech.flowcentral.connect.common.data.GetEntityRequest;
import com.flowcentraltech.flowcentral.connect.common.data.GetEntityResponse;
import com.flowcentraltech.flowcentral.connect.common.data.JsonDataSourceResponse;
import com.flowcentraltech.flowcentral.connect.common.data.JsonProcedureResponse;
import com.flowcentraltech.flowcentral.connect.common.data.ProcedureRequest;
import com.flowcentraltech.flowcentral.delegate.constants.DelegateErrorCodeConstants;
import com.tcdng.unify.common.data.DelegateEntityListingDTO;
import com.tcdng.unify.common.data.EntityDTO;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.PrintFormat;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Convenient abstract base class for JSON based protocol environment delegates.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractJsonEnvironmentDelegate extends AbstractSynchronizableEnvironmentDelegate {

    @Override
    public String[] executeProcedure(String operation, String... payload) throws UnifyException {
        ProcedureRequest req = new ProcedureRequest(operation);
        req.setPayload(payload);
        req.setUseRawPayload(true);
        JsonProcedureResponse resp = sendToDelegateProcedureService(req);
        return resp.getPayload();
    }

    @Override
    protected DelegateEntityListingDTO getDelegatedEntityList() throws UnifyException {
        String reqJSON = DataUtils.asJsonString(new EntityListingRequest(), PrintFormat.NONE);
        String respJSON = listEntities(reqJSON);
        EntityListingResponse resp = DataUtils.fromJsonString(EntityListingResponse.class, respJSON);
        return resp != null && !resp.error()
                ? new DelegateEntityListingDTO(resp.getListings(), resp.getRedirectErrors())
                : null;
    }

    @Override
    protected EntityDTO getDelegatedEntitySchema(String entity) throws UnifyException {
        String reqJSON = DataUtils.asJsonString(new GetEntityRequest(entity), PrintFormat.NONE);
        String respJSON = getEntity(reqJSON);
        GetEntityResponse resp = DataUtils.fromJsonString(GetEntityResponse.class, respJSON);
        return resp != null && !resp.error() ? resp.getEntity() : null;
    }

    protected BaseResponse sendToDelegateDatasourceService(DataSourceRequest req) throws UnifyException {
        String reqJSON = DataUtils.asJsonString(req, PrintFormat.NONE);
        String respJSON = sendToDelegateDatasourceService(reqJSON);
        JsonDataSourceResponse resp = StringUtils.isBlank(respJSON) ? new JsonDataSourceResponse()
                : DataUtils.fromJsonString(JsonDataSourceResponse.class, respJSON);
        if (resp.error()) {
            throw new UnifyException(DelegateErrorCodeConstants.DELEGATION_ERROR, resp.getErrorMsg());
        }

        return resp;
    }

    protected JsonProcedureResponse sendToDelegateProcedureService(ProcedureRequest req) throws UnifyException {
        String reqJSON = DataUtils.asJsonString(req, PrintFormat.NONE);
        String respJSON = sendToDelegateProcedureService(reqJSON);
        JsonProcedureResponse resp = DataUtils.fromJsonString(JsonProcedureResponse.class, respJSON);
        if (resp.error()) {
            // TODO Translate to local exception and throw
        }

        return resp;
    }

    protected abstract String listEntities(String jsonReq) throws UnifyException;

    protected abstract String getEntity(String jsonReq) throws UnifyException;

    protected abstract String sendToDelegateProcedureService(String jsonReq) throws UnifyException;

    protected abstract String sendToDelegateDatasourceService(String jsonReq) throws UnifyException;

}
