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
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.connect.common.data.BaseResponse;
import com.flowcentraltech.flowcentral.connect.common.data.DataSourceRequest;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Mapped entity environment delegate.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(name = ApplicationModuleNameConstants.MAPPEDENTITY_ENVIRONMENT_DELEGATE,
        description = "Mapped Entity Environment Delegate")
public class MappedEntityEnvironmentDelegate extends AbstractEnvironmentDelegate {

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T find(Class<T> entityClass, Object id) throws UnifyException {
        return au().isMappingProviderPresent(entityClass) ? (T) au().getProvider(entityClass).find((Long) id) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T find(Class<T> entityClass, Object id, Object versionNo) throws UnifyException {
        return au().isMappingProviderPresent(entityClass)
                ? (T) au().getProvider(entityClass).find((Long) id, (long) versionNo)
                : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T find(Query<T> query) throws UnifyException {
        return au().isMappingProviderPresent(query) ? (T) au().getProvider(query).find(query) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T findLean(Class<T> entityClass, Object id) throws UnifyException {
        return au().isMappingProviderPresent(entityClass) ? (T) au().getProvider(entityClass).findLean((Long) id)
                : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T findLean(Class<T> entityClass, Object id, Object versionNo) throws UnifyException {
        return au().isMappingProviderPresent(entityClass)
                ? (T) au().getProvider(entityClass).findLean((Long) id, (long) versionNo)
                : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T findLean(Query<T> query) throws UnifyException {
        return au().isMappingProviderPresent(query) ? (T) au().getProvider(query).findLean(query) : null;
    }

    @Override
    public <T extends Entity> T findConstraint(T record) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> List<T> findAll(Query<T> query) throws UnifyException {
        return au().isMappingProviderPresent(query) ? (List<T>) au().getProvider(query).findAll(query)
                : Collections.emptyList();
    }

    @Override
    public <T extends Entity> List<T> findAllWithChildren(Query<T> query) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T, U extends Entity> Map<T, U> findAllMap(Class<T> keyClass, String keyName, Query<U> query)
            throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T, U extends Entity> Map<T, List<U>> findAllListMap(Class<T> keyClass, String keyName, Query<U> query)
            throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Entity> void findChildren(T record) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Entity> void findEditableChildren(T record) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Entity> void findReadOnlyChildren(T record) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T list(Class<T> entityClass, Object id) throws UnifyException {
        return au().isMappingProviderPresent(entityClass) ? (T) au().getProvider(entityClass).list((Long) id) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T list(Class<T> entityClass, Object id, Object versionNo) throws UnifyException {
        return au().isMappingProviderPresent(entityClass)
                ? (T) au().getProvider(entityClass).list((Long) id, (long) versionNo)
                : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T list(Query<T> query) throws UnifyException {
        return au().isMappingProviderPresent(query) ? (T) au().getProvider(query).list(query) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T listLean(Class<T> entityClass, Object id) throws UnifyException {
        return au().isMappingProviderPresent(entityClass) ? (T) au().getProvider(entityClass).listLean((Long) id)
                : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T listLean(Class<T> entityClass, Object id, Object versionNo) throws UnifyException {
        return au().isMappingProviderPresent(entityClass)
                ? (T) au().getProvider(entityClass).listLean((Long) id, (long) versionNo)
                : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T listLean(Query<T> query) throws UnifyException {
        return au().isMappingProviderPresent(query) ? (T) au().getProvider(query).listLean(query) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> List<T> listAll(Query<T> query) throws UnifyException {
        return au().isMappingProviderPresent(query) ? (List<T>) au().getProvider(query).listAll(query)
                : Collections.emptyList();
    }

    @Override
    public <T extends Entity> List<T> listAllWithChildren(Query<T> query) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T, U extends Entity> Map<T, U> listAllMap(Class<T> keyClass, String keyName, Query<U> query)
            throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T, U extends Entity> Map<T, List<U>> listAllListMap(Class<T> keyClass, String keyName, Query<U> query)
            throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Entity> void listChildren(T record) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Entity> void listEditableChildren(T record) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Entity> void listReadOnlyChildren(T record) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T, U extends Entity> List<T> valueList(Class<T> fieldClass, String fieldName, Query<U> query)
            throws UnifyException {
        return au().isMappingProviderPresent(query)
                ? (List<T>) au().getProvider(query).valueList(fieldClass, fieldName, query)
                : Collections.emptyList();
    }

    @Override
    public <T, U extends Entity> T value(Class<T> fieldClass, String fieldName, Query<U> query) throws UnifyException {
        return au().isMappingProviderPresent(query) ? (T) au().getProvider(query).value(fieldClass, fieldName, query)
                : DataUtils.convert(fieldClass, null);
    }

    @Override
    public <T, U extends Entity> T valueOptional(Class<T> fieldClass, String fieldName, Query<U> query) throws UnifyException {
        return au().isMappingProviderPresent(query) ? (T) au().getProvider(query).value(fieldClass, fieldName, query)
                : DataUtils.convert(fieldClass, null);
    }

    @Override
    public <T extends Number, U extends Entity> T min(Class<T> fieldClass, String fieldName, Query<U> query)
            throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Number, U extends Entity> T max(Class<T> fieldClass, String fieldName, Query<U> query)
            throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T, U extends Entity> Set<T> valueSet(Class<T> fieldClass, String fieldName, Query<U> query)
            throws UnifyException {
        return au().isMappingProviderPresent(query)
                ? (Set<T>) au().getProvider(query).valueSet(fieldClass, fieldName, query)
                : Collections.emptySet();
    }

    @Override
    public <T, U, V extends Entity> Map<T, U> valueMap(Class<T> keyClass, String keyName, Class<U> valueClass,
            String valueName, Query<V> query) throws UnifyException {
        return au().isMappingProviderPresent(query)
                ? (Map<T, U>) au().getProvider(query).valueMap(keyClass, keyName, valueClass, valueName, query)
                : Collections.emptyMap();
    }

    @Override
    public <T, U, V extends Entity> Map<T, List<U>> valueListMap(Class<T> keyClass, String keyName, Class<U> valueClass,
            String valueName, Query<V> query) throws UnifyException {
        return au().isMappingProviderPresent(query) ? (Map<T, List<U>>) au().getProvider(query).valueListMap(keyClass,
                keyName, valueClass, valueName, query) : Collections.emptyMap();
    }

    @Override
    public void populateListOnly(Entity record) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object create(Entity record) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int updateById(Entity record) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int updateByIdVersion(Entity record) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int updateByIdEditableChildren(Entity record) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int updateByIdVersionEditableChildren(Entity record) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int updateLeanById(Entity record) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int updateLeanByIdVersion(Entity record) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int updateById(Class<? extends Entity> entityClass, Object id, Update update) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int updateAll(Query<? extends Entity> query, Update update) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int deleteById(Entity record) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int deleteByIdVersion(Entity record) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(Class<? extends Entity> entityClass, Object id) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int deleteAll(Query<? extends Entity> query) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Entity> int countAll(Query<T> query) throws UnifyException {
        return au().isMappingProviderPresent(query) ? au().getProvider(query).countAll(query) : 0;
    }

    @Override
    public String[] executeProcedure(String operation, String... payload) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDataSourceName(String entityLongName) throws UnifyException {
        return null;
    }

    @Override
    protected BaseResponse sendToDelegateDatasourceService(DataSourceRequest req) throws UnifyException {
        throw new UnsupportedOperationException();
    }
}
