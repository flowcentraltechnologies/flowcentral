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

import java.util.HashMap;
import java.util.Map;

import com.flowcentraltech.flowcentral.connect.common.data.BaseResponse;
import com.flowcentraltech.flowcentral.connect.common.data.DataSourceRequest;
import com.flowcentraltech.flowcentral.connect.common.data.GetEntityDataSourceAliasRequest;
import com.flowcentraltech.flowcentral.connect.common.data.GetEntityDataSourceAliasResponse;
import com.flowcentraltech.flowcentral.connect.common.data.JsonDataSourceResponse;
import com.flowcentraltech.flowcentral.connect.common.data.JsonProcedureResponse;
import com.flowcentraltech.flowcentral.connect.common.data.ProcedureRequest;
import com.flowcentraltech.flowcentral.delegate.constants.DelegateErrorCodeConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.PrintFormat;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient abstract base class for JSON based protocol environment delegates.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractJsonEnvironmentDelegate extends AbstractEnvironmentDelegate {

    private Map<String, GetEntityDataSourceAliasResponse> entityDataSourceAliases;

    public AbstractJsonEnvironmentDelegate() {
        this.entityDataSourceAliases = new HashMap<String, GetEntityDataSourceAliasResponse>();
    }

    @Override
    public String[] executeProcedure(String operation, String... payload) throws UnifyException {
        ProcedureRequest req = new ProcedureRequest(operation);
        req.setPayload(payload);
        req.setUseRawPayload(true);
        JsonProcedureResponse resp = sendToDelegateProcedureService(req);
        return resp.getPayload();
    }

    @Override
    public final String getDataSourceName(String entityLongName) throws UnifyException {
        GetEntityDataSourceAliasResponse resp = entityDataSourceAliases.get(entityLongName);
        if (resp == null) {
            synchronized(this) {
                resp = entityDataSourceAliases.get(entityLongName);
                if (resp == null) {
                    String reqJSON = DataUtils.asJsonString(new GetEntityDataSourceAliasRequest(entityLongName),
                            PrintFormat.NONE);
                    String respJSON = sendToDelegateDatasourceAliasService(reqJSON);
                    resp = DataUtils.fromJsonString(GetEntityDataSourceAliasResponse.class, respJSON);
                    if (resp.error()) {
                        throw new UnifyException(DelegateErrorCodeConstants.DELEGATION_ERROR, resp.getErrorMsg());
                    } else {
                        entityDataSourceAliases.put(entityLongName, resp);
                    }
                }
            }
        }

        return resp != null ? resp.getDataSourceAlias() : null;
    }

    protected BaseResponse sendToDelegateDatasourceService(DataSourceRequest req) throws UnifyException {
        String reqJSON = DataUtils.asJsonString(req, PrintFormat.NONE);
        String respJSON = sendToDelegateDatasourceService(reqJSON);
        JsonDataSourceResponse resp = DataUtils.fromJsonString(JsonDataSourceResponse.class, respJSON);
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

    protected abstract String sendToDelegateProcedureService(String jsonReq) throws UnifyException;

    protected abstract String sendToDelegateDatasourceService(String jsonReq) throws UnifyException;

    protected abstract String sendToDelegateDatasourceAliasService(String jsonReq) throws UnifyException;

}
