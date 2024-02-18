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
package com.flowcentraltech.flowcentral.system.business;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.common.business.EnvironmentDelegate;
import com.flowcentraltech.flowcentral.common.business.EnvironmentDelegateHolder;
import com.flowcentraltech.flowcentral.common.business.EnvironmentDelegateRegistrar;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.business.SuggestionProvider;
import com.flowcentraltech.flowcentral.common.business.policies.ChildListEditPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.business.policies.EntityListActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityListActionPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.EntityListActionResult;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.entities.EntityWrapper;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.flowcentraltech.flowcentral.configuration.constants.RecordActionType;
import com.flowcentraltech.flowcentral.system.constants.SystemModuleNameConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.business.AbstractBusinessService;
import com.tcdng.unify.core.criterion.AggregateFunction;
import com.tcdng.unify.core.criterion.GroupingFunction;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.database.Aggregation;
import com.tcdng.unify.core.database.Database;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.GroupingAggregation;
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
public class EnvironmentServiceImpl extends AbstractBusinessService implements EnvironmentService {

    @Configurable
    private SuggestionProvider suggestionProvider;

    @Configurable
    private EnvironmentDelegateRegistrar environmentDelegateRegistrar;

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
    public List<String> validate(Entity inst, EvaluationMode mode) throws UnifyException {
        Database db = db(inst.getClass());
        if (db instanceof EnvironmentDelegate) {
            return ((EnvironmentDelegate) db).validate(inst, mode);
        }

        return Collections.emptyList();
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
    public Object create(EntityWrapper wrapperInst) throws UnifyException {
        final Entity inst = wrapperInst.isIndexed() ? wrapperInst.getValueObjectAtDataIndex()
                : wrapperInst.getValueObject();
        return db(inst.getClass()).create(inst);
    }

    @Override
    public EntityActionResult create(EntityActionContext ctx) throws UnifyException {
        EntityActionResult result = executeEntityPreActionPolicy(ctx);
        if (result == null) {
            Entity inst = ctx.getInst();
            ctx.setResult(create(inst));
            if (suggestionProvider != null) {
                suggestionProvider.saveSuggestions(ctx.getEntityDef(Object.class), inst);
            }

            if (inst instanceof WorkEntity && ((WorkEntity) inst).getOriginalCopyId() != null) {
                // Update original instance workflow flag
                updateById((Class<? extends Entity>) inst.getClass(), ((WorkEntity) inst).getOriginalCopyId(),
                        new Update().add("inWorkflow", Boolean.TRUE));
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
    public <T extends Entity> void findEditableChildren(T record) throws UnifyException {
        db(record.getClass()).findEditableChildren(record);
    }

    @Override
    public <T extends Entity> void findReadOnlyChildren(T record) throws UnifyException {
        db(record.getClass()).findReadOnlyChildren(record);
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
    public <T extends Entity> void listChildren(T record) throws UnifyException {
        db(record.getClass()).listChildren(record);
    }

    @Override
    public <T extends Entity> void listEditableChildren(T record) throws UnifyException {
        db(record.getClass()).listEditableChildren(record);
    }

    @Override
    public <T extends Entity> void listReadOnlyChildren(T record) throws UnifyException {
        db(record.getClass()).listReadOnlyChildren(record);
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
    public int updateById(Entity record) throws UnifyException {
        return db(record.getClass()).updateById(record);
    }

    @Override
    public int updateByIdVersion(Entity record) throws UnifyException {
        return db(record.getClass()).updateByIdVersion(record);
    }

    @Override
    public int updateByIdVersionEditableChildren(Entity record) throws UnifyException {
        return db(record.getClass()).updateByIdVersionEditableChildren(record);
    }

    @Override
    public int updateLean(Entity record) throws UnifyException {
        return db(record.getClass()).updateLeanByIdVersion(record);
    }

    @Override
    public int updateByIdVersion(EntityWrapper wrappedInst) throws UnifyException {
        Entity inst = wrappedInst.isIndexed() ? wrappedInst.getValueObjectAtDataIndex() : wrappedInst.getValueObject();
        return db(inst.getClass()).updateByIdVersion(inst);
    }

    @Override
    public int updateLean(EntityWrapper wrappedInst) throws UnifyException {
        Entity inst = wrappedInst.isIndexed() ? wrappedInst.getValueObjectAtDataIndex() : wrappedInst.getValueObject();
        return db(inst.getClass()).updateLeanByIdVersion(inst);
    }

    @Override
    public EntityActionResult updateByIdVersion(EntityActionContext ctx) throws UnifyException {
        EntityActionResult result = executeEntityPreActionPolicy(ctx);
        if (result == null) {
            final Entity inst = ctx.getInst();
            ctx.setResult(db(inst.getClass()).updateByIdVersion(inst));
            if (suggestionProvider != null) {
                suggestionProvider.saveSuggestions(ctx.getEntityDef(Object.class), inst);
            }

            return executeEntityPostActionPolicy(db(inst.getClass()), ctx);
        }

        return result;
    }

    @Override
    public EntityActionResult updateLean(EntityActionContext ctx) throws UnifyException {
        EntityActionResult result = executeEntityPreActionPolicy(ctx);
        if (result == null) {
            final Entity inst = ctx.getInst();
            ctx.setResult(db(inst.getClass()).updateLeanByIdVersion(inst));
            if (suggestionProvider != null) {
                suggestionProvider.saveSuggestions(ctx.getEntityDef(Object.class), inst);
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
    public int updateLeanById(EntityWrapper wrappedInst) throws UnifyException {
        Entity inst = wrappedInst.isIndexed() ? wrappedInst.getValueObjectAtDataIndex() : wrappedInst.getValueObject();
        return db(inst.getClass()).updateLeanById(inst);
    }

    @Override
    public int updateLeanByIdVersion(EntityWrapper wrappedInst) throws UnifyException {
        Entity inst = wrappedInst.isIndexed() ? wrappedInst.getValueObjectAtDataIndex() : wrappedInst.getValueObject();
        return db(inst.getClass()).updateLeanByIdVersion(inst);
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
    public <T extends Number, U extends Entity> T min(Class<T> fieldClass, String fieldName, Query<U> query)
            throws UnifyException {
        return db(query.getEntityClass()).min(fieldClass, fieldName, query);
    }

    @Override
    public <T extends Number, U extends Entity> T max(Class<T> fieldClass, String fieldName, Query<U> query)
            throws UnifyException {
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
            releaseOriginalCopy(inst);
            return executeEntityPostActionPolicy(db(inst.getClass()), ctx);
        }

        return result;
    }

    @Override
    public <T extends Entity> int delete(Class<T> clazz, Object id) throws UnifyException {
        // TODO Release original copy
        return db(clazz).delete(clazz, id);
    }

    @Override
    public int deleteAll(Query<? extends Entity> query) throws UnifyException {
        return db(query.getEntityClass()).deleteAll(query);
    }

    @Override
    public int deleteByIdVersion(Entity record) throws UnifyException {
        int result = db(record.getClass()).deleteByIdVersion(record);
        releaseOriginalCopy(record);
        return result;
    }

    @Override
    public int deleteById(Entity record) throws UnifyException {
        int result = db(record.getClass()).deleteById(record);
        releaseOriginalCopy(record);
        return result;
    }

    @Override
    public int deleteById(EntityWrapper wrappedInst) throws UnifyException {
        Entity inst = wrappedInst.isIndexed() ? wrappedInst.getValueObjectAtDataIndex() : wrappedInst.getValueObject();
        int result = db(inst.getClass()).deleteById(inst);
        releaseOriginalCopy(inst);
        return result;
    }

    @Override
    public int deleteByIdVersion(EntityWrapper wrappedInst) throws UnifyException {
        Entity inst = wrappedInst.isIndexed() ? wrappedInst.getValueObjectAtDataIndex() : wrappedInst.getValueObject();
        int result = db(inst.getClass()).deleteByIdVersion(inst);
        return result;
    }

    private int releaseOriginalCopy(Entity inst) throws UnifyException {
        // TODO Put this in a policy
        if (inst instanceof WorkEntity && ((WorkEntity) inst).getOriginalCopyId() != null) {
            // Update original instance workflow flag
            return updateById((Class<? extends Entity>) inst.getClass(), ((WorkEntity) inst).getOriginalCopyId(),
                    new Update().add("inWorkflow", Boolean.FALSE));
        }

        return 0;
    }

    @Override
    public Aggregation aggregate(AggregateFunction aggregateFunction, Query<? extends Entity> query)
            throws UnifyException {
        return db_direct(query.getEntityClass()).aggregate(aggregateFunction, query);
    }

    @Override
    public List<Aggregation> aggregate(List<AggregateFunction> aggregateFunction, Query<? extends Entity> query)
            throws UnifyException {
        return db_direct(query.getEntityClass()).aggregate(aggregateFunction, query);
    }

    @Override
    public List<GroupingAggregation> aggregate(AggregateFunction aggregateFunction, Query<? extends Entity> query,
            GroupingFunction groupingFunction) throws UnifyException {
        return db_direct(query.getEntityClass()).aggregate(aggregateFunction, query, groupingFunction);
    }

    @Override
    public List<GroupingAggregation> aggregate(List<AggregateFunction> aggregateFunction, Query<? extends Entity> query,
            GroupingFunction groupingFunction) throws UnifyException {
        return db_direct(query.getEntityClass()).aggregate(aggregateFunction, query, groupingFunction);
    }

    @Override
    public List<GroupingAggregation> aggregate(AggregateFunction aggregateFunction, Query<? extends Entity> query,
            List<GroupingFunction> groupingFunction) throws UnifyException {
        return db_direct(query.getEntityClass()).aggregate(aggregateFunction, query, groupingFunction);
    }

    @Override
    public List<GroupingAggregation> aggregate(List<AggregateFunction> aggregateFunction, Query<? extends Entity> query,
            List<GroupingFunction> groupingFunction) throws UnifyException {
        return db_direct(query.getEntityClass()).aggregate(aggregateFunction, query, groupingFunction);
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
    public <T> T valueOptional(Class<T> valueType, String valueFieldName, Query<? extends Entity> query)
            throws UnifyException {
        return db(query.getEntityClass()).valueOptional(valueType, valueFieldName, query);
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
        EnvironmentDelegateHolder delegateInfo = environmentDelegateRegistrar
                .getEnvironmentDelegateInfo(entityLongName);
        return delegateInfo != null ? delegateInfo.getDataSourceName() : db().getDataSourceName();
    }

    @Override
    public String getEntityDataSourceName(Class<? extends Entity> entityClass) throws UnifyException {
        EnvironmentDelegateHolder delegateInfo = environmentDelegateRegistrar.getEnvironmentDelegateInfo(entityClass);
        return delegateInfo != null ? delegateInfo.getDataSourceName() : db().getDataSourceName();
    }

    private Database db(Class<? extends Entity> entityClass) throws UnifyException {
        EnvironmentDelegateHolder delegateInfo = environmentDelegateRegistrar.getEnvironmentDelegateInfo(entityClass);
        return delegateInfo != null ? (delegateInfo.isDirect() ? db(delegateInfo.getDataSourceName())
                : delegateInfo.getEnvironmentDelegate()) : db();
    }

    private Database db_direct(Class<? extends Entity> entityClass) throws UnifyException {
        EnvironmentDelegateHolder delegateInfo = environmentDelegateRegistrar.getEnvironmentDelegateInfo(entityClass);
        return delegateInfo != null ? db(delegateInfo.getDataSourceName()) : db();
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
