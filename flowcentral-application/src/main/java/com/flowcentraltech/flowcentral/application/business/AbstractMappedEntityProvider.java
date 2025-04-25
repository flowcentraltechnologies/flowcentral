/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.criterion.Order;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.BeanValueListStore;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.ReflectUtils;

/**
 * Convenient abstract base class for mapped entity providers..
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractMappedEntityProvider<U extends BaseMappedEntityProviderContext>
        extends AbstractFlowCentralComponent implements MappedEntityProvider<U> {

    @Configurable
    private AppletUtilities au;

    private final Class<U> contextClass;

    private final String destEntityName;

    private final String srcEntityName;

    private final Map<String, String> queryFieldMap;

    protected AbstractMappedEntityProvider(Class<U> contextClass, String destEntityName, String srcEntityName,
            Map<String, String> queryFieldMap) {
        this.contextClass = contextClass;
        this.destEntityName = destEntityName;
        this.srcEntityName = srcEntityName;
        this.queryFieldMap = Collections.unmodifiableMap(new HashMap<String, String>(queryFieldMap));
    }

    @Override
    public boolean isEntitiesDef() throws UnifyException {
        return au.isEntityDef(srcEntityName) && au.isEntityDef(destEntityName);
    }

    @Override
    public String destEntity() {
        return destEntityName;
    }

    @Override
    public String srcEntity() {
        return srcEntityName;
    }

    @Override
    public Object create(Entity dest) throws UnifyException {
        Entity srcInst = createSrcInst(dest);
        return srcInst != null ? environment().create(srcInst) : null;
    }

    @Override
    public int updateById(Entity dest) throws UnifyException {
        Entity srcInst = createSrcInst(dest);
        return srcInst != null ? environment().updateById(srcInst) : 0;
    }

    @Override
    public int updateByIdVersion(Entity dest) throws UnifyException {
        Entity srcInst = createSrcInst(dest);
        return srcInst != null ? environment().updateByIdVersion(srcInst) : 0;
    }

    @Override
    public Entity find(Long id) throws UnifyException {
        Entity record = environment().find(getSrcEntityClass(), id);
        return createDestInst(record);
    }

    @Override
    public Entity find(Long id, long versionNo) throws UnifyException {
        Entity record = environment().find(getSrcEntityClass(), id);
        return createDestInst(record);
    }

    @Override
    public Entity find(Query<? extends Entity> query) throws UnifyException {
        Entity record = environment().find(convertQuery(query));
        return createDestInst(record);
    }

    @Override
    public Entity findLean(Long id) throws UnifyException {
        Entity record = environment().findLean(getSrcEntityClass(), id);
        return createDestInst(record);
    }

    @Override
    public Entity findLean(Long id, long versionNo) throws UnifyException {
        Entity record = environment().findLean(getSrcEntityClass(), id);
        return createDestInst(record);
    }

    @Override
    public Entity findLean(Query<? extends Entity> query) throws UnifyException {
        Entity record = environment().findLean(convertQuery(query));
        return createDestInst(record);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> List<T> findAll(Query<T> query) throws UnifyException {
        List<? extends Entity> instList = environment().findAll(convertQuery(query));
        return (List<T>) createList(instList);
    }

    @Override
    public Entity list(Long id) throws UnifyException {
        Entity record = environment().list(getSrcEntityClass(), id);
        return createDestInst(record);
    }

    @Override
    public Entity list(Long id, long versionNo) throws UnifyException {
        Entity record = environment().list(getSrcEntityClass(), id);
        return createDestInst(record);
    }

    @Override
    public Entity list(Query<? extends Entity> query) throws UnifyException {
        Entity record = environment().list(convertQuery(query));
        return createDestInst(record);
    }

    @Override
    public Entity listLean(Long id) throws UnifyException {
        Entity record = environment().listLean(getSrcEntityClass(), id);
        return createDestInst(record);
    }

    @Override
    public Entity listLean(Long id, long versionNo) throws UnifyException {
        Entity record = environment().listLean(getSrcEntityClass(), id);
        return createDestInst(record);
    }

    @Override
    public Entity listLean(Query<? extends Entity> query) throws UnifyException {
        Entity record = environment().listLean(convertQuery(query));
        return createDestInst(record);
    }

    @Override
    public List<? extends Entity> listAll(Query<? extends Entity> query) throws UnifyException {
        List<? extends Entity> instList = environment().listAll(convertQuery(query));
        return createList(instList);
    }

    @Override
    public int countAll(Query<? extends Entity> query) throws UnifyException {
        return environment().countAll(convertQuery(query));
    }

    @Override
    public int deleteAll(Query<? extends Entity> query) throws UnifyException {
        return environment().deleteAll(convertQuery(query));
    }

    @Override
    public <T> List<T> valueList(Class<T> fieldClass, String fieldName, Query<? extends Entity> query)
            throws UnifyException {
        return environment().valueList(fieldClass, fieldName, convertQuery(query));
    }

    @Override
    public <T> T value(Class<T> fieldClass, String fieldName, Query<? extends Entity> query) throws UnifyException {
        return environment().value(fieldClass, fieldName, convertQuery(query));
    }

    @Override
    public <T, V extends Entity> Set<T> valueSet(Class<T> fieldClass, String fieldName, Query<V> query)
            throws UnifyException {
        return environment().valueSet(fieldClass, fieldName, convertQuery(query));
    }

    @Override
    public <T, V> Map<T, V> valueMap(Class<T> keyClass, String keyName, Class<V> valueClass, String valueName,
            Query<? extends Entity> query) throws UnifyException {
        return environment().valueMap(keyClass, keyName, valueClass, valueName, convertQuery(query));
    }

    @Override
    public <T, V> Map<T, List<V>> valueListMap(Class<T> keyClass, String keyName, Class<V> valueClass, String valueName,
            Query<? extends Entity> query) throws UnifyException {
        return environment().valueListMap(keyClass, keyName, valueClass, valueName, convertQuery(query));
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    protected AppletUtilities au() {
        return au;
    }

    protected ApplicationModuleService application() {
        return au.application();
    }

    protected EnvironmentService environment() {
        return au.environment();
    }

    protected abstract void doMappedCopyFromSrcToDest(U context, ValueStore destValueStore, ValueStore srcValueStore)
            throws UnifyException;

    protected abstract void doMappedCopyFromDestToSrc(U context, ValueStore srcValueStore, ValueStore destValueStore)
            throws UnifyException;

    @SuppressWarnings("unchecked")
    private Class<? extends Entity> getSrcEntityClass() throws UnifyException {
        return (Class<? extends Entity>) au.getEntityClassDef(srcEntityName).getEntityClass();
    }

    private Query<? extends Entity> convertQuery(Query<? extends Entity> query) throws UnifyException {
        final Query<? extends Entity> _query = Query.of(getSrcEntityClass());
        _query.ignoreEmptyCriteria(query.isIgnoreEmptyCriteria());
        _query.ignoreTenancy(query.isIgnoreTenancy());
        if (!query.isEmptyCriteria()) {
            Restriction restriction = query.getRestrictions();
            restriction.fieldSwap(queryFieldMap);
            _query.addRestriction(restriction);
        }

        if (query.getOrder() != null) {
            Order _order = query.getOrder().copy();
            _order.fieldSwap(queryFieldMap);
            _query.setOrder(_order);
        }

        return _query;
    }

    @SuppressWarnings("unchecked")
    private List<? extends Entity> createList(List<? extends Entity> srcInstList) throws UnifyException {
        if (!DataUtils.isBlank(srcInstList)) {
            Class<? extends Entity> destEntityClass = (Class<? extends Entity>) au.getEntityClassDef(destEntityName)
                    .getEntityClass();
            U context = ReflectUtils.newInstance(contextClass);
            context.setMultiple(true);
            List<Entity> resultList = new ArrayList<>();
            ValueStore srcValueStore = new BeanValueListStore(srcInstList);
            final int len = srcValueStore.size();
            for (int i = 0; i < len; i++) {
                srcValueStore.setDataIndex(i);
                Entity destInst = ReflectUtils.newInstance(destEntityClass);
                doMappedCopyFromSrcToDest(context, new BeanValueStore(destInst), srcValueStore);
                resultList.add(destInst);
            }

            return resultList;
        }

        return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    private Entity createDestInst(Entity srcInst) throws UnifyException {
        if (srcInst != null) {
            Class<? extends Entity> destEntityClass = (Class<? extends Entity>) au.getEntityClassDef(destEntityName)
                    .getEntityClass();
            U context = ReflectUtils.newInstance(contextClass);
            Entity destInst = ReflectUtils.newInstance(destEntityClass);
            doMappedCopyFromSrcToDest(context, new BeanValueStore(destInst), new BeanValueStore(srcInst));
            return destInst;
        }

        return null;
    }

    private Entity createSrcInst(Entity destInst) throws UnifyException {
        if (destInst != null) {
            Class<? extends Entity> srcEntityClass = getSrcEntityClass();
            U context = ReflectUtils.newInstance(contextClass);
            Entity srcInst = ReflectUtils.newInstance(srcEntityClass);
            doMappedCopyFromDestToSrc(context, new BeanValueStore(srcInst), new BeanValueStore(destInst));
            return srcInst;
        }

        return null;
    }

}
