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

package com.flowcentraltech.flowcentral.connect.springboot.controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowcentraltech.flowcentral.connect.common.data.BaseRequest;
import com.flowcentraltech.flowcentral.connect.common.data.DataSourceRequest;
import com.flowcentraltech.flowcentral.connect.common.data.JsonDataSourceResponse;
import com.flowcentraltech.flowcentral.connect.common.data.JsonProcedureResponse;
import com.flowcentraltech.flowcentral.connect.common.data.ProcedureRequest;

/**
 * Convenient abstract base class for interconnect redirects
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractSpringBootInterconnectRedirect implements SpringBootInterconnectRedirect {

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    private Map<String, String> dataSourceRequestRedirects;

    private Map<String, String> procedureRequestRedirects;

    public AbstractSpringBootInterconnectRedirect() {
        this.dataSourceRequestRedirects = new HashMap<String, String>();
        this.procedureRequestRedirects = new HashMap<String, String>();
    }

    @Override
    public JsonDataSourceResponse processDataSourceRequest(DataSourceRequest req) {
        String redirectUrl = dataSourceRequestRedirects.get(req.getEntity());
        if (redirectUrl != null) {
            String endpoint = redirectUrl + "/datasource";
            JsonDataSourceResponse resp = redirect(JsonDataSourceResponse.class, endpoint, req);
            if (resp == null) {
                resp = new JsonDataSourceResponse();
                resp.setErrorMsg("Redirection error");
            }
            
            return resp;
        }

        return null;
    }

    @Override
    public JsonProcedureResponse processDataSourceRequest(ProcedureRequest req) {
        String redirectUrl = procedureRequestRedirects.get(req.getOperation());
        if (redirectUrl != null) {
            String endpoint = redirectUrl + "/procedure";
            JsonProcedureResponse resp = redirect(JsonProcedureResponse.class, endpoint, req);
            if (resp == null) {
                resp = new JsonProcedureResponse();
                resp.setErrorMsg("Redirection error");
            }
            
            return resp;
        }

        return null;
    }

    @PostConstruct
    public void init() throws Exception {
        setupRedirects();
    }

    protected abstract void setupRedirects();
    
    protected void addDataSourceRequestRedirect(String redirectUrl, String... entities) {
        for (String entity : entities) {
            dataSourceRequestRedirects.put(entity, redirectUrl);
        }
    }

    protected void addProcedureRequestRedirect(String redirectUrl, String... operations) {
        for (String operation : operations) {
            procedureRequestRedirects.put(operation, redirectUrl);
        }
    }

    private <T extends JsonDataSourceResponse> T redirect(Class<T> responseClass, String endpoint, BaseRequest req) {
        T resp = null;
        StringBuilder response = new StringBuilder();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonReq = objectMapper.writeValueAsString(req);

            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            try (OutputStream out = conn.getOutputStream()) {
                out.write(jsonReq.getBytes("utf-8"));
                out.flush();
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            resp = objectMapper.readValue(response.toString(), responseClass);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Redirection error", e);
        }

        return resp;
    }
}
