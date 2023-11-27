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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.IOUtils;

/**
 * Convenient abstract base class for HTTP Post environment delegates.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractHttpPostEnvironmentDelegate extends AbstractJsonEnvironmentDelegate {

    protected enum ActionType {
        LIST_ENTITIES,
        GET_ENTITY,
        PROCEDURE,
        DATASOURCE
    }

    private final Map<ActionType, ActionPath> paths;

    public AbstractHttpPostEnvironmentDelegate(List<ActionPath> paths) {
        Map<ActionType, ActionPath> _paths = new HashMap<ActionType, ActionPath>();
        for (ActionPath path : paths) {
            _paths.put(path.getType(), path);
        }

        this.paths = Collections.unmodifiableMap(_paths);
    }

    @Override
    protected String listEntities(String jsonReq) throws UnifyException {
        final String endpoint = getEndpointNode() + paths.get(ActionType.LIST_ENTITIES).getPath();
        logDebug("Sending to delegate list entities service [{0}] with request [{1}]...", endpoint, jsonReq);
        return IOUtils.postJsonToEndpoint(endpoint, jsonReq);
    }

    @Override
    protected String getEntity(String jsonReq) throws UnifyException {
        final String endpoint = getEndpointNode() + paths.get(ActionType.GET_ENTITY).getPath();
        logDebug("Sending to delegateget entity service [{0}] with request [{1}]...", endpoint, jsonReq);
        return IOUtils.postJsonToEndpoint(endpoint, jsonReq);
    }

    @Override
    protected String sendToDelegateProcedureService(String jsonReq) throws UnifyException {
        final String endpoint = getEndpointNode() + paths.get(ActionType.PROCEDURE).getPath();
        logDebug("Sending to delegate procedure service [{0}] with request [{1}]...", endpoint, jsonReq);
        return IOUtils.postJsonToEndpoint(endpoint, jsonReq);
    }

    @Override
    protected String sendToDelegateDatasourceService(String jsonReq) throws UnifyException {
        final String endpoint = getEndpointNode() + paths.get(ActionType.DATASOURCE).getPath();
        logDebug("Sending to delegate data source service [{0}] with request [{1}]...", endpoint, jsonReq);
        return IOUtils.postJsonToEndpoint(endpoint, jsonReq);
    }

    protected abstract String getEndpointNode() throws UnifyException;

    protected static class ActionPath {

        private final ActionType type;

        private final String path;

        public ActionPath(ActionType type, String path) {
            this.type = type;
            this.path = path;
        }

        public ActionType getType() {
            return type;
        }

        public String getPath() {
            return path;
        }
    }
}
