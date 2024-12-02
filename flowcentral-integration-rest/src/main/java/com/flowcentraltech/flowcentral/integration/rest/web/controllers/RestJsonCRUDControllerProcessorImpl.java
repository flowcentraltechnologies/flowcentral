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

import java.util.Map;

import com.flowcentraltech.flowcentral.integration.data.EndpointDef;
import com.flowcentraltech.flowcentral.integration.data.EndpointPathDef;
import com.flowcentraltech.flowcentral.integration.rest.constants.IntegrationRestModuleNameConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.web.AbstractHttpCRUDControllerProcessor;
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

    @Override
    public void applySettings(EndpointDef endpointDef, EndpointPathDef endpointPathDef) throws UnifyException {
        // TODO Auto-generated method stub

    }

    @Override
    public Response create(HttpRequestHeaders arg0, Map<String, Object> arg1, String arg2) throws UnifyException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response delete(HttpRequestHeaders arg0, Map<String, Object> arg1) throws UnifyException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response read(HttpRequestHeaders arg0, Map<String, Object> arg1) throws UnifyException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response update(HttpRequestHeaders arg0, Map<String, Object> arg1, String arg2) throws UnifyException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isSupportCreate() throws UnifyException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSupportDelete() throws UnifyException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSupportRead() throws UnifyException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSupportUpdate() throws UnifyException {
        // TODO Auto-generated method stub
        return false;
    }

}
