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

package com.flowcentraltech.flowcentral.connect.springboot.controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowcentraltech.flowcentral.connect.common.constants.FlowCentralInterconnectConstants;
import com.flowcentraltech.flowcentral.connect.common.data.BaseRequest;
import com.flowcentraltech.flowcentral.connect.common.data.BaseResponse;
import com.flowcentraltech.flowcentral.connect.common.data.DataSourceRequest;
import com.flowcentraltech.flowcentral.connect.common.data.DetectEntityRequest;
import com.flowcentraltech.flowcentral.connect.common.data.DetectEntityResponse;
import com.flowcentraltech.flowcentral.connect.common.data.EntityListingDTO;
import com.flowcentraltech.flowcentral.connect.common.data.EntityListingRequest;
import com.flowcentraltech.flowcentral.connect.common.data.EntityListingResponse;
import com.flowcentraltech.flowcentral.connect.common.data.GetEntityRequest;
import com.flowcentraltech.flowcentral.connect.common.data.GetEntityResponse;
import com.flowcentraltech.flowcentral.connect.common.data.JsonDataSourceResponse;
import com.flowcentraltech.flowcentral.connect.common.data.JsonProcedureResponse;
import com.flowcentraltech.flowcentral.connect.common.data.ProcedureRequest;
import com.flowcentraltech.flowcentral.connect.common.data.RedirectErrorDTO;

/**
 * Convenient abstract base class for interconnect redirects
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractSpringBootInterconnectRedirect implements SpringBootInterconnectRedirect {

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    private final List<String> redirectUrls;

    private final Map<String, Redirect> entityRedirects;

    private final Map<String, String> procedureRequestRedirects;

    public AbstractSpringBootInterconnectRedirect() {
        this.redirectUrls = new ArrayList<String>();
        this.entityRedirects = new HashMap<String, Redirect>();
        this.procedureRequestRedirects = new HashMap<String, String>();
    }

    @Override
    public EntityListingResponse listEntities(EntityListingRequest req) {
        List<EntityListingDTO> listings = new ArrayList<EntityListingDTO>();
        List<RedirectErrorDTO> redirectErrors = new ArrayList<RedirectErrorDTO>();
        for (String redirectUrl : redirectUrls) {
            String endpoint = redirectUrl + "/listEntities";
            EntityListingResponse resp = redirect(EntityListingResponse.class, endpoint, req);
            if (resp == null) {
                redirectErrors.add(new RedirectErrorDTO(redirectUrl, null, "Redirection error"));
            } else if (resp.error()) {
                redirectErrors.add(new RedirectErrorDTO(redirectUrl, resp.getErrorCode(), resp.getErrorMsg()));
            } else {
                if (resp.getListings() != null) {
                    for (EntityListingDTO entityListingDTO : resp.getListings()) {
                        if (entityListingDTO.getRedirect() != null) {
                            entityListingDTO.setRedirect(redirectUrl);
                        }

                        listings.add(entityListingDTO);
                    }
                }

                if (resp.getRedirectErrors() != null) {
                    redirectErrors.addAll(resp.getRedirectErrors());
                }
            }
        }

        return new EntityListingResponse(redirectErrors, listings);
    }

    @Override
    public DetectEntityResponse detectEntity(DetectEntityRequest req) {
        return processEntityRequest(DetectEntityResponse.class, req, "/detectEntity");
    }

    @Override
    public GetEntityResponse getEntity(GetEntityRequest req) {
        return processEntityRequest(GetEntityResponse.class, req, "/getEntity");
    }

    @Override
    public JsonDataSourceResponse processDataSourceRequest(DataSourceRequest req) {
        return processEntityRequest(JsonDataSourceResponse.class, req, "/dataSource");
    }

    @Override
    public JsonProcedureResponse executeProcedureRequest(ProcedureRequest req) {
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
        List<String> _redirectNodes = setupRedirects();
        if (_redirectNodes != null && !_redirectNodes.isEmpty()) {
            for (String redirectNode : _redirectNodes) {
                redirectUrls.add(redirectNode + FlowCentralInterconnectConstants.INTERCONNECT_CONTROLLER);
            }
        }
    }

    protected abstract List<String> setupRedirects();

    protected void addProcedureRequestRedirect(String redirectNode, String... operations) {
        for (String operation : operations) {
            procedureRequestRedirects.put(operation,
                    redirectNode + FlowCentralInterconnectConstants.INTERCONNECT_CONTROLLER);
        }
    }

    private <T extends BaseResponse, U extends BaseRequest> T processEntityRequest(Class<T> respClass, U req,
            String actionPath) {
        final Redirect redirect = getEntityRedirect(req.getEntity());
        if (redirect != null) {
            final String endpoint = redirect.getRedirectUrl() + actionPath;
            T resp = redirect(respClass, endpoint, req);
            if (resp == null) {
                try {
                    resp = respClass.newInstance();
                    resp.setErrorMsg("Redirection error");
                } catch (Exception e) {
                }
            }

            if (resp != null && resp.getRedirect() == null) {
                resp.setRedirect(redirect.getRedirectUrl());
            }

            return resp;
        }

        return null;
    }

    private Redirect getEntityRedirect(String entity) {
        Redirect redirect = entityRedirects.get(entity);
        if (redirect == null) {
            synchronized (this) {
                redirect = entityRedirects.get(entity);
                if (redirect == null) {
                    // Scan
                    for (String redirectUrl : redirectUrls) {
                        final String endpoint = redirectUrl + "/detectEntity";
                        DetectEntityResponse resp = redirect(DetectEntityResponse.class, endpoint,
                                new DetectEntityRequest(entity));
                        if (resp != null && !resp.error() && resp.isPresent()) {
                            redirect = new Redirect(redirectUrl);
                            entityRedirects.put(entity, redirect);
                            break;
                        }
                    }
                }
            }
        }

        return redirect;
    }

    private class Redirect {

        private final String redirectUrl;

        public Redirect(String redirectUrl) {
            this.redirectUrl = redirectUrl;
        }

        public String getRedirectUrl() {
            return redirectUrl;
        }

    }

    private <T extends BaseResponse> T redirect(Class<T> responseClass, String endpoint, BaseRequest req) {
        T resp = null;
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

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            if (response.length() > 0) {
                resp = objectMapper.readValue(response.toString(), responseClass);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Redirection error", e);
        }

        return resp;
    }
}
