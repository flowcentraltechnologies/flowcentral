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
package com.flowcentraltech.flowcentral.connect.unify.controllers;

import com.flowcentraltech.flowcentral.connect.common.constants.DataSourceErrorCodeConstants;
import com.flowcentraltech.flowcentral.connect.common.constants.FlowCentralInterconnectConstants;
import com.flowcentraltech.flowcentral.connect.common.data.DataSourceRequest;
import com.flowcentraltech.flowcentral.connect.common.data.DetectEntityRequest;
import com.flowcentraltech.flowcentral.connect.common.data.DetectEntityResponse;
import com.flowcentraltech.flowcentral.connect.common.data.EntityListingRequest;
import com.flowcentraltech.flowcentral.connect.common.data.EntityListingResponse;
import com.flowcentraltech.flowcentral.connect.common.data.GetEntityRequest;
import com.flowcentraltech.flowcentral.connect.common.data.GetEntityResponse;
import com.flowcentraltech.flowcentral.connect.common.data.JsonDataSourceResponse;
import com.flowcentraltech.flowcentral.connect.common.data.JsonProcedureResponse;
import com.flowcentraltech.flowcentral.connect.common.data.ProcedureRequest;
import com.flowcentraltech.flowcentral.connect.unify.service.UnifyInterconnectService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.web.AbstractJsonObjectController;
import com.tcdng.unify.web.annotation.Action;

/**
 * Flow central unify interconnect controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(FlowCentralInterconnectConstants.INTERCONNECT_CONTROLLER)
public class UnifyInterconnectController extends AbstractJsonObjectController {

    @Configurable
    private UnifyInterconnectService unifyInterconnectService;

    private UnifyInterconnectRedirect unifyInterconnectRedirect;

    public final void setSpringBootInterconnectService(UnifyInterconnectService unifyInterconnectService) {
        this.unifyInterconnectService = unifyInterconnectService;
    }

    @Action
    public EntityListingResponse listEntities(EntityListingRequest req) throws Exception {
        EntityListingResponse result = unifyInterconnectService.listEntities(req);
        EntityListingResponse resp = unifyInterconnectRedirect != null
                ? unifyInterconnectRedirect.listEntities(req)
                : null;
        result.merge(resp);
        return result;
    }

    @Action
    public DetectEntityResponse detectEntity(DetectEntityRequest req) throws Exception {
        DetectEntityResponse resp = null;
        try {
            resp = unifyInterconnectService.detectEntity(req);
        } catch (Exception e) {
            logSevere(e);
            resp = new DetectEntityResponse();
            resp.setErrorCode(DataSourceErrorCodeConstants.PROVIDER_SERVICE_EXCEPTION);
            resp.setErrorMsg(e.getMessage());
        }
        
        if (resp == null || resp.error() || !resp.isPresent()) {
            resp = unifyInterconnectRedirect != null
                    ? unifyInterconnectRedirect.detectEntity(req)
                    : null;
        }

        return resp;
    }

    @Action
    public GetEntityResponse getEntity(GetEntityRequest req) throws Exception {
        GetEntityResponse resp = null;
        try {
            resp = unifyInterconnectService.getEntity(req);
        } catch (Exception e) {
            logSevere(e);
            resp = new GetEntityResponse();
            resp.setErrorCode(DataSourceErrorCodeConstants.PROVIDER_SERVICE_EXCEPTION);
            resp.setErrorMsg(e.getMessage());
        }
        
        if (resp == null || resp.error() || !resp.present()) {
            resp = unifyInterconnectRedirect != null
                    ? unifyInterconnectRedirect.getEntity(req)
                    : null;
        }

        return resp;
    }

    @Action
    public JsonDataSourceResponse dataSource(DataSourceRequest req) {
        JsonDataSourceResponse resp = null;
        try {
            resp = unifyInterconnectService.processDataSourceRequest(req);
        } catch (Exception e) {
            logSevere(e);
            resp = new JsonDataSourceResponse();
            resp.setErrorCode(DataSourceErrorCodeConstants.PROVIDER_SERVICE_EXCEPTION);
            resp.setErrorMsg(e.getMessage());
        }
        
        if (resp == null) {
            resp = unifyInterconnectRedirect != null
                    ? unifyInterconnectRedirect.processDataSourceRequest(req)
                    : null;
        }

        return resp;
    }

    @Action
    public JsonProcedureResponse procedure(ProcedureRequest req) {
        JsonProcedureResponse resp = null;
        try {
            resp = unifyInterconnectService.executeProcedureRequest(req);
        } catch (Exception e) {
            logSevere(e);
            resp = new JsonProcedureResponse();
            resp.setErrorCode(DataSourceErrorCodeConstants.PROVIDER_SERVICE_EXCEPTION);
            resp.setErrorMsg(e.getMessage());
        }
        
        if (resp == null) {
            resp = unifyInterconnectRedirect != null
                    ? unifyInterconnectRedirect.executeProcedureRequest(req)
                    : null;
        }

        return resp;
    }

    @Override
    protected void onInitialize() throws UnifyException {
        super.onInitialize();
        String redirect = unifyInterconnectService.getRedirect();
        if (redirect != null) {
            unifyInterconnectRedirect = getComponent(UnifyInterconnectRedirect.class,redirect);
        }
    }
}
