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
package com.flowcentraltech.flowcentral.integration.rest.web.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.APIDef;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
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
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.OrderType;
import com.tcdng.unify.core.criterion.FilterConditionType;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.JsonObjectComposition;
import com.tcdng.unify.core.data.Parameters;
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
 * @since 4.1
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
    public Response count(HttpRequestHeaders headers, Parameters parameters) throws UnifyException {
        final APIDef apiDef = getAPIDef();
        Query<? extends Entity> query = appletUtilities.application().queryOf(apiDef.getEntity());
        final EntityClassDef entityClassDef = appletUtilities.getEntityClassDef(apiDef.getEntity());
        Response resp = setCriteria(entityClassDef.getEntityDef(), query, parameters);
        if (resp == null) {
            final int count = appletUtilities.environment().countAll(query);
            return getResponse(HttpResponseConstants.OK,
                    new CountResult(entityClassDef.getEntityDef().getDescription(), count));
        }

        return getBadRequestResponse(null);
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
    public Response delete(HttpRequestHeaders headers, Parameters parameters, Long resourceId) throws UnifyException {
        if (!isAuthorized(headers)) {
            return getUnauthResponse();
        }

        final APIDef apiDef = getAPIDef();
        final EntityClassDef entityClassDef = appletUtilities.getEntityClassDef(apiDef.getEntity());
        if (resourceId != null) {
            final int deleted = appletUtilities.environment()
                    .delete((Class<? extends Entity>) entityClassDef.getEntityClass(), resourceId);
            if (deleted > 0) {
                return getResponse(HttpResponseConstants.OK,
                        new DeleteResult(entityClassDef.getEntityDef().getDescription(), resourceId));
            } else {
                return getNotFoundResponse();
            }
        }

        Query<? extends Entity> query = appletUtilities.application().queryOf(apiDef.getEntity());
        Response resp = setCriteria(entityClassDef.getEntityDef(), query, parameters);
        if (resp == null) {
            query.ignoreEmptyCriteria(true);
            final int deleted = appletUtilities.environment().deleteAll(query);
            return getResponse(HttpResponseConstants.OK,
                    new DeleteResult(entityClassDef.getEntityDef().getDescription(), deleted));
        }

        return resp;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Response read(HttpRequestHeaders headers, Parameters parameters, Long resourceId) throws UnifyException {
        if (!isAuthorized(headers)) {
            return getUnauthResponse();
        }

        final APIDef apiDef = getAPIDef();
        final EntityClassDef entityClassDef = appletUtilities.getEntityClassDef(apiDef.getEntity());

        if (resourceId != null) {
            final Entity srcInst = appletUtilities.environment()
                    .find((Class<? extends Entity>) entityClassDef.getEntityClass(), resourceId);
            if (srcInst != null) {
                return getResponse(entityClassDef.getJsonComposition(appletUtilities), HttpResponseConstants.OK,
                        srcInst);
            }

            return getNotFoundResponse();
        }

        Query<? extends Entity> query = appletUtilities.application().queryOf(apiDef.getEntity());
        Response resp = setCriteria(entityClassDef.getEntityDef(), query, parameters);
        if (resp == null) {
            List<? extends Entity> list = appletUtilities.environment().listAllWithChildren(query);
            return getResponse(entityClassDef.getJsonComposition(appletUtilities), HttpResponseConstants.OK, list);
        }

        return resp;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Response update(HttpRequestHeaders headers, Parameters parameters, String body, Long resourceId)
            throws UnifyException {
        if (!isAuthorized(headers)) {
            return getUnauthResponse();
        }

        EntityItem instItem = getEntity(body);
        if (instItem.isWithErrors()) {
            return getValidationErrorsResponse(instItem.getErrors());
        }

        final EntityClassDef entityClassDef = instItem.getEntityClassDef();
        final Entity destInst = appletUtilities.environment()
                .find((Class<? extends Entity>) entityClassDef.getEntityClass(), resourceId);
        if (destInst != null) {
            new BeanValueStore(destInst).copyWithExclusions(new BeanValueStore(instItem.getInst()),
                    ApplicationEntityUtils.getReservedFieldNames());
            appletUtilities.environment().updateByIdVersion(destInst);
            return getResponse(HttpResponseConstants.OK,
                    new UpdateResult(entityClassDef.getEntityDef().getDescription(), resourceId));
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

        private EntityClassDef entityClassDef;

        private Entity inst;

        private List<String> errors;

        public EntityItem(EntityClassDef entityClassDef, Entity inst, List<String> errors) {
            this.entityClassDef = entityClassDef;
            this.inst = inst;
            this.errors = errors;
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
        final EntityClassDef entityClassDef = appletUtilities.getEntityClassDef(apiDef.getEntity());
        final Entity inst = (Entity) DataUtils.fromJsonString(entityClassDef.getJsonComposition(appletUtilities),
                entityClassDef.getEntityClass(), body);
        final List<String> errors = inst == null
                ? Arrays.asList(resolveApplicationMessage("$m{restjsoncrud.comtrollerprocessor.nomessagebody}"))
                : entityClassDef.validate(appletUtilities, inst);
        return new EntityItem(entityClassDef, inst, errors);
    }

    private APIDef getAPIDef() throws UnifyException {
        return appletUtilities.application().getAPIDef(endpointPathDef.getPath());
    }

    @SuppressWarnings("unchecked")
    private Response setCriteria(EntityDef entityDef, Query<? extends Entity> query, Parameters parameters)
            throws UnifyException {
        try {
            for (String name : parameters.getParamNames()) {
                switch (name) {
                    case "_offset": {
                        final int offset = DataUtils.convert(int.class, parameters.getParam("_offset"));
                        if (offset >= 0) {
                            query.setOffset(offset);
                        }
                    }
                        break;
                    case "_limit": {
                        final int limit = DataUtils.convert(int.class, parameters.getParam("_limit"));
                        if (limit > 0) {
                            query.setLimit(limit);
                        }
                    }
                        break;
                    case "_sort": {
                        final String[] _vals = DataUtils.convert(String.class, parameters.getParam("_sort")).split(",");
                        for (String _val : _vals) {
                            final int index = _val.lastIndexOf('_');
                            final String _order = index > 0 ? _val.substring(0, index) : _val;
                            final String _direc = index > 0 ? _val.substring(index + 1).toUpperCase() : "ASC";
                            query.addOrder(OrderType.fromCode(_direc), _order);
                        }
                    }
                        break;
                    default:
                        final int index = name.lastIndexOf('_');
                        final String fieldName = index > 0 ? name.substring(0, index) : name;
                        if (entityDef.isField(fieldName)) {
                            final String cond = index > 0 ? name.substring(index + 1).toUpperCase() : "EQ";
                            final FilterConditionType type = FilterConditionType.fromCode(cond);
                            if (type != null) {
                                Object val = parameters.getParam(name);
                                Object paramA = null;
                                Object paramB = null;
                                if (val != null) {
                                    if (type.isRange()) {
                                        String[] _val = String.valueOf(val).split(",");
                                        if (_val.length == 2) {
                                            paramA = _val[0];
                                            paramB = _val[1];
                                        }
                                    } else if (type.isAmongst()) {
                                        paramA = Arrays.asList(String.valueOf(val).split(","));
                                    } else {
                                        paramA = val;
                                    }

                                    EntityFieldDef entityFieldDef = entityDef.getFieldDef(fieldName);
                                    if (entityFieldDef.isDate() || entityFieldDef.isTimestamp()) {
                                        JsonObjectComposition jsonComposition = entityDef
                                                .getJsonComposition(appletUtilities);
                                        if (type.isRange()) {
                                            paramA = DataUtils.getDateValue(jsonComposition, fieldName,
                                                    (String) paramA);
                                            paramB = DataUtils.getDateValue(jsonComposition, fieldName,
                                                    (String) paramB);
                                        } else if (type.isAmongst()) {
                                            if (paramA != null) {
                                                List<Date> _paramA = new ArrayList<Date>();
                                                for (String _val : (List<String>) paramA) {
                                                    _paramA.add((Date) DataUtils.getDateValue(jsonComposition,
                                                            fieldName, _val));
                                                }
                                                paramA = _paramA;
                                            }
                                        } else {
                                            paramA = DataUtils.getDateValue(jsonComposition, fieldName,
                                                    (String) paramA);
                                        }
                                    }
                                }

                                query.addRestriction(type.createSimpleCriteria(fieldName, paramA, paramB));
                            } else {
                                return getBadRequestResponse("Unknown condition type [" + cond + "].");
                            }
                        }
                        break;
                }
            }

            query.ignoreEmptyCriteria(true);
        } catch (Exception e) {
            return getBadRequestResponse(e.getMessage());
        }

        return null;
    }

    private Response getValidationErrorsResponse(List<String> errors) throws UnifyException {
        return getErrorResponse(HttpResponseConstants.BAD_REQUEST, "Validation Errors.", errors);
    }

    private Response getBadRequestResponse(String message) throws UnifyException {
        if (badRequestResponse == null) {
            synchronized (this) {
                if (badRequestResponse == null) {
                    badRequestResponse = getErrorResponse(HttpResponseConstants.BAD_REQUEST, "Bad request.",
                            message != null ? message : "The server cannot not process the request.");
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
