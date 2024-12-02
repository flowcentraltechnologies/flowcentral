/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.integration.rest.web.controllers;

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.data.APIDef;
import com.flowcentraltech.flowcentral.integration.constants.RestEndpointConstants;
import com.flowcentraltech.flowcentral.integration.data.EndpointDef;
import com.flowcentraltech.flowcentral.integration.data.EndpointPathDef;
import com.flowcentraltech.flowcentral.integration.rest.constants.IntegrationRestModuleNameConstants;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.flowcentraltech.flowcentral.system.data.CredentialDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.Parameters;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.AbstractHttpCRUDControllerProcessor;
import com.tcdng.unify.web.constant.HttpResponseConstants;
import com.tcdng.unify.web.data.Response;
import com.tcdng.unify.web.http.HttpRequestHeaders;

/**
 * REST JSON controller processor.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(IntegrationRestModuleNameConstants.RESTJSONCRUD_PROCESSOR)
public class RestJsonCRUDControllerProcessorImpl extends AbstractHttpCRUDControllerProcessor
        implements RestJsonCRUDControllerProcessor {

    @Configurable
    private ApplicationModuleService applicationModuleService;

    @Configurable
    private SystemModuleService systemModuleService;

    private EndpointDef endpointDef;

    private EndpointPathDef endpointPathDef;

    private Response unAuthResponse;

    @Override
    public void applySettings(EndpointDef endpointDef, EndpointPathDef endpointPathDef) throws UnifyException {
        this.endpointDef = endpointDef;
        this.endpointPathDef = endpointPathDef;
    }

    @Override
    public Response create(HttpRequestHeaders headers, Parameters parameters, String body) throws UnifyException {
        if (isAuthorized(headers)) {

        }

        return getUnauthResponse();
    }

    @Override
    public Response delete(HttpRequestHeaders headers, Parameters parameters) throws UnifyException {
        if (isAuthorized(headers)) {

        }

        return getUnauthResponse();
    }

    @Override
    public Response read(HttpRequestHeaders headers, Parameters parameters) throws UnifyException {
        if (isAuthorized(headers)) {

        }

        return getUnauthResponse();
    }

    @Override
    public Response update(HttpRequestHeaders headers, Parameters parameters, String body) throws UnifyException {
        if (isAuthorized(headers)) {

        }

        return getUnauthResponse();
    }

    @Override
    public boolean isSupportCreate() throws UnifyException {
        return getAPIDef().isSupportCreate();
    }

    @Override
    public boolean isSupportDelete() throws UnifyException {
        return getAPIDef().isSupportDelete();
    }

    @Override
    public boolean isSupportRead() throws UnifyException {
        return getAPIDef().isSupportRead();
    }

    @Override
    public boolean isSupportUpdate() throws UnifyException {
        return getAPIDef().isSupportUpdate();
    }

    private APIDef getAPIDef() throws UnifyException {
        return applicationModuleService.getAPIDef(endpointPathDef.getPath());
    }

    private Response getUnauthResponse() throws UnifyException {
        if (unAuthResponse == null) {
            synchronized (this) {
                if (unAuthResponse == null) {
                    unAuthResponse = getErrorResponse(HttpResponseConstants.UNAUTHORIZED, "Client unauthorized.",
                            "Client not unauthorized to access resource.");
                }
            }
        }

        return unAuthResponse;
    }

    private boolean isAuthorized(HttpRequestHeaders headers) throws UnifyException {
        final String credential = endpointDef.getParam(String.class, RestEndpointConstants.CREDENTIAL_NAME);
        if (!StringUtils.isBlank(credential)) {
            final CredentialDef credentialDef = systemModuleService.getCredentialDef(credential);
            final String auth = headers.getHeader("Authorization");
            if (StringUtils.isBlank(auth)) {
                return false;
            }

            String[] parts = auth.split(" ");
            final String _encoded = parts.length >= 2 ? parts[1] : parts[0];
            return _encoded.equals(credentialDef.getBase64Encoded());
        }

        return true;
    }
}
