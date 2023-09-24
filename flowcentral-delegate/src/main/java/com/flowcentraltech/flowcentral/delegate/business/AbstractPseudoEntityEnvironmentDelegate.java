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

import java.util.List;

import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.connect.common.data.BaseResponse;
import com.flowcentraltech.flowcentral.connect.common.data.DataSourceRequest;
import com.flowcentraltech.flowcentral.connect.common.data.PseudoDataSourceResponse;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;

/**
 * Convenient abstract base class for pseudo entities environment delegates.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractPseudoEntityEnvironmentDelegate<T extends Entity> extends AbstractEnvironmentDelegate {

    private static final Object[] EMPTY_PAYLOAD = new Object[0];

    @Configurable
    private EnvironmentService environmentService;
    
    public final void setEnvironmentService(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    @Override
    public String getDataSourceByEntityAlias(String entityLongName) throws UnifyException {
        return "application-datasource";
    }

    @Override
    public String[] executeProcedure(String operation, String... payload) throws UnifyException {
        // TODO Auto-generated method stub
        return null;
    }

    protected final EnvironmentService environment() {
        return environmentService;
    }

    @Override
    protected BaseResponse sendToDelegateDatasourceService(DataSourceRequest req) throws UnifyException {
        Object[] payload = EMPTY_PAYLOAD;
        switch (req.getOperation()) {
            case COUNT_ALL: {
                long count = countAll(req);
                payload = new Object[] { count };
            }
                break;
            case CREATE:
                break;
            case DELETE:
                break;
            case DELETE_ALL:
                break;
            case FIND:
                break;
            case FIND_ALL: {
                List<T> list = findAll(req);
                payload = list.toArray(new Object[list.size()]);
            }
                break;
            case FIND_LEAN:
                break;
            case LIST:
                break;
            case LIST_ALL: {
                List<T> list = listAll(req);
                payload = list.toArray(new Object[list.size()]);
            }
                break;
            case LIST_LEAN:
                break;
            case UPDATE:
                break;
            case UPDATE_EDITABLE_CHILD:
                break;
            case UPDATE_LEAN:
                break;
            case UPDATE_ALL:
                break;
            case VALUE:
                break;
            case VALUE_LIST:
                break;
            case VALUE_SET:
                break;
            default:
                break;

        }

        PseudoDataSourceResponse resp = new PseudoDataSourceResponse();
        resp.setPayload(payload);
        return resp;
    }

    protected abstract long countAll(DataSourceRequest req) throws UnifyException;

    protected abstract List<T> findAll(DataSourceRequest req) throws UnifyException;

    protected abstract List<T> listAll(DataSourceRequest req) throws UnifyException;

    protected Query<? extends Entity> getQuery(String entity, DataSourceRequest req) throws UnifyException {
        Query<? extends Entity> query = utilities().decodeDelegateQuery(entity, req.getQuery(), req.getOrder());
        query.setOffset(req.getOffset());
        query.setLimit(req.getLimit());
        return query;
    }

}
