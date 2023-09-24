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

import com.flowcentraltech.flowcentral.connect.common.data.BaseResponse;
import com.flowcentraltech.flowcentral.connect.common.data.DataSourceRequest;
import com.flowcentraltech.flowcentral.connect.common.data.EntityDTO;
import com.flowcentraltech.flowcentral.delegate.data.DelegateEntityListingDTO;
import com.tcdng.unify.core.UnifyException;

/**
 * Convenient abstract base class for direct environment delegates.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractDirectEnvironmentDelegate extends AbstractEnvironmentDelegate {

    private final String dataSourceName;
    
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
        return null;
    }

    @Override
    protected EntityDTO getDelegatedEntitySchema(String entity) throws UnifyException {
        return null;
    }

    @Override
    protected BaseResponse sendToDelegateDatasourceService(DataSourceRequest req) throws UnifyException {
        throw new UnsupportedOperationException();
    }

}
