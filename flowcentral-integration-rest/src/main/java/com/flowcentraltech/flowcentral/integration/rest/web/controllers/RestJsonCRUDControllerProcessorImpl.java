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

import java.util.Arrays;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.APIDef;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityUtils;
import com.flowcentraltech.flowcentral.integration.constants.RestEndpointConstants;
import com.flowcentraltech.flowcentral.integration.data.EndpointDef;
import com.flowcentraltech.flowcentral.integration.data.EndpointPathDef;
import com.flowcentraltech.flowcentral.integration.rest.constants.IntegrationRestModuleNameConstants;
import com.flowcentraltech.flowcentral.integration.rest.data.CountResult;
import com.flowcentraltech.flowcentral.integration.rest.data.CreateResult;
import com.flowcentraltech.flowcentral.integration.rest.data.DeleteResult;
import com.flowcentraltech.flowcentral.integration.rest.data.UpdateResult;
import com.flowcentraltech.flowcentral.system.data.CredentialDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.Parameters;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.DataUtils;
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
    private AppletUtilities appletUtilities;

    private EndpointDef endpointDef;

    private EndpointPathDef endpointPathDef;

    private Response badRequestResponse;

    private Response notFoundResponse;

    private Response unAuthResponse;

    @Override
    public void applySettings(EndpointDef endpointDef, EndpointPathDef endpointPathDef) throws UnifyException {
        this.endpointDef = endpointDef;
        this.endpointPathDef = endpointPathDef;
    }

    @Override
    public Response create(HttpRequestHeaders headers, Parameters parameters, String body) throws UnifyException {
        if (!isAuthorized(headers)) {
            return getUnauthResponse();
        }

        EntityItem instItem = getEntity(body);
        if (instItem.isWithErrors()) {
            return getValidationErrorsResponse(instItem.getErrors());
        }

        final Long id = (Long) appletUtilities.environment().create(instItem.getInst());
        return getResponse(HttpResponseConstants.CREATED,
                new CreateResult(instItem.getEntityClassDef().getEntityDef().getDescription(), id));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Response delete(HttpRequestHeaders headers, Parameters parameters) throws UnifyException {
        if (!isAuthorized(headers)) {
            return getUnauthResponse();
        }

        final APIDef apiDef = getAPIDef();
        final EntityClassDef entityClassDef = appletUtilities.getEntityClassDef(apiDef.getEntity());
        final Long id = parameters.getParam(Long.class, "id");
        int deleted = appletUtilities.environment().delete((Class<? extends Entity>) entityClassDef.getEntityClass(),
                id);
        if (deleted > 0) {
            return getResponse(HttpResponseConstants.OK,
                    new DeleteResult(entityClassDef.getEntityDef().getDescription(), id));
        }

        return getNotFoundResponse();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Response read(HttpRequestHeaders headers, Parameters parameters) throws UnifyException {
        if (!isAuthorized(headers)) {
            return getUnauthResponse();
        }

        final String op = parameters.isParam("_op") ? parameters.getParam(String.class, "_op") : "find";
        final APIDef apiDef = getAPIDef();
        final EntityClassDef entityClassDef = appletUtilities.getEntityClassDef(apiDef.getEntity());

        if ("count".equals(op)) {
            Query<? extends Entity> query = appletUtilities.application().queryOf(apiDef.getEntity());
            setCriteria(query, parameters);
            final int count = appletUtilities.environment().countAll(query);
            return getResponse(HttpResponseConstants.OK, new CountResult(entityClassDef.getEntityDef().getDescription(), count));
        }

        if ("find".equals(op)) {
            if (parameters.isParam("id")) {
                final Long id = parameters.getParam(Long.class, "id");
                final Entity srcInst = appletUtilities.environment()
                        .find((Class<? extends Entity>) entityClassDef.getEntityClass(), id);
                if (srcInst != null) {
                    return getResponse(entityClassDef.getJsonComposition(appletUtilities), HttpResponseConstants.OK, srcInst);
                }

                return getNotFoundResponse();
            }
            
            Query<? extends Entity> query = appletUtilities.application().queryOf(apiDef.getEntity());
            setCriteria(query, parameters);
            List<? extends Entity> list = appletUtilities.environment().listAll(query);
            return getResponse(entityClassDef.getJsonComposition(appletUtilities), HttpResponseConstants.OK, list);
        }
        
        return getBadRequestResponse();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Response update(HttpRequestHeaders headers, Parameters parameters, String body) throws UnifyException {
        if (!isAuthorized(headers)) {
            return getUnauthResponse();
        }

        EntityItem instItem = getEntity(body);
        if (instItem.isWithErrors()) {
            return getValidationErrorsResponse(instItem.getErrors());
        }

        final EntityClassDef entityClassDef = instItem.getEntityClassDef();
        final Long id = parameters.getParam(Long.class, "id");
        final Entity destInst = appletUtilities.environment()
                .find((Class<? extends Entity>) entityClassDef.getEntityClass(), id);
        if (destInst != null) {
            new BeanValueStore(destInst).copyWithExclusions(new BeanValueStore(instItem.getInst()),
                    ApplicationEntityUtils.getReservedFieldNames());
            appletUtilities.environment().updateByIdVersion(destInst);
            return getResponse(HttpResponseConstants.OK,
                    new UpdateResult(entityClassDef.getEntityDef().getDescription(), id));
        }

        return getNotFoundResponse();
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

    private class EntityItem {

        private AppletDef appletDef;

        private EntityClassDef entityClassDef;

        private Entity inst;

        private List<String> errors;

        public EntityItem(AppletDef appletDef, EntityClassDef entityClassDef, Entity inst, List<String> errors) {
            this.appletDef = appletDef;
            this.entityClassDef = entityClassDef;
            this.inst = inst;
            this.errors = errors;
        }

        public AppletDef getAppletDef() {
            return appletDef;
        }

        public EntityClassDef getEntityClassDef() {
            return entityClassDef;
        }

        public Entity getInst() {
            return inst;
        }

        public List<String> getErrors() {
            return errors;
        }

        public boolean isWithErrors() {
            return !DataUtils.isBlank(errors);
        }
    }

    private EntityItem getEntity(String body) throws UnifyException {
        final APIDef apiDef = getAPIDef();
        final AppletDef appletDef = appletUtilities.getAppletDef(apiDef.getApplet());
        final EntityClassDef entityClassDef = appletUtilities.getEntityClassDef(apiDef.getEntity());
        final Entity inst = (Entity) DataUtils.fromJsonString(entityClassDef.getJsonComposition(appletUtilities),
                entityClassDef.getEntityClass(), body);
        final List<String> errors = inst == null
                ? Arrays.asList(resolveApplicationMessage("$m{restjsoncrud.comtrollerprocessor.nomessagebody}"))
                : entityClassDef.validate(appletUtilities, inst);
        return new EntityItem(appletDef, entityClassDef, inst, errors);
    }

    private APIDef getAPIDef() throws UnifyException {
        return appletUtilities.application().getAPIDef(endpointPathDef.getPath());
    }

    private void setCriteria(Query<? extends Entity> query, Parameters parameters) throws UnifyException {
        // TODO Auto-generated method stub
        
        query.ignoreEmptyCriteria(true);
    }

    private Response getValidationErrorsResponse(List<String> errors) throws UnifyException {
        return getErrorResponse(HttpResponseConstants.BAD_REQUEST, "Validation Errors.", errors);
    }

    private Response getBadRequestResponse() throws UnifyException {
        if (badRequestResponse == null) {
            synchronized (this) {
                if (badRequestResponse == null) {
                    badRequestResponse = getErrorResponse(HttpResponseConstants.BAD_REQUEST, "Bad request.",
                            "The server cannot not process the request.");
                }
            }
        }

        return badRequestResponse;
    }

    private Response getNotFoundResponse() throws UnifyException {
        if (notFoundResponse == null) {
            synchronized (this) {
                if (notFoundResponse == null) {
                    notFoundResponse = getErrorResponse(HttpResponseConstants.NOT_FOUND, "Not found.",
                            "Resource not found.");
                }
            }
        }

        return notFoundResponse;
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
            final CredentialDef credentialDef = appletUtilities.system().getCredentialDef(credential);
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
