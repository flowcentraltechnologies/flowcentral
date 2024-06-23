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
package com.flowcentraltech.flowcentral.application.business;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.connect.common.data.BaseResponse;
import com.flowcentraltech.flowcentral.connect.common.data.DataSourceRequest;
import com.flowcentraltech.flowcentral.connect.common.data.DelegateEntityListingDTO;
import com.flowcentraltech.flowcentral.connect.common.data.EntityDTO;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;

/**
 * Missing environment delegate.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(name = ApplicationModuleNameConstants.MISSING_ENVIRONMENT_DELEGATE, description = "Missing Environment Delegate")
public class MissingEnvironmentDelegate extends AbstractEnvironmentDelegate {

    @Override
    public <T extends Entity> T find(Class<T> entityClass, Object id) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Entity> T find(Class<T> entityClass, Object id, Object versionNo) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Entity> T find(Query<T> query) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Entity> T findLean(Class<T> entityClass, Object id) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Entity> T findLean(Class<T> entityClass, Object id, Object versionNo) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Entity> T findLean(Query<T> query) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Entity> T findConstraint(T record) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Entity> List<T> findAll(Query<T> query) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Entity> List<T> findAllWithChildren(Query<T> query) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T, U extends Entity> Map<T, U> findAllMap(Class<T> keyClass, String keyName, Query<U> query)
            throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T, U extends Entity> Map<T, List<U>> findAllListMap(Class<T> keyClass, String keyName, Query<U> query)
            throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Entity> void findChildren(T record) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Entity> void findEditableChildren(T record) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Entity> void findReadOnlyChildren(T record) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Entity> T list(Class<T> entityClass, Object id) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Entity> T list(Class<T> entityClass, Object id, Object versionNo) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Entity> T list(Query<T> query) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Entity> T listLean(Class<T> entityClass, Object id) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Entity> T listLean(Class<T> entityClass, Object id, Object versionNo) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Entity> T listLean(Query<T> query) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Entity> List<T> listAll(Query<T> query) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Entity> List<T> listAllWithChildren(Query<T> query) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T, U extends Entity> Map<T, U> listAllMap(Class<T> keyClass, String keyName, Query<U> query)
            throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T, U extends Entity> Map<T, List<U>> listAllListMap(Class<T> keyClass, String keyName, Query<U> query)
            throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Entity> void listChildren(T record) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Entity> void listEditableChildren(T record) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Entity> void listReadOnlyChildren(T record) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T, U extends Entity> List<T> valueList(Class<T> fieldClass, String fieldName, Query<U> query)
            throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T, U extends Entity> T value(Class<T> fieldClass, String fieldName, Query<U> query) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T, U extends Entity> Optional<T> valueOptional(Class<T> fieldClass, String fieldName, Query<U> query)
            throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Number, U extends Entity> T min(Class<T> fieldClass, String fieldName, Query<U> query) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Number, U extends Entity> T max(Class<T> fieldClass, String fieldName, Query<U> query) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T, U extends Entity> Set<T> valueSet(Class<T> fieldClass, String fieldName, Query<U> query)
            throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T, U, V extends Entity> Map<T, U> valueMap(Class<T> keyClass, String keyName, Class<U> valueClass,
            String valueName, Query<V> query) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T, U, V extends Entity> Map<T, List<U>> valueListMap(Class<T> keyClass, String keyName, Class<U> valueClass,
            String valueName, Query<V> query) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public void populateListOnly(Entity record) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public Object create(Entity record) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public int updateById(Entity record) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public int updateByIdVersion(Entity record) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public int updateByIdEditableChildren(Entity record) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public int updateByIdVersionEditableChildren(Entity record) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public int updateLeanById(Entity record) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public int updateLeanByIdVersion(Entity record) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public int updateById(Class<? extends Entity> entityClass, Object id, Update update) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public int updateAll(Query<? extends Entity> query, Update update) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public int deleteById(Entity record) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public int deleteByIdVersion(Entity record) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public int delete(Class<? extends Entity> entityClass, Object id) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public int deleteAll(Query<? extends Entity> query) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public <T extends Entity>  int countAll(Query<T> query) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public String[] executeProcedure(String operation, String... payload) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    public String getDataSourceByEntityAlias(String entityLongName) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    protected DelegateEntityListingDTO getDelegatedEntityList() throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    protected EntityDTO getDelegatedEntitySchema(String entity) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

    @Override
    protected BaseResponse sendToDelegateDatasourceService(DataSourceRequest req) throws UnifyException {
        throw new IllegalArgumentException("Environment delegate component is missing.");
    }

}
