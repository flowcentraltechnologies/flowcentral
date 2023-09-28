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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldSchema;
import com.flowcentraltech.flowcentral.application.data.EntitySchema;
import com.flowcentraltech.flowcentral.application.entities.AppEntityQuery;
import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.flowcentraltech.flowcentral.common.business.EnvironmentDelegate;
import com.flowcentraltech.flowcentral.common.business.EnvironmentDelegateUtilities;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseVersionEntity;
import com.flowcentraltech.flowcentral.configuration.constants.EntityBaseType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.connect.common.constants.DataSourceOperation;
import com.flowcentraltech.flowcentral.connect.common.data.BaseResponse;
import com.flowcentraltech.flowcentral.connect.common.data.DataSourceRequest;
import com.flowcentraltech.flowcentral.connect.common.data.EntityDTO;
import com.flowcentraltech.flowcentral.connect.common.data.EntityFieldDTO;
import com.flowcentraltech.flowcentral.connect.common.data.EntityListingDTO;
import com.flowcentraltech.flowcentral.connect.common.data.JsonDataSourceResponse;
import com.flowcentraltech.flowcentral.connect.common.data.PseudoDataSourceResponse;
import com.flowcentraltech.flowcentral.connect.common.data.RedirectErrorDTO;
import com.flowcentraltech.flowcentral.delegate.data.DelegateEntityListingDTO;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.core.criterion.Aggregate;
import com.tcdng.unify.core.criterion.AggregateFunction;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.Aggregation;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.GroupAggregation;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.CallableProc;
import com.tcdng.unify.core.database.DataSource;
import com.tcdng.unify.core.database.DataSourceEntityListProvider;
import com.tcdng.unify.core.database.DatabaseSession;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient abstract base class for environment delegates.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractEnvironmentDelegate extends AbstractFlowCentralComponent implements EnvironmentDelegate {

    @Configurable
    private AppletUtilities au;

    @Configurable(ApplicationModuleNameConstants.DELEGATE_ENTITYLIST_PROVIDER)
    private DataSourceEntityListProvider dataSourceEntityListProvider;

    @Override
    public boolean isDirect() {
        return false;
    }

    public final void setAu(AppletUtilities au) {
        this.au = au;
    }

    public final void setDataSourceEntityListProvider(DataSourceEntityListProvider dataSourceEntityListProvider) {
        this.dataSourceEntityListProvider = dataSourceEntityListProvider;
    }

    @Override
    public void delegateCreateSynchronization(TaskMonitor taskMonitor) throws UnifyException {
        logInfo(taskMonitor, "Performing create synchronization of entities for delegate [0]...", getName());
        DelegateEntityListingDTO delegateEntityListingDTO = getDelegatedEntityList();
        if (delegateEntityListingDTO != null) {
            if (!DataUtils.isBlank(delegateEntityListingDTO.getRedirectErrors())) {
                for (RedirectErrorDTO redirectErrorDTO : delegateEntityListingDTO.getRedirectErrors()) {
                    logWarn(taskMonitor, "Unable to synchronize with redirect [{0}]. Error [{1}].",
                            redirectErrorDTO.getRedirect(), redirectErrorDTO.getErrorMsg());
                }
            }

            if (!DataUtils.isBlank(delegateEntityListingDTO.getListings())) {
                final String delegate = getName();
                logInfo(taskMonitor, "[{0}] entities detected...", delegateEntityListingDTO.getListings().size());
                final Set<String> existing = new HashSet<String>(
                        au.getApplicationEntitiesLongNames(new AppEntityQuery().ignoreEmptyCriteria(true)));
                for (EntityListingDTO entityListingDTO : delegateEntityListingDTO.getListings()) {
                    final String entity = entityListingDTO.getEntity();
                    if (!existing.contains(entity)) {
                        logInfo(taskMonitor, "Fetching schema information for [{0}]...", entity);
                        EntityDTO entityDTO = getDelegatedEntitySchema(entity);
                        if (entityDTO != null) {
                            logInfo(taskMonitor, "Creating entity schema...");
                            List<EntityFieldSchema> fields = new ArrayList<EntityFieldSchema>();
                            for (EntityFieldDTO entityFieldDTO : entityDTO.getFields()) {
                                EntityFieldDataType dataType = null; // TODO
                                fields.add(new EntityFieldSchema(dataType, entityFieldDTO.getName(),
                                        entityFieldDTO.getDescription(), entityFieldDTO.getColumn(),
                                        entityFieldDTO.getReferences(), entityFieldDTO.getScale(),
                                        entityFieldDTO.getPrecision(), entityFieldDTO.getLength()));
                            }

                            EntityBaseType baseType = null; // TODO 
                            EntitySchema entitySchema = new EntitySchema(baseType, delegate, entityDTO.getDataSourceAlias(),
                                    entity, entityDTO.getName(), entityDTO.getDescription(), entityDTO.getTableName(),
                                    fields);
                            au.createEntitySchema(entitySchema);
                            logInfo(taskMonitor, "Entity schema create for [{0}] completed...", entity);
                        } else {
                            logWarn(taskMonitor, "Could no retreive schema information for entity [{0}]...", entity);
                        }
                    }
                }
            } else {
                logInfo(taskMonitor, "No entity listings found for this delegate.");
            }

            logInfo(taskMonitor, "Delegate synchronization completed.");
        } else {
            logInfo(taskMonitor, "Could not retrieve synchronization information.");
        }
    }

    @Override
    public void delegateUpdateSynchronization(TaskMonitor taskMonitor) throws UnifyException {
        logInfo(taskMonitor, "Performing update synchronization of entities for delegate [0]...", getName());
        DelegateEntityListingDTO delegateEntityListingDTO = getDelegatedEntityList();
        if (delegateEntityListingDTO != null) {
            if (!DataUtils.isBlank(delegateEntityListingDTO.getRedirectErrors())) {
                for (RedirectErrorDTO redirectErrorDTO : delegateEntityListingDTO.getRedirectErrors()) {
                    logWarn(taskMonitor, "Unable to synchronize with redirect [{0}]. Error [{1}].",
                            redirectErrorDTO.getRedirect(), redirectErrorDTO.getErrorMsg());
                }
            }

            if (!DataUtils.isBlank(delegateEntityListingDTO.getListings())) {
                final String delegate = getName();
                logInfo(taskMonitor, "[{0}] entities detected...", delegateEntityListingDTO.getListings().size());
                final Set<String> existing = new HashSet<String>(
                        au.getApplicationEntitiesLongNames(new AppEntityQuery().ignoreEmptyCriteria(true)));
                for (EntityListingDTO entityListingDTO : delegateEntityListingDTO.getListings()) {
                    final String entity = entityListingDTO.getEntity();
                    if (!existing.contains(entity)) {
                        logInfo(taskMonitor, "Fetching schema information for [{0}]...", entity);
                        EntityDTO entityDTO = getDelegatedEntitySchema(entity);
                        if (entityDTO != null) {
                            logInfo(taskMonitor, "Updating entity schema...");
                            List<EntityFieldSchema> fields = new ArrayList<EntityFieldSchema>();
                            for (EntityFieldDTO entityFieldDTO : entityDTO.getFields()) {
                                EntityFieldDataType dataType = null; // TODO
                                fields.add(new EntityFieldSchema(dataType, entityFieldDTO.getName(),
                                        entityFieldDTO.getDescription(), entityFieldDTO.getColumn(),
                                        entityFieldDTO.getReferences(), entityFieldDTO.getScale(),
                                        entityFieldDTO.getPrecision(), entityFieldDTO.getLength()));
                            }

                            EntityBaseType baseType = null; // TODO 
                            EntitySchema entitySchema = new EntitySchema(baseType, delegate, entityDTO.getDataSourceAlias(),
                                    entity, entityDTO.getName(), entityDTO.getDescription(), entityDTO.getTableName(),
                                    fields);
                            au.updateEntitySchema(entitySchema);
                            logInfo(taskMonitor, "Entity schema update for [{0}] completed...", entity);
                        } else {
                            logWarn(taskMonitor, "Could no retreive schema information for entity [{0}]...", entity);
                        }
                    }
                }
            } else {
                logInfo(taskMonitor, "No entity listings found for this delegate.");
            }

            logInfo(taskMonitor, "Delegate synchronization completed.");
        } else {
            logInfo(taskMonitor, "Could not retrieve synchronization information.");
        }
    }

    protected abstract DelegateEntityListingDTO getDelegatedEntityList() throws UnifyException;

    protected abstract EntityDTO getDelegatedEntitySchema(String entity) throws UnifyException;

    @Override
    public boolean isReadOnly() throws UnifyException {
        return true;
    }

    @Override
    public final DataSource getDataSource() throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDataSourceName() throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public final List<String> getEntityAliasesByDataSource(String dataSourceName) throws UnifyException {
        return dataSourceEntityListProvider.getEntityAliasesByDataSource(dataSourceName);
    }

    @Override
    public String getDataSourceByEntityAlias(String entityLongName) throws UnifyException {
        return dataSourceEntityListProvider.getDataSourceByEntityAlias(entityLongName);
    }

    @Override
    public final void joinTransaction() throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public final DatabaseSession createDatabaseSession() throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Entity> T find(Class<T> entityClass, Object id) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.FIND,
                au.delegateUtilities().encodeDelegateObjectId(id), null);
        return singleEntityResultOperation(entityClass, req);
    }

    @Override
    public <T extends Entity> T find(Class<T> entityClass, Object id, Object versionNo) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.FIND,
                au.delegateUtilities().encodeDelegateObjectId(id),
                au.delegateUtilities().encodeDelegateVersionNo(versionNo));
        return singleEntityResultOperation(entityClass, req);
    }

    @Override
    public <T extends Entity> T find(Query<T> query) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.FIND);
        setQueryDetails(req, query);
        return singleEntityResultOperation(query.getEntityClass(), req);
    }

    @Override
    public <T extends Entity> T findLean(Class<T> entityClass, Object id) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.FIND_LEAN,
                au.delegateUtilities().encodeDelegateObjectId(id), null);
        return singleEntityResultOperation(entityClass, req);
    }

    @Override
    public <T extends Entity> T findLean(Class<T> entityClass, Object id, Object versionNo) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.FIND_LEAN,
                au.delegateUtilities().encodeDelegateObjectId(id),
                au.delegateUtilities().encodeDelegateVersionNo(versionNo));
        return singleEntityResultOperation(entityClass, req);
    }

    @Override
    public <T extends Entity> T findLean(Query<T> query) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.FIND_LEAN);
        setQueryDetails(req, query);
        return singleEntityResultOperation(query.getEntityClass(), req);
    }

    @Override
    public <T extends Entity> T findConstraint(T record) throws UnifyException {
        return null;
    }

    @Override
    public <T extends Entity> List<T> findAll(Query<T> query) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.FIND_ALL);
        setQueryDetails(req, query);
        return multipleEntityResultOperation(query.getEntityClass(), req);
    }

    @Override
    public <T extends Entity> List<T> findAllWithChildren(Query<T> query) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.FIND_ALL);
        setQueryDetails(req, query);
        return multipleEntityResultOperation(query.getEntityClass(), req);
    }

    @Override
    public <T, U extends Entity> Map<T, U> findAllMap(Class<T> keyClass, String keyName, Query<U> query)
            throws UnifyException {
        return Collections.emptyMap();
    }

    @Override
    public <T, U extends Entity> Map<T, List<U>> findAllListMap(Class<T> keyClass, String keyName, Query<U> query)
            throws UnifyException {
        return Collections.emptyMap();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> void findChildren(T record) throws UnifyException {
        Class<T> entityClass = (Class<T>) record.getClass();
        Entity srcRecord = find(entityClass, record.getId());
        EntityDef entityDef = au.getEntityDef(au.delegateRegistrar().resolveLongName(entityClass));
        if (!DataUtils.isBlank(entityDef.getChildrenFieldNames())) {
            new BeanValueStore(record).copyWithInclusions(new BeanValueStore(srcRecord),
                    entityDef.getChildrenFieldNames());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> void findEditableChildren(T record) throws UnifyException {
        Class<T> entityClass = (Class<T>) record.getClass();
        Entity srcRecord = find(entityClass, record.getId());
        EntityDef entityDef = au.getEntityDef(au.delegateRegistrar().resolveLongName(entityClass));
        if (!DataUtils.isBlank(entityDef.getEditableChildrenFieldNames())) {
            new BeanValueStore(record).copyWithInclusions(new BeanValueStore(srcRecord),
                    entityDef.getEditableChildrenFieldNames());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> void findReadOnlyChildren(T record) throws UnifyException {
        Class<T> entityClass = (Class<T>) record.getClass();
        Entity srcRecord = find(entityClass, record.getId());
        EntityDef entityDef = au.getEntityDef(au.delegateRegistrar().resolveLongName(entityClass));
        if (!DataUtils.isBlank(entityDef.getReadOnlyChildrenFieldNames())) {
            new BeanValueStore(record).copyWithInclusions(new BeanValueStore(srcRecord),
                    entityDef.getReadOnlyChildrenFieldNames());
        }
    }

    @Override
    public <T extends Entity> T list(Class<T> entityClass, Object id) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.LIST,
                au.delegateUtilities().encodeDelegateObjectId(id), null);
        return singleEntityResultOperation(entityClass, req);
    }

    @Override
    public <T extends Entity> T list(Class<T> entityClass, Object id, Object versionNo) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.LIST,
                au.delegateUtilities().encodeDelegateObjectId(id),
                au.delegateUtilities().encodeDelegateVersionNo(versionNo));
        return singleEntityResultOperation(entityClass, req);
    }

    @Override
    public <T extends Entity> T list(Query<T> query) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.LIST);
        setQueryDetails(req, query);
        return singleEntityResultOperation(query.getEntityClass(), req);
    }

    @Override
    public <T extends Entity> T listLean(Class<T> entityClass, Object id) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.LIST_LEAN,
                au.delegateUtilities().encodeDelegateObjectId(id), null);
        return singleEntityResultOperation(entityClass, req);
    }

    @Override
    public <T extends Entity> T listLean(Class<T> entityClass, Object id, Object versionNo) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.LIST_LEAN,
                au.delegateUtilities().encodeDelegateObjectId(id),
                au.delegateUtilities().encodeDelegateVersionNo(versionNo));
        return singleEntityResultOperation(entityClass, req);
    }

    @Override
    public <T extends Entity> T listLean(Query<T> query) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.LIST_LEAN);
        setQueryDetails(req, query);
        return singleEntityResultOperation(query.getEntityClass(), req);
    }

    @Override
    public <T extends Entity> List<T> listAll(Query<T> query) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.LIST_ALL);
        setQueryDetails(req, query);
        return multipleEntityResultOperation(query.getEntityClass(), req);
    }

    @Override
    public <T extends Entity> List<T> listAllWithChildren(Query<T> query) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.LIST_ALL);
        setQueryDetails(req, query);
        return multipleEntityResultOperation(query.getEntityClass(), req);
    }

    @Override
    public <T, U extends Entity> Map<T, U> listAllMap(Class<T> keyClass, String keyName, Query<U> query)
            throws UnifyException {
        return Collections.emptyMap();
    }

    @Override
    public <T, U extends Entity> Map<T, List<U>> listAllListMap(Class<T> keyClass, String keyName, Query<U> query)
            throws UnifyException {
        return Collections.emptyMap();
    }

    @Override
    public <T extends Entity> void listChildren(T record) throws UnifyException {

    }

    @Override
    public <T extends Entity> void listEditableChildren(T record) throws UnifyException {

    }

    @Override
    public <T extends Entity> void listReadOnlyChildren(T record) throws UnifyException {

    }

    @Override
    public <T, U extends Entity> List<T> valueList(Class<T> fieldClass, String fieldName, Query<U> query)
            throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.VALUE_LIST);
        setQueryDetails(req, query);
        req.setFieldName(fieldName);
        return multipleValueResultOperation(fieldClass, query.getEntityClass(), req);
    }

    @Override
    public <T, U extends Entity> T value(Class<T> fieldClass, String fieldName, Query<U> query) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.VALUE);
        setQueryDetails(req, query);
        req.setFieldName(fieldName);
        return singleValueResultOperation(fieldClass, query.getEntityClass(), req);
    }

    @Override
    public <T, U extends Entity> T valueOptional(Class<T> fieldClass, String fieldName, Query<U> query)
            throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.VALUE);
        setQueryDetails(req, query);
        req.setFieldName(fieldName);
        return singleValueResultOperation(fieldClass, query.getEntityClass(), req);
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
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.VALUE_SET);
        setQueryDetails(req, query);
        req.setFieldName(fieldName);
        return new LinkedHashSet<T>(multipleValueResultOperation(fieldClass, query.getEntityClass(), req));
    }

    @Override
    public <T, U, V extends Entity> Map<T, U> valueMap(Class<T> keyClass, String keyName, Class<U> valueClass,
            String valueName, Query<V> query) throws UnifyException {
        // TODO
        return null;
    }

    @Override
    public <T, U, V extends Entity> Map<T, List<U>> valueListMap(Class<T> keyClass, String keyName, Class<U> valueClass,
            String valueName, Query<V> query) throws UnifyException {
        // TODO
        return null;
    }

    @Override
    public <T extends Entity> T findFirst(Query<T> query) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Entity> T findLast(Query<T> query) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Entity> T findLeanFirst(Query<T> query) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Entity> T findLeanLast(Query<T> query) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Entity> T listFirst(Query<T> query) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Entity> T listLast(Query<T> query) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Entity> T listLeanFirst(Query<T> query) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Entity> T listLeanLast(Query<T> query) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void populateListOnly(Entity record) throws UnifyException {

    }

    @Override
    public Object create(Entity record) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.CREATE);
        setCreateAuditInformation(record);
        req.setPayload(au.delegateUtilities().encodeDelegateEntity(record));
        Long id = singleValueResultOperation(Long.class, record.getClass(), req);
        record.setPreferredId(id);
        return id;
    }

    @Override
    public int updateById(Entity record) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.UPDATE,
                au.delegateUtilities().encodeDelegateObjectId(record.getId()), null);
        setUpdateAuditInformation(record);
        req.setPayload(au.delegateUtilities().encodeDelegateEntity(record));
        return singleValueResultOperation(int.class, record.getClass(), req);
    }

    @Override
    public int updateByIdVersion(Entity record) throws UnifyException {
        long versionNo = record instanceof BaseVersionEntity ? ((BaseVersionEntity) record).getVersionNo() : 0;
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.UPDATE,
                au.delegateUtilities().encodeDelegateObjectId(record.getId()), versionNo);
        setUpdateAuditInformation(record);
        req.setPayload(au.delegateUtilities().encodeDelegateEntity(record));
        return singleValueResultOperation(int.class, record.getClass(), req);
    }

    @Override
    public int updateByIdEditableChildren(Entity record) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.UPDATE_EDITABLE_CHILD,
                au.delegateUtilities().encodeDelegateObjectId(record.getId()), null);
        setUpdateAuditInformation(record);
        req.setPayload(au.delegateUtilities().encodeDelegateEntity(record));
        return singleValueResultOperation(int.class, record.getClass(), req);
    }

    @Override
    public int updateByIdVersionEditableChildren(Entity record) throws UnifyException {
        long versionNo = record instanceof BaseVersionEntity ? ((BaseVersionEntity) record).getVersionNo() : 0;
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.UPDATE_EDITABLE_CHILD,
                au.delegateUtilities().encodeDelegateObjectId(record.getId()), versionNo);
        setUpdateAuditInformation(record);
        req.setPayload(au.delegateUtilities().encodeDelegateEntity(record));
        return singleValueResultOperation(int.class, record.getClass(), req);
    }

    @Override
    public int updateLeanById(Entity record) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.UPDATE_LEAN,
                au.delegateUtilities().encodeDelegateObjectId(record.getId()), null);
        setUpdateAuditInformation(record);
        req.setPayload(au.delegateUtilities().encodeDelegateEntity(record));
        return singleValueResultOperation(int.class, record.getClass(), req);
    }

    @Override
    public int updateLeanByIdVersion(Entity record) throws UnifyException {
        long versionNo = record instanceof BaseVersionEntity ? ((BaseVersionEntity) record).getVersionNo() : 0;
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.UPDATE_LEAN,
                au.delegateUtilities().encodeDelegateObjectId(record.getId()), versionNo);
        setUpdateAuditInformation(record);
        req.setPayload(au.delegateUtilities().encodeDelegateEntity(record));
        return singleValueResultOperation(int.class, record.getClass(), req);
    }

    @Override
    public int updateById(Class<? extends Entity> entityClass, Object id, Update update) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.UPDATE,
                au.delegateUtilities().encodeDelegateObjectId(id), null);
        setUpdateAuditInformation(entityClass, id, update);
        req.setUpdate(au.delegateUtilities().encodeDelegateUpdate(update));
        return singleValueResultOperation(int.class, entityClass, req);
    }

    @Override
    public int updateAll(Query<? extends Entity> query, Update update) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.UPDATE_ALL);
        setQueryDetails(req, query);
        setUpdateAuditInformation(query.getEntityClass(), null, update);
        req.setUpdate(au.delegateUtilities().encodeDelegateUpdate(update));
        return singleValueResultOperation(int.class, query.getEntityClass(), req);
    }

    @Override
    public <T extends Number, U extends Entity> int add(Class<T> arg0, String arg1, T arg2, Query<U> arg3)
            throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Number, U extends Entity> int divide(Class<T> arg0, String arg1, T arg2, Query<U> arg3)
            throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Number, U extends Entity> int multiply(Class<T> arg0, String arg1, T arg2, Query<U> arg3)
            throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Number, U extends Entity> int subtract(Class<T> arg0, String arg1, T arg2, Query<U> arg3)
            throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int deleteById(Entity record) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.DELETE,
                au.delegateUtilities().encodeDelegateObjectId(record.getId()), null);
        return singleValueResultOperation(int.class, record.getClass(), req);
    }

    @Override
    public int deleteByIdVersion(Entity record) throws UnifyException {
        long versionNo = record instanceof BaseVersionEntity ? ((BaseVersionEntity) record).getVersionNo() : 0;
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.DELETE,
                au.delegateUtilities().encodeDelegateObjectId(record.getId()), versionNo);
        return singleValueResultOperation(int.class, record.getClass(), req);
    }

    @Override
    public int delete(Class<? extends Entity> entityClass, Object id) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.DELETE,
                au.delegateUtilities().encodeDelegateObjectId(id), null);
        return singleValueResultOperation(int.class, entityClass, req);
    }

    @Override
    public int deleteAll(Query<? extends Entity> query) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.DELETE_ALL);
        setQueryDetails(req, query);
        return singleValueResultOperation(int.class, query.getEntityClass(), req);
    }

    @Override
    public <T extends Entity> int countAll(Query<T> query) throws UnifyException {
        DataSourceRequest req = new DataSourceRequest(DataSourceOperation.COUNT_ALL);
        setQueryDetails(req, query);
        return singleValueResultOperation(int.class, query.getEntityClass(), req);
    }

    @Override
    public Aggregation aggregate(AggregateFunction aggregateFunction, Query<? extends Entity> query)
            throws UnifyException {
        return null;
    }

    @Override
    public List<Aggregation> aggregateMany(Aggregate aggregate, Query<? extends Entity> query) throws UnifyException {
        return null;
    }

    @Override
    public List<GroupAggregation> aggregateGroupMany(Aggregate aggregate, Query<? extends Entity> query)
            throws UnifyException {
        return null;
    }

    @Override
    public Entity getExtendedInstance(Class<? extends Entity> entityClass) throws UnifyException {
        return null;
    }

    @Override
    public Date getNow() throws UnifyException {
        return application().getNow();
    }

    @Override
    public void executeCallable(CallableProc callableProc) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<Class<?>, List<?>> executeCallableWithResults(CallableProc callableProc) throws UnifyException {
        throw new UnsupportedOperationException();
    }

    protected AppletUtilities au() {
        return au;
    }

    protected ApplicationModuleService application() {
        return au.application();
    }

    protected EnvironmentDelegateUtilities utilities() {
        return au.delegateUtilities();
    }

    protected String getLongName(Class<? extends Entity> entityClass) throws UnifyException {
        return au.delegateRegistrar().resolveLongName(entityClass);
    }

    protected abstract BaseResponse sendToDelegateDatasourceService(DataSourceRequest req) throws UnifyException;

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    private void setCreateAuditInformation(Entity entity) throws UnifyException {
        if (entity instanceof BaseAuditEntity) {
            UserToken userToken = getUserToken();
            String userLoginId = userToken != null ? userToken.getUserLoginId() : null;
            BaseAuditEntity baseAuditEntity = (BaseAuditEntity) entity;
            Date now = getNow();
            baseAuditEntity.setCreateDt(now);
            baseAuditEntity.setCreatedBy(userLoginId);
            baseAuditEntity.setUpdateDt(now);
            baseAuditEntity.setUpdatedBy(userLoginId);
        }
    }

    private void setUpdateAuditInformation(Entity entity) throws UnifyException {
        if (entity instanceof BaseAuditEntity) {
            UserToken userToken = getUserToken();
            String userLoginId = userToken != null ? userToken.getUserLoginId() : null;
            BaseAuditEntity baseAuditEntity = (BaseAuditEntity) entity;
            Date now = getNow();
            baseAuditEntity.setUpdateDt(now);
            if (userLoginId != null) {
                baseAuditEntity.setUpdatedBy(userLoginId);
            }
        }
    }

    private void setUpdateAuditInformation(Class<? extends Entity> entityClass, Object id, Update update)
            throws UnifyException {
        if (BaseAuditEntity.class.isAssignableFrom(entityClass)) {
            UserToken userToken = getUserToken();
            String userLoginId = userToken != null ? userToken.getUserLoginId() : null;
            Date now = getNow();
            update.add("updateDt", now);
            if (userLoginId != null) {
                update.add("updatedBy", userLoginId);
            }
        }
    }

    private <T extends Entity, U> U singleValueResultOperation(Class<U> resultClass, Class<T> entityClass,
            DataSourceRequest req) throws UnifyException {
        req.setEntity(au.delegateRegistrar().resolveLongName(entityClass));
        BaseResponse resp = sendToDelegateDatasourceService(req);
        Object[] payload = resp instanceof JsonDataSourceResponse ? ((JsonDataSourceResponse) resp).getPayload()
                : ((PseudoDataSourceResponse) resp).getPayload();
        Object result = payload != null && payload.length == 1 ? payload[0] : null;
        return DataUtils.convert(resultClass, result);
    }

    @SuppressWarnings("unchecked")
    private <T extends Entity, U> List<U> multipleValueResultOperation(Class<U> resultClass, Class<T> entityClass,
            DataSourceRequest req) throws UnifyException {
        req.setEntity(au.delegateRegistrar().resolveLongName(entityClass));
        BaseResponse resp = sendToDelegateDatasourceService(req);
        Object[] payload = resp instanceof JsonDataSourceResponse ? ((JsonDataSourceResponse) resp).getPayload()
                : ((PseudoDataSourceResponse) resp).getPayload();
        if (payload != null) {
            return DataUtils.convert(List.class, resultClass, Arrays.asList(payload));
        }

        return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    private <T extends Entity> T singleEntityResultOperation(Class<T> entityClass, DataSourceRequest req)
            throws UnifyException {
        req.setEntity(au.delegateRegistrar().resolveLongName(entityClass));
        BaseResponse resp = sendToDelegateDatasourceService(req);
        if (resp instanceof JsonDataSourceResponse) {
            String[] payload = ((JsonDataSourceResponse) resp).getPayload();
            if (payload != null && payload.length == 1) {
                return readEntityResultFromJsonPayload(entityClass, (String) payload[0], req);
            }
        } else {
            Object[] payload = ((PseudoDataSourceResponse) resp).getPayload();
            if (payload != null && payload.length == 1) {
                return (T) payload[0];
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private <T extends Entity> List<T> multipleEntityResultOperation(Class<T> entityClass, DataSourceRequest req)
            throws UnifyException {
        req.setEntity(au.delegateRegistrar().resolveLongName(entityClass));
        BaseResponse resp = sendToDelegateDatasourceService(req);
        if (resp instanceof JsonDataSourceResponse) {
            String[] payload = ((JsonDataSourceResponse) resp).getPayload();
            if (payload != null && payload.length > 0) {
                List<T> resultList = new ArrayList<T>();
                for (int i = 0; i < payload.length; i++) {
                    resultList.add(readEntityResultFromJsonPayload(entityClass, (String) payload[i], req));
                }
                return resultList;
            }
        } else {
            Object[] payload = ((PseudoDataSourceResponse) resp).getPayload();
            if (payload != null && payload.length > 0) {
                List<T> resultList = new ArrayList<T>();
                for (int i = 0; i < payload.length; i++) {
                    resultList.add((T) payload[i]);
                }
                return resultList;
            }
        }

        return Collections.emptyList();
    }

    private <T extends Entity> void setQueryDetails(DataSourceRequest req, Query<T> query) throws UnifyException {
        req.setQuery(au.delegateUtilities().encodeDelegateQuery(query));
        req.setOrder(au.delegateUtilities().encodeDelegateOrder(query));
        req.setIgnoreEmptyCriteria(query.isIgnoreEmptyCriteria());
        req.setOffset(query.getOffset());
        req.setLimit(query.getLimit());
    }

    private <T extends Entity> T readEntityResultFromJsonPayload(Class<T> entityClass, String json,
            DataSourceRequest req) throws UnifyException {
        T entity = DataUtils.fromJsonString(entityClass, json);
        if (req.getOperation().isList()) {
            String entityName = au.delegateRegistrar().resolveLongName(entityClass);
            EntityDef entityDef = au.application().getEntityDef(entityName);
            if (entityDef.isWithListOnly()) {
                LocalKeyObjects localKeyObjects = new LocalKeyObjects();
                for (EntityFieldDef entityFieldDef : entityDef.getListOnlyFieldDefList()) {
                    ValueStore keyObject = getLocalKeyObject(entityDef, entity, entityFieldDef.getKey(),
                            localKeyObjects);
                    if (keyObject != null) {
                        Object listVal = keyObject.retrieve(entityFieldDef.getProperty());
                        DataUtils.setBeanProperty(entity, entityFieldDef.getFieldName(), listVal);
                    }
                }
            }
        }

        return entity;
    }

    @SuppressWarnings("unchecked")
    private ValueStore getLocalKeyObject(EntityDef entityDef, Entity bean, String keyFieldName,
            LocalKeyObjects localKeyObjects) throws UnifyException {
        Object keyVal = DataUtils.getBeanProperty(Object.class, bean, keyFieldName);
        if (keyVal != null) {
            if (!localKeyObjects.containsLocalKeyObject(keyFieldName, keyVal)) {
                Object resolvedKeyObject = null;
                if (keyVal != null) {
                    EntityFieldDef _refEntityFieldDef = entityDef.getFieldDef(keyFieldName);
                    if (_refEntityFieldDef.isEnumDataType()) {
                        resolvedKeyObject = getListItemByKey(LocaleType.SESSION, _refEntityFieldDef.getReferences(),
                                ((EnumConst) keyVal).code());
                        resolvedKeyObject = resolvedKeyObject != null ? new ListOption((Listable) resolvedKeyObject)
                                : null;
                    } else {
                        EntityClassDef _refEntityClassDef = au.application()
                                .getEntityClassDef(_refEntityFieldDef.getRefDef().getEntity());
                        resolvedKeyObject = listLean((Class<? extends Entity>) _refEntityClassDef.getEntityClass(),
                                keyVal);
                    }
                }

                localKeyObjects.putKeyObject(keyFieldName, keyVal, resolvedKeyObject);
            }

            return localKeyObjects.getLocalKeyObject(keyFieldName, keyVal);
        }

        return null;
    }

    private class LocalKeyObjects {

        private Map<String, Map<Object, ValueStore>> objects;

        public LocalKeyObjects() {
            this.objects = new HashMap<String, Map<Object, ValueStore>>();
        }

        public void putKeyObject(String keyFieldName, Object keyVal, Object resolvedKeyVal) {
            Map<Object, ValueStore> map = objects.get(keyFieldName);
            if (map == null) {
                map = new HashMap<Object, ValueStore>();
                objects.put(keyFieldName, map);
            }

            BeanValueStore valueStore = resolvedKeyVal != null ? new BeanValueStore(resolvedKeyVal) : null;
            map.put(keyVal, valueStore);
        }

        public boolean containsLocalKeyObject(String keyFieldName, Object keyVal) {
            Map<Object, ValueStore> map = objects.get(keyFieldName);
            return map != null && map.containsKey(keyVal);
        }

        public ValueStore getLocalKeyObject(String keyFieldName, Object keyVal) {
            Map<Object, ValueStore> map = objects.get(keyFieldName);
            if (map != null) {
                return map.get(keyVal);
            }

            return null;
        }
    }

    public static class ListOption {

        private Listable listable;

        public ListOption(Listable listable) {
            this.listable = listable;
        }

        public String getName() {
            return listable.getListKey();
        }

        public String getDescription() {
            return listable.getListDescription();
        }
    }
}
