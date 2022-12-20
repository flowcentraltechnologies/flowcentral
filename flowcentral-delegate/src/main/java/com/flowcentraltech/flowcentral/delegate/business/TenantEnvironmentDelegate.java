/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.application.business.TenantProvider;
import com.flowcentraltech.flowcentral.connect.common.data.BaseResponse;
import com.flowcentraltech.flowcentral.connect.common.data.DataSourceRequest;
import com.flowcentraltech.flowcentral.system.entities.Tenant;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;

/**
 * Tenant environment delegate..
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(name = "tenant-environmentdelegate", description = "Tenant Environment Delegate")
public class TenantEnvironmentDelegate extends AbstractEnvironmentDelegate {

    @Configurable
    private TenantProvider tenantProvider;

    public final void setTenantProvider(TenantProvider tenantProvider) {
        this.tenantProvider = tenantProvider;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T find(Class<T> entityClass, Object id) throws UnifyException {
        return tenantProvider != null ? (T) tenantProvider.find((Long) id) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T find(Class<T> entityClass, Object id, Object versionNo) throws UnifyException {
        return tenantProvider != null ? (T) tenantProvider.find((Long) id, versionNo) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T find(Query<T> query) throws UnifyException {
        return tenantProvider != null ? (T) tenantProvider.find((Query<Tenant>) query) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T findLean(Class<T> entityClass, Object id) throws UnifyException {
        return tenantProvider != null ? (T) tenantProvider.findLean((Long) id) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T findLean(Class<T> entityClass, Object id, Object versionNo) throws UnifyException {
        return tenantProvider != null ? (T) tenantProvider.findLean((Long) id, versionNo) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T findLean(Query<T> query) throws UnifyException {
        return tenantProvider != null ? (T) tenantProvider.findLean((Query<Tenant>) query) : null;
    }

    @Override
    public <T extends Entity> T findConstraint(T record) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> List<T> findAll(Query<T> query) throws UnifyException {
        return tenantProvider != null ? (List<T>) tenantProvider.findAll((Query<Tenant>) query)
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

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T list(Class<T> entityClass, Object id) throws UnifyException {
        return tenantProvider != null ? (T) tenantProvider.list((Long) id) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T list(Class<T> entityClass, Object id, Object versionNo) throws UnifyException {
        return tenantProvider != null ? (T) tenantProvider.list((Long) id, versionNo) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T list(Query<T> query) throws UnifyException {
        return tenantProvider != null ? (T) tenantProvider.list((Query<Tenant>) query) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T listLean(Class<T> entityClass, Object id) throws UnifyException {
        return tenantProvider != null ? (T) tenantProvider.listLean((Long) id) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T listLean(Class<T> entityClass, Object id, Object versionNo) throws UnifyException {
        return tenantProvider != null ? (T) tenantProvider.listLean((Long) id, versionNo) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T listLean(Query<T> query) throws UnifyException {
        return tenantProvider != null ? (T) tenantProvider.listLean((Query<Tenant>) query) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> List<T> listAll(Query<T> query) throws UnifyException {
        return tenantProvider != null ? (List<T>) tenantProvider.listAll((Query<Tenant>) query)
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
    public <T, U extends Entity> List<T> valueList(Class<T> fieldClass, String fieldName, Query<U> query)
            throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T, U extends Entity> T value(Class<T> fieldClass, String fieldName, Query<U> query) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T, U extends Entity> T min(Class<T> fieldClass, String fieldName, Query<U> query) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T, U extends Entity> T max(Class<T> fieldClass, String fieldName, Query<U> query) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T, U extends Entity> Set<T> valueSet(Class<T> fieldClass, String fieldName, Query<U> query)
            throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T, U, V extends Entity> Map<T, U> valueMap(Class<T> keyClass, String keyName, Class<U> valueClass,
            String valueName, Query<V> query) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T, U, V extends Entity> Map<T, List<U>> valueListMap(Class<T> keyClass, String keyName, Class<U> valueClass,
            String valueName, Query<V> query) throws UnifyException {
        throw new UnsupportedOperationException();
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

    @SuppressWarnings("unchecked")
    @Override
    public int countAll(Query<? extends Entity> query) throws UnifyException {
        return tenantProvider != null ? tenantProvider.countAll((Query<Tenant>) query) : 0;
    }

    @Override
    public void executeProcedure(String operation, String... payload) throws UnifyException {
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
