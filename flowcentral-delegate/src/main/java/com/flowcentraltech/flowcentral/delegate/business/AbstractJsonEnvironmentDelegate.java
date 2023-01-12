/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.application.constants.AppletRequestAttributeConstants;
import com.flowcentraltech.flowcentral.connect.common.constants.DataSourceOperation;
import com.flowcentraltech.flowcentral.connect.common.data.BaseResponse;
import com.flowcentraltech.flowcentral.connect.common.data.DataSourceRequest;
import com.flowcentraltech.flowcentral.connect.common.data.JsonDataSourceResponse;
import com.flowcentraltech.flowcentral.connect.common.data.JsonProcedureResponse;
import com.flowcentraltech.flowcentral.connect.common.data.ProcedureRequest;
import com.flowcentraltech.flowcentral.delegate.constants.DelegateErrorCodeConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.core.constant.PrintFormat;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient abstract base class for JSON based protocol environment delegates.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractJsonEnvironmentDelegate extends AbstractEnvironmentDelegate {

    @Override
    public String[] executeProcedure(String operation, String... payload) throws UnifyException {
        ProcedureRequest req = new ProcedureRequest(operation);
        req.setPayload(payload);
        req.setUseRawPayload(true);
        JsonProcedureResponse resp = sendToDelegateProcedureService(req);
        return resp.getPayload();
    }

    protected BaseResponse sendToDelegateDatasourceService(DataSourceRequest req) throws UnifyException {
        JsonDataSourceResponse resp = null;
        try {
            String reqJSON = DataUtils.asJsonString(req, PrintFormat.NONE);
            String respJSON = sendToDelegateDatasourceService(reqJSON);
            resp = DataUtils.fromJsonString(JsonDataSourceResponse.class, respJSON);
            if (resp.error()) {
                // TODO Translate to local exception and throw
            }
        } catch (UnifyException e) {
            logError(e);
            resp = new JsonDataSourceResponse(e.getErrorCode(), getErrorMessage(LocaleType.SESSION, e.getUnifyError()));
        } catch (Exception e) {
            logError(e);
            resp = new JsonDataSourceResponse(DelegateErrorCodeConstants.DELEGATE_BACKEND_CONNECTION_ERROR, this
                    .getSessionMessage(DelegateErrorCodeConstants.DELEGATE_BACKEND_CONNECTION_ERROR, e.getMessage()));
        }

        if (resp.error()) {
            DataSourceOperation dso = req.getOperation();
            if (dso.formResult()) {
                setRequestAttribute(AppletRequestAttributeConstants.SILENT_FORM_ERROR_MSG, resp.getErrorMsg());
            } else {
                setRequestAttribute(AppletRequestAttributeConstants.SILENT_MULTIRECORD_SEARCH_ERROR_MSG, resp.getErrorMsg());
            }
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

}
