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
package com.flowcentraltech.flowcentral.delegate.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.common.business.DirectEnvironmentDelegate;
import com.flowcentraltech.flowcentral.connect.common.data.BaseResponse;
import com.flowcentraltech.flowcentral.connect.common.data.DataSourceRequest;
import com.tcdng.unify.common.data.DelegateEntityListingDTO;
import com.tcdng.unify.common.data.EntityDTO;
import com.tcdng.unify.common.data.EntityListingDTO;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.DataSource;
import com.tcdng.unify.core.database.sql.SqlDataSource;
import com.tcdng.unify.core.database.sql.SqlEntityInfo;

/**
 * Convenient abstract base class for direct environment delegates.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractDirectEnvironmentDelegate extends AbstractSynchronizableEnvironmentDelegate
        implements DirectEnvironmentDelegate {

    private final String dataSourceName;

    private DelegateEntityListingDTO delegateEntityListingDTO;

    private Map<String, EntityDTO> entities;

    public AbstractDirectEnvironmentDelegate(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    @Override
    public final boolean isDirect() {
        return true;
    }

    @Override
    public String getDataSourceName() throws UnifyException {
        return dataSourceName;
    }

    @Override
    public String getDataSourceByEntityAlias(String entityLongName) throws UnifyException {
        return dataSourceName;
    }

    @Override
    public String[] executeProcedure(String operation, String... payload) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected DelegateEntityListingDTO getDelegatedEntityList() throws UnifyException {
        if (delegateEntityListingDTO == null) {
            synchronized (this) {
                if (delegateEntityListingDTO == null) {
                    try {
                        List<EntityListingDTO> listings = new ArrayList<EntityListingDTO>();
                        for (SqlEntityInfo sqlEntityInfo : ((SqlDataSource) getDataSource()).getSqlEntityInfos()) {
                            if (sqlEntityInfo.isWithEntityDTO()) {
                                listings.add(new EntityListingDTO(sqlEntityInfo.getEntityDTO().getName()));
                            }
                        }

                        delegateEntityListingDTO = new DelegateEntityListingDTO(Collections.unmodifiableList(listings),
                                Collections.emptyList());
                    } catch (Exception e) {
                        throwOperationErrorException(e);
                    }
                }
            }
        }

        return delegateEntityListingDTO;
    }

    @Override
    protected EntityDTO getDelegatedEntitySchema(String entity) throws UnifyException {
        if (entities == null) {
            synchronized (this) {
                if (entities == null) {
                    try {
                        entities = new HashMap<String, EntityDTO>();
                        for (SqlEntityInfo sqlEntityInfo : ((SqlDataSource) getDataSource()).getSqlEntityInfos()) {
                            if (sqlEntityInfo.isWithEntityDTO()) {
                                EntityDTO entityDTO = sqlEntityInfo.getEntityDTO();
                                entities.put(entityDTO.getName(), entityDTO);
                            }
                        }

                        entities = Collections.unmodifiableMap(entities);
                    } catch (Exception e) {
                        throwOperationErrorException(e);
                    }
                }
            }
        }

        return entities.get(entity);
    }

    @Override
    protected BaseResponse sendToDelegateDatasourceService(DataSourceRequest req) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DataSource getDataSource() throws UnifyException {
        return au().environment().getDatabase(dataSourceName).getDataSource();
    }

}
