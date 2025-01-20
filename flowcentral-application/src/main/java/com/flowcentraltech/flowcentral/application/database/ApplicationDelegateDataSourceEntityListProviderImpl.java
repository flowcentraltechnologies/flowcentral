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
package com.flowcentraltech.flowcentral.application.database;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.data.DelegateEntityInfo;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.database.AbstractDataSourceEntityListProvider;
import com.tcdng.unify.core.database.DataSourceEntityContext;

/**
 * Application delegate datasource entity list provider..
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(ApplicationModuleNameConstants.DELEGATE_ENTITYLIST_PROVIDER)
public class ApplicationDelegateDataSourceEntityListProviderImpl extends AbstractDataSourceEntityListProvider {

    @Configurable
    private ApplicationModuleService applicationModuleService;

    @Override
    public String getDataSourceByEntityAlias(String entityName) throws UnifyException {
        DelegateEntityInfo delegateEntityInfo = applicationModuleService.getDelegateEntity(entityName);
        return delegateEntityInfo.getDataSourceName();
    }

    @Override
    public List<String> getEntityAliasesByDataSource(String datasourceName) throws UnifyException {
        List<DelegateEntityInfo> delegateEntityInfos = applicationModuleService
                .getDelegateEntitiesByDataSource(datasourceName);
        return DelegateEntityInfo.getEntityAliases(delegateEntityInfos);
    }

    @Override
    public DataSourceEntityContext getDataSourceEntityContext(List<String> datasources) throws UnifyException {
        Map<String, List<Class<?>>> tableEnitiesByDataSource = new HashMap<String, List<Class<?>>>();
        Map<String, List<Class<? extends Entity>>> viewEnitiesByDataSource = new HashMap<String, List<Class<? extends Entity>>>();

        for (String datasource : datasources) {
            // Entities
            List<String> entityAliases = getEntityAliasesByDataSource(datasource);
            List<Class<?>> tableEntities = applicationModuleService.getDelegateEntities(entityAliases);

            // Views
            List<Class<? extends Entity>> viewEntities = Collections.emptyList();
            
            tableEnitiesByDataSource.put(datasource, tableEntities);
            viewEnitiesByDataSource.put(datasource, viewEntities);
        }

        return new DataSourceEntityContext(datasources, tableEnitiesByDataSource, viewEnitiesByDataSource);
    }

}
