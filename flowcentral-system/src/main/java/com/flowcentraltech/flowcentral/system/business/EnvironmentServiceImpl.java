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
package com.flowcentraltech.flowcentral.system.business;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.flowcentraltech.flowcentral.common.business.EntityAuditInfoProvider;
import com.flowcentraltech.flowcentral.common.business.EnvironmentDelegate;
import com.flowcentraltech.flowcentral.common.business.EnvironmentDelegateInfo;
import com.flowcentraltech.flowcentral.common.business.EnvironmentDelegateUtilities;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.business.QueryEncoder;
import com.flowcentraltech.flowcentral.common.business.SuggestionProvider;
import com.flowcentraltech.flowcentral.common.business.policies.ChildListEditPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.business.policies.EntityListActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityListActionPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.EntityListActionResult;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.data.EntityAuditInfo;
import com.flowcentraltech.flowcentral.configuration.constants.RecordActionType;
import com.flowcentraltech.flowcentral.system.constants.SystemModuleNameConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.business.AbstractBusinessService;
import com.tcdng.unify.core.constant.PrintFormat;
import com.tcdng.unify.core.criterion.Aggregate;
import com.tcdng.unify.core.criterion.AggregateFunction;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.Aggregation;
import com.tcdng.unify.core.data.Audit;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.GroupAggregation;
import com.tcdng.unify.core.database.Database;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Default implementation of environment service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(SystemModuleNameConstants.ENVIRONMENT_SERVICE)
public class EnvironmentServiceImpl extends AbstractBusinessService
        implements EnvironmentService, EnvironmentDelegateUtilities {

    private Map<Class<? extends Entity>, EnvironmentDelegateInfo> delegateInfoByEntityClass;

    private Map<String, EnvironmentDelegateInfo> delegateInfoByLongName;

    @Configurable
    private QueryEncoder queryEncoder;

    @Configurable
    private SuggestionProvider suggestionProvider;

    @Configurable
    private EntityAuditInfoProvider entityAuditInfoProvider;

    public EnvironmentServiceImpl() {
        this.delegateInfoByEntityClass = new ConcurrentHashMap<Class<? extends Entity>, EnvironmentDelegateInfo>();
        this.delegateInfoByLongName = new ConcurrentHashMap<String, EnvironmentDelegateInfo>();
    }

    public final void setQueryEncoder(QueryEncoder queryEncoder) {
        this.queryEncoder = queryEncoder;
    }

    public final void setSuggestionProvider(SuggestionProvider suggestionProvider) {
        this.suggestionProvider = suggestionProvider;
    }

    public final void setEntityAuditInfoProvider(EntityAuditInfoProvider entityAuditInfoProvider) {
        this.entityAuditInfoProvider = entityAuditInfoProvider;
    }

    @Override
    public void registerDelegate(Class<? extends Entity> entityClass, String entityLongName, String delegateName)
            throws UnifyException {
        unregisterDelegate(entityLongName);
        EnvironmentDelegate environmentDelegate = (EnvironmentDelegate) getComponent(delegateName);
        EnvironmentDelegateInfo delegateInfo = new EnvironmentDelegateInfo(entityLongName, entityClass,
                environmentDelegate);
        delegateInfoByEntityClass.put(entityClass, delegateInfo);
        delegateInfoByLongName.put(entityLongName, delegateInfo);
    }

    @Override
    public void unregisterDelegate(String entityLongName) throws UnifyException {
        EnvironmentDelegateInfo delegateInfo = delegateInfoByLongName.remove(entityLongName);
        if (delegateInfo != null) {
            delegateInfoByEntityClass.remove(delegateInfo.getEntityClass());
        }
    }

    @Override
    public Long encodeDelegateObjectId(Object id) throws UnifyException {
        return (Long) id;
    }

    @Override
    public Long encodeDelegateVersionNo(Object versionNo) throws UnifyException {
        return (Long) versionNo;
    }

    @Override
    public String encodeDelegateQuery(Query<? extends Entity> query) throws UnifyException {
        return queryEncoder.encodeQueryFilter(query);
    }

    @Override
    public String encodeDelegateOrder(Query<? extends Entity> query) throws UnifyException {
        return queryEncoder.encodeQueryOrder(query);
    }

    @Override
    public String encodeDelegateUpdate(Update update) throws UnifyException {
        return queryEncoder.encodeUpdate(update);
    }

    @Override
    public String[] encodeDelegateEntity(Entity inst) throws UnifyException {
        String json = DataUtils.asJsonString(inst, PrintFormat.NONE);
        return new String[] { json };
    }

    @Override
    public Query<? extends Entity> decodeDelegateQuery(String entity, String query, String order)
            throws UnifyException {
        return queryEncoder.decodeQuery(entity, query, order);
    }

    @Override
    public String resolveLongName(Class<? extends Entity> entityClass) throws UnifyException {
        EnvironmentDelegateInfo delegateInfo = delegateInfoByEntityClass.get(entityClass);
        if (delegateInfo == null) {
            // TODO Throw exception
        }

        return delegateInfo.getEntityLongName();
    }

    @Override
    public Database getDatabase() throws UnifyException {
        return db();
    }

    @Override
    public void clearRollbackTransactions() throws UnifyException {
        super.clearRollbackTransactions();
    }

    @Override
    public void setSavePoint() throws UnifyException {
        super.setSavePoint();
    }

    @Override
    public void clearSavePoint() throws UnifyException {
        super.clearSavePoint();
    }

    @Override
    public void rollbackToSavePoint() throws UnifyException {
        super.rollbackToSavePoint();
    }

    @Override
    public EntityActionResult performEntityAction(EntityActionContext ctx) throws UnifyException {
        EntityActionResult result = executeEntityPreActionPolicy(ctx);
        if (result == null) {
            return executeEntityPostActionPolicy(db(ctx.getInst().getClass()), ctx);
        }

        return result;
    }

    @Override
    public EntityListActionResult performEntityAction(EntityListActionContext ctx) throws UnifyException {
        if (ctx.isWithPolicy()) {
            return ((EntityListActionPolicy) getComponent(ctx.getPolicyName())).executeAction(ctx);
        }

        return new EntityListActionResult(ctx);
    }

    @Override
    public Object create(Entity inst) throws UnifyException {
        return db(inst.getClass()).create(inst);
    }

    @Override
    public EntityActionResult create(EntityActionContext ctx) throws UnifyException {
        EntityActionResult result = executeEntityPreActionPolicy(ctx);
        if (result == null) {
            Entity inst = ctx.getInst();
            ctx.setResult(create(inst));
            if (suggestionProvider != null) {
                suggestionProvider.saveSuggestions(ctx.getEntityDef(), inst);
            }

            return executeEntityPostActionPolicy(db(inst.getClass()), ctx);
        }

        return result;
    }

    @Override
    public <T extends Entity> T find(Class<T> clazz, Object id) throws UnifyException {
        return (T) db(clazz).find(Query.of(clazz).addEquals("id", id));
    }

    @Override
    public <T extends Entity> T find(Query<T> query) throws UnifyException {
        return (T) db(query.getEntityClass()).find(query);
    }

    @Override
    public <T extends Entity> T findLean(Class<T> clazz, Object id) throws UnifyException {
        return (T) db(clazz).findLean(Query.of(clazz).addEquals("id", id));
    }

    @Override
    public <T extends Entity> T findLean(Query<T> query) throws UnifyException {
        return (T) db(query.getEntityClass()).findLean(query);
    }

    @Override
    public <T extends Entity> List<T> findAll(Query<T> query) throws UnifyException {
        return db(query.getEntityClass()).findAll(query);
    }

    @Override
    public <T extends Entity> List<T> findAllWithChildren(Query<T> query) throws UnifyException {
        return db(query.getEntityClass()).findAllWithChildren(query);
    }

    @Override
    public <T, U extends Entity> Map<T, U> findAllMap(Class<T> keyClass, String keyName, Query<U> query)
            throws UnifyException {
        return db(query.getEntityClass()).findAllMap(keyClass, keyName, query);
    }

    @Override
    public <T, U extends Entity> Map<T, List<U>> findAllListMap(Class<T> keyClass, String keyName, Query<U> query)
            throws UnifyException {
        return db(query.getEntityClass()).findAllListMap(keyClass, keyName, query);
    }

    @Override
    public <T extends Entity> void findChildren(T record) throws UnifyException {
        db(record.getClass()).findChildren(record);
    }

    @Override
    public <T extends Entity> T list(Class<T> clazz, Object id) throws UnifyException {
        return (T) db(clazz).list(Query.of(clazz).addEquals("id", id));
    }

    @Override
    public <T extends Entity> T list(Query<T> query) throws UnifyException {
        return (T) db(query.getEntityClass()).list(query);
    }

    @Override
    public <T extends Entity> T listLean(Class<T> clazz, Object id) throws UnifyException {
        return (T) db(clazz).listLean(Query.of(clazz).addEquals("id", id));
    }

    @Override
    public <T extends Entity> T listLean(Query<T> query) throws UnifyException {
        return (T) db(query.getEntityClass()).listLean(query);
    }

    @Override
    public <T extends Entity> List<T> listAll(Query<T> query) throws UnifyException {
        return db(query.getEntityClass()).listAll(query);
    }

    @Override
    public <T extends Entity> List<T> listAllWithChildren(Query<T> query) throws UnifyException {
        return db(query.getEntityClass()).listAllWithChildren(query);
    }

    @Override
    public <T, U extends Entity> T listValue(Class<T> valueClazz, Class<U> recordClazz, Object id, String property)
            throws UnifyException {
        return db(recordClazz).value(valueClazz, property, Query.of(recordClazz).addEquals("id", id));
    }

    @Override
    public <T, U extends Entity> Map<T, U> listAllMap(Class<T> keyClass, String keyName, Query<U> query)
            throws UnifyException {
        return db(query.getEntityClass()).listAllMap(keyClass, keyName, query);
    }

    @Override
    public int updateByIdVersion(Entity record) throws UnifyException {
        return db(record.getClass()).updateByIdVersion(record);
    }

    @Override
    public int updateLean(Entity record) throws UnifyException {
        return db(record.getClass()).updateLeanByIdVersion(record);
    }

    @Override
    public EntityActionResult updateByIdVersion(EntityActionContext ctx) throws UnifyException {
        EntityActionResult result = executeEntityPreActionPolicy(ctx);
        if (result == null) {
            Entity inst = ctx.getInst();
            if (entityAuditInfoProvider != null) {
                EntityAuditInfo entityAuditInfo = entityAuditInfoProvider.getEntityAuditInfo(ctx.getEntityDef());
                if (entityAuditInfo.auditable() && entityAuditInfo.inclusions()) {
                    Entity _oldInst = findLean(inst.getClass(), inst.getId());
                    Audit audit = new BeanValueStore(inst).diff(new BeanValueStore(_oldInst),
                            entityAuditInfo.getInclusions());
                    ctx.setAudit(audit);
                }
            }

            ctx.setResult(db(inst.getClass()).updateByIdVersion(inst));
            if (suggestionProvider != null) {
                suggestionProvider.saveSuggestions(ctx.getEntityDef(), inst);
            }

            return executeEntityPostActionPolicy(db(inst.getClass()), ctx);
        }

        return result;
    }

    @Override
    public EntityActionResult updateLean(EntityActionContext ctx) throws UnifyException {
        EntityActionResult result = executeEntityPreActionPolicy(ctx);
        if (result == null) {
            Entity inst = ctx.getInst();
            if (entityAuditInfoProvider != null) {
                EntityAuditInfo entityAuditInfo = entityAuditInfoProvider.getEntityAuditInfo(ctx.getEntityDef());
                if (entityAuditInfo.auditable() && entityAuditInfo.inclusions()) {
                    Entity _oldInst = findLean(inst.getClass(), inst.getId());
                    Audit audit = new BeanValueStore(inst).diff(new BeanValueStore(_oldInst),
                            entityAuditInfo.getInclusions());
                    ctx.setAudit(audit);
                }
            }

            ctx.setResult(db(inst.getClass()).updateLeanByIdVersion(inst));
            if (suggestionProvider != null) {
                suggestionProvider.saveSuggestions(ctx.getEntityDef(), inst);
            }

            return executeEntityPostActionPolicy(db(inst.getClass()), ctx);
        }

        return result;
    }

    @Override
    public int updateById(Class<? extends Entity> clazz, Object id, Update update) throws UnifyException {
        return db(clazz).updateById(clazz, id, update);
    }

    @Override
    public int updateAll(Query<? extends Entity> query, Update update) throws UnifyException {
        return db(query.getEntityClass()).updateAll(query, update);
    }

    @Override
    public int updateLeanById(Entity record) throws UnifyException {
        return db(record.getClass()).updateLeanById(record);
    }

    @Override
    public int updateLeanByIdVersion(Entity record) throws UnifyException {
        return db(record.getClass()).updateLeanByIdVersion(record);
    }

    @Override
    public <T extends Number, U extends Entity> int add(Class<T> fieldClass, String fieldName, T val, Query<U> query)
            throws UnifyException {
        return db(query.getEntityClass()).add(fieldClass, fieldName, val, query);
    }

    @Override
    public <T extends Number, U extends Entity> int subtract(Class<T> fieldClass, String fieldName, T val,
            Query<U> query) throws UnifyException {
        return db(query.getEntityClass()).subtract(fieldClass, fieldName, val, query);
    }

    @Override
    public <T extends Number, U extends Entity> int multiply(Class<T> fieldClass, String fieldName, T val,
            Query<U> query) throws UnifyException {
        return db(query.getEntityClass()).multiply(fieldClass, fieldName, val, query);
    }

    @Override
    public <T extends Number, U extends Entity> int divide(Class<T> fieldClass, String fieldName, T val, Query<U> query)
            throws UnifyException {
        return db(query.getEntityClass()).divide(fieldClass, fieldName, val, query);
    }

    @Override
    public <T extends Number, U extends Entity> T min(Class<T> fieldClass, String fieldName, Query<U> query) throws UnifyException {
        return db(query.getEntityClass()).min(fieldClass, fieldName, query);
    }

    @Override
    public <T extends Number, U extends Entity> T max(Class<T> fieldClass, String fieldName, Query<U> query) throws UnifyException {
        return db(query.getEntityClass()).max(fieldClass, fieldName, query);
    }

    @Override
    public int delete(Entity inst) throws UnifyException {
        return db(inst.getClass()).deleteByIdVersion(inst);
    }

    @Override
    public EntityActionResult delete(EntityActionContext ctx) throws UnifyException {
        EntityActionResult result = executeEntityPreActionPolicy(ctx);
        if (result == null) {
            Entity inst = ctx.getInst();
            ctx.setResult(db(inst.getClass()).deleteByIdVersion(inst));
            return executeEntityPostActionPolicy(db(inst.getClass()), ctx);
        }

        return result;
    }

    @Override
    public <T extends Entity> int delete(Class<T> clazz, Object id) throws UnifyException {
        return db(clazz).delete(clazz, id);
    }

    @Override
    public int deleteAll(Query<? extends Entity> query) throws UnifyException {
        return db(query.getEntityClass()).deleteAll(query);
    }

    @Override
    public int deleteByIdVersion(Entity record) throws UnifyException {
        return db(record.getClass()).deleteByIdVersion(record);
    }

    @Override
    public int deleteById(Entity record) throws UnifyException {
        return db(record.getClass()).deleteById(record);
    }

    @Override
    public Aggregation aggregate(AggregateFunction aggregateFunction, Query<? extends Entity> query)
            throws UnifyException {
        return db(query.getEntityClass()).aggregate(aggregateFunction, query);
    }

    @Override
    public List<Aggregation> aggregateMany(Aggregate aggregate, Query<? extends Entity> query) throws UnifyException {
        return db(query.getEntityClass()).aggregateMany(aggregate, query);
    }

    @Override
    public List<GroupAggregation> aggregateGroupMany(Aggregate aggregate, Query<? extends Entity> query)
            throws UnifyException {
        return db(query.getEntityClass()).aggregateGroupMany(aggregate, query);
    }

    @Override
    public void populateListOnly(Entity record) throws UnifyException {
        db(record.getClass()).populateListOnly(record);
    }

    @Override
    public <T extends Entity> int countAll(Query<T> query) throws UnifyException {
        return db(query.getEntityClass()).countAll(query);
    }

    @Override
    public <T extends Entity> T findConstraint(T record) throws UnifyException {
        return db(record.getClass()).findConstraint(record);
    }

    @Override
    public <T, U extends Entity> List<T> getAssignedList(Class<U> entityClass, Class<T> valueClass, String baseField,
            Object baseId, String valueField) throws UnifyException {
        return db(entityClass).valueList(valueClass, valueField, Query.of(entityClass).addEquals(baseField, baseId));
    }

    @Override
    public <T, U extends Entity> Set<T> getAssignedSet(Class<U> entityClass, Class<T> valueClass, String baseField,
            Object baseId, String valueField) throws UnifyException {
        return db(entityClass).valueSet(valueClass, valueField, Query.of(entityClass).addEquals(baseField, baseId));
    }

    @Override
    public <T, U extends Entity> int updateAssignedList(SweepingCommitPolicy sweepingCommitPolicy,
            String assignmentUpdatePolicy, Class<U> entityClass, String baseField, Object baseId, String valueField,
            List<T> valueList) throws UnifyException {
        final Database db = db(entityClass);
        int updated = 0;
        db.deleteAll(Query.of(entityClass).addEquals(baseField, baseId));
        if (!DataUtils.isBlank(valueList)) {
            Entity inst = ReflectUtils.newInstance(entityClass); // TODO Get from class manager
            DataUtils.setBeanProperty(inst, baseField, baseId);
            for (T val : valueList) {
                DataUtils.setBeanProperty(inst, valueField, val);
                db.create(inst);
            }

            updated = valueList.size();
        }

        if (sweepingCommitPolicy != null) {
            sweepingCommitPolicy.bumpAllParentVersions(db, RecordActionType.UPDATE);
        }

        if (!StringUtils.isBlank(assignmentUpdatePolicy)) {
            ((ChildListEditPolicy) getComponent(assignmentUpdatePolicy)).postAssignmentUpdate(entityClass, baseField,
                    baseId);
        }

        return updated;
    }

    @Override
    public <T, U extends Entity> int updateAssignedList(SweepingCommitPolicy sweepingCommitPolicy,
            String assignmentUpdatePolicy, Class<U> entityClass, String baseField, Object baseId, List<T> instList,
            boolean fixedAssignment) throws UnifyException {
        final Database db = db(entityClass);
        if (fixedAssignment) {
            if (!DataUtils.isBlank(instList)) {
                for (T inst : instList) {
                    DataUtils.setBeanProperty(inst, baseField, baseId);
                    db.updateByIdVersion((Entity) inst);
                }
            }
        } else {
            db.deleteAll(Query.of(entityClass).addEquals(baseField, baseId));
            if (!DataUtils.isBlank(instList)) {
                for (T inst : instList) {
                    DataUtils.setBeanProperty(inst, baseField, baseId);
                    db.create((Entity) inst);
                }
            }
        }

        if (sweepingCommitPolicy != null) {
            sweepingCommitPolicy.bumpAllParentVersions(db, RecordActionType.UPDATE);
        }

        if (!StringUtils.isBlank(assignmentUpdatePolicy)) {
            ((ChildListEditPolicy) getComponent(assignmentUpdatePolicy)).postAssignmentUpdate(entityClass, baseField,
                    baseId);
        }

        return instList != null ? instList.size() : 0;
    }

    @Override
    public <T, U extends Entity> int updateEntryList(SweepingCommitPolicy sweepingCommitPolicy,
            String entryUpdatePolicy, Class<U> entityClass, String baseField, Object baseId, List<T> instList)
            throws UnifyException {
        final Database db = db(entityClass);
        if (!DataUtils.isBlank(instList)) {
            for (T inst : instList) {
                DataUtils.setBeanProperty(inst, baseField, baseId);
                db.updateByIdVersion((Entity) inst);
            }
        }

        if (sweepingCommitPolicy != null) {
            sweepingCommitPolicy.bumpAllParentVersions(db, RecordActionType.UPDATE);
        }

        if (!StringUtils.isBlank(entryUpdatePolicy)) {
            ((ChildListEditPolicy) getComponent(entryUpdatePolicy)).postEntryUpdate(entityClass, baseField, baseId,
                    instList);
        }

        return instList != null ? instList.size() : 0;
    }

    @Override
    public <T> T value(Class<T> valueType, String valueFieldName, Query<? extends Entity> query) throws UnifyException {
        return db(query.getEntityClass()).value(valueType, valueFieldName, query);
    }

    @Override
    public <T> List<T> valueList(Class<T> valueType, String valueFieldName, Query<? extends Entity> query)
            throws UnifyException {
        return db(query.getEntityClass()).valueList(valueType, valueFieldName, query);
    }

    @Override
    public <T, U extends Entity> Set<T> valueSet(Class<T> fieldClass, String fieldName, Query<U> query)
            throws UnifyException {
        return db(query.getEntityClass()).valueSet(fieldClass, fieldName, query);
    }

    @Override
    public <T, U, V extends Entity> Map<T, U> valueMap(Class<T> keyClass, String keyName, Class<U> valueClass,
            String valueName, Query<V> query) throws UnifyException {
        return db(query.getEntityClass()).valueMap(keyClass, keyName, valueClass, valueName, query);
    }

    @Override
    public <T, U, V extends Entity> Map<T, List<U>> valueListMap(Class<T> keyClass, String keyName, Class<U> valueClass,
            String valueName, Query<V> query) throws UnifyException {
        return db(query.getEntityClass()).valueListMap(keyClass, keyName, valueClass, valueName, query);
    }

    @Override
    public <T extends Entity> T findFirst(Query<T> query) throws UnifyException {
        return db(query.getEntityClass()).findFirst(query);
    }

    @Override
    public <T extends Entity> T findLast(Query<T> query) throws UnifyException {
        return db(query.getEntityClass()).findLast(query);
    }

    @Override
    public <T extends Entity> T findLeanFirst(Query<T> query) throws UnifyException {
        return db(query.getEntityClass()).findLeanFirst(query);
    }

    @Override
    public <T extends Entity> T findLeanLast(Query<T> query) throws UnifyException {
        return db(query.getEntityClass()).findLeanLast(query);
    }

    @Override
    public <T extends Entity> T listFirst(Query<T> query) throws UnifyException {
        return db(query.getEntityClass()).listFirst(query);
    }

    @Override
    public <T extends Entity> T listLast(Query<T> query) throws UnifyException {
        return db(query.getEntityClass()).listLast(query);
    }

    @Override
    public <T extends Entity> T listLeanFirst(Query<T> query) throws UnifyException {
        return db(query.getEntityClass()).listLeanFirst(query);
    }

    @Override
    public <T extends Entity> T listLeanLast(Query<T> query) throws UnifyException {
        return db(query.getEntityClass()).listLeanLast(query);
    }

    @Override
    public String getEntityDataSourceName(String entityLongName) throws UnifyException {
        EnvironmentDelegateInfo delegateInfo = delegateInfoByLongName.get(entityLongName);
        return delegateInfo != null ? delegateInfo.getDataSourceName() : db().getDataSourceName();
    }

    @Override
    public String getEntityDataSourceName(Class<? extends Entity> entityClass) throws UnifyException {
        EnvironmentDelegateInfo delegateInfo = delegateInfoByEntityClass.get(entityClass);
        return delegateInfo != null ? delegateInfo.getDataSourceName() : db().getDataSourceName();
    }

    private Database db(Class<? extends Entity> entityClass) throws UnifyException {
        EnvironmentDelegateInfo delegateInfo = delegateInfoByEntityClass.get(entityClass);
        return delegateInfo != null ? (delegateInfo.isDirect() ? db(delegateInfo.getDataSourceName())
                : delegateInfo.getEnvironmentDelegate()) : db();
    }

    private EntityActionResult executeEntityPreActionPolicy(EntityActionContext ctx) throws UnifyException {
        if (ctx.isWithActionPolicy()) {
            return ((EntityActionPolicy) getComponent(ctx.getActionPolicyName())).executePreAction(ctx);
        }

        return null;
    }

    private EntityActionResult executeEntityPostActionPolicy(Database tdb, EntityActionContext ctx)
            throws UnifyException {
        if (ctx.isWithSweepingCommitPolicy()) {
            ctx.getSweepingCommitPolicy().bumpAllParentVersions(tdb, ctx.getActionType());
        }

        if (ctx.isWithActionPolicy()) {
            return ((EntityActionPolicy) getComponent(ctx.getActionPolicyName())).executePostAction(ctx);
        }

        return new EntityActionResult(ctx);
    }
}
