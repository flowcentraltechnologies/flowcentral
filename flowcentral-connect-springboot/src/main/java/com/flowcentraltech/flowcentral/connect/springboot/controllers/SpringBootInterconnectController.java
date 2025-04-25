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
package com.flowcentraltech.flowcentral.connect.springboot.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import com.flowcentraltech.flowcentral.connect.springboot.service.SpringBootInterconnectService;

/**
 * Flow central spring boot interconnect controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@RestController
@RequestMapping(FlowCentralInterconnectConstants.INTERCONNECT_CONTROLLER)
public class SpringBootInterconnectController {

    private static final Logger LOGGER = Logger.getLogger(SpringBootInterconnectController.class.getName());

    private final SpringBootInterconnectService springBootInterconnectService;

    private final ApplicationContext context;

    private SpringBootInterconnectRedirect springBootInterconnectRedirect;

    @Autowired
    public SpringBootInterconnectController(SpringBootInterconnectService springBootInterconnectService,
            ApplicationContext context) {
        this.springBootInterconnectService = springBootInterconnectService;
        this.context = context;
    }

    @PostConstruct
    public void init() throws Exception {
        String redirect = springBootInterconnectService.getRedirect();
        if (redirect != null) {
            springBootInterconnectRedirect = context.getBean(redirect, SpringBootInterconnectRedirect.class);
        }
    }

    @PostMapping(path = "/listEntities")
    public EntityListingResponse listEntities(@RequestBody EntityListingRequest req) throws Exception {
        EntityListingResponse result = springBootInterconnectService.listEntities(req);
        EntityListingResponse resp = springBootInterconnectRedirect != null
                ? springBootInterconnectRedirect.listEntities(req)
                : null;
        result.merge(resp);
        return result;
    }

    @PostMapping(path = "/detectEntity")
    public DetectEntityResponse detectEntity(@RequestBody DetectEntityRequest req) throws Exception {
        DetectEntityResponse resp = null;
        try {
            resp = springBootInterconnectService.detectEntity(req);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            LOGGER.log(Level.SEVERE, errorMessage, e);
            resp = new DetectEntityResponse();
            resp.setErrorCode(DataSourceErrorCodeConstants.PROVIDER_SERVICE_EXCEPTION);
            resp.setErrorMsg(errorMessage);
        }
        
        if (resp == null || resp.error() || !resp.isPresent()) {
            resp = springBootInterconnectRedirect != null
                    ? springBootInterconnectRedirect.detectEntity(req)
                    : null;
        }

        return resp;
    }

    @PostMapping(path = "/getEntity")
    public GetEntityResponse getEntity(@RequestBody GetEntityRequest req) throws Exception {
        GetEntityResponse resp = null;
        try {
            resp = springBootInterconnectService.getEntity(req);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            LOGGER.log(Level.SEVERE, errorMessage, e);
            resp = new GetEntityResponse();
            resp.setErrorCode(DataSourceErrorCodeConstants.PROVIDER_SERVICE_EXCEPTION);
            resp.setErrorMsg(errorMessage);
        }
        
        if (resp == null || resp.error() || !resp.present()) {
            resp = springBootInterconnectRedirect != null
                    ? springBootInterconnectRedirect.getEntity(req)
                    : null;
        }

        return resp;
    }

    @PostMapping(path = "/dataSource")
    public JsonDataSourceResponse processDataSourceRequest(@RequestBody DataSourceRequest req) {
        JsonDataSourceResponse resp = null;
        try {
            resp = springBootInterconnectService.processDataSourceRequest(req);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            LOGGER.log(Level.SEVERE, errorMessage, e);
            resp = new JsonDataSourceResponse();
            resp.setErrorCode(DataSourceErrorCodeConstants.PROVIDER_SERVICE_EXCEPTION);
            resp.setErrorMsg(errorMessage);
        }
        
        if (resp == null) {
            resp = springBootInterconnectRedirect != null
                    ? springBootInterconnectRedirect.processDataSourceRequest(req)
                    : null;
        }

        return resp;
    }

    @PostMapping(path = "/procedure")
    public JsonProcedureResponse procedureRequest(@RequestBody ProcedureRequest req) {
        JsonProcedureResponse resp = null;
        try {
            resp = springBootInterconnectService.executeProcedureRequest(req);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            LOGGER.log(Level.SEVERE, errorMessage, e);
            resp = new JsonProcedureResponse();
            resp.setErrorCode(DataSourceErrorCodeConstants.PROVIDER_SERVICE_EXCEPTION);
            resp.setErrorMsg(errorMessage);
        }
        
        if (resp == null) {
            resp = springBootInterconnectRedirect != null
                    ? springBootInterconnectRedirect.executeProcedureRequest(req)
                    : null;
        }

        return resp;
    }
}
