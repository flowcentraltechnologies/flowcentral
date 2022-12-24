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
package com.flowcentraltech.flowcentral.application.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;

/**
 * Convenient abstract base class for mapped entity providers..
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractMappedEntityProvider<T extends Entity> extends AbstractUnifyComponent
        implements MappedEntityProvider<T> {

    @Configurable
    private AppletUtilities au;

    private Class<T> destEntityClass;
    
    private final String srcEntityName;

    private final Map<String, String> queryFieldMap;

    protected AbstractMappedEntityProvider(Class<T> destEntityClass, String srcEntityName, Map<String, String> queryFieldMap) {
        this.destEntityClass = destEntityClass;
        this.srcEntityName = srcEntityName;
        this.queryFieldMap = Collections.unmodifiableMap(new HashMap<String, String>(queryFieldMap));
    }

    public final void setAu(AppletUtilities au) {
        this.au = au;
    }

    @Override
    public Class<T> getDestEntityClass() {
        return destEntityClass;
    }

    @Override
    public String srcEntity() {
        return srcEntityName;
    }

    @Override
    public T find(Long id) throws UnifyException {
        Entity record = environment().find(getSrcEntityClass(), id);
        return create(record);
    }

    @Override
    public T find(Long id, long versionNo) throws UnifyException {
        Entity record = environment().find(getSrcEntityClass(), id);
        return create(record);
    }

    @Override
    public T find(Query<T> query) throws UnifyException {
        Entity record = environment().find(convertQuery(query));
        return create(record);
    }

    @Override
    public T findLean(Long id) throws UnifyException {
        Entity record = environment().findLean(getSrcEntityClass(), id);
        return create(record);
    }

    @Override
    public T findLean(Long id, long versionNo) throws UnifyException {
        Entity record = environment().findLean(getSrcEntityClass(), id);
        return create(record);
    }

    @Override
    public T findLean(Query<T> query) throws UnifyException {
        Entity record = environment().findLean(convertQuery(query));
        return create(record);
    }

    @Override
    public List<T> findAll(Query<T> query) throws UnifyException {
        List<? extends Entity> instList = environment().findAll(convertQuery(query));
        return createList(instList);
    }

    @Override
    public T list(Long id) throws UnifyException {
        Entity record = environment().list(getSrcEntityClass(), id);
        return create(record);
    }

    @Override
    public T list(Long id, long versionNo) throws UnifyException {
        Entity record = environment().list(getSrcEntityClass(), id);
        return create(record);
    }

    @Override
    public T list(Query<T> query) throws UnifyException {
        Entity record = environment().list(convertQuery(query));
        return create(record);
    }

    @Override
    public T listLean(Long id) throws UnifyException {
        Entity record = environment().listLean(getSrcEntityClass(), id);
        return create(record);
    }

    @Override
    public T listLean(Long id, long versionNo) throws UnifyException {
        Entity record = environment().listLean(getSrcEntityClass(), id);
        return create(record);
    }

    @Override
    public T listLean(Query<T> query) throws UnifyException {
        Entity record = environment().listLean(convertQuery(query));
        return create(record);
    }

    @Override
    public List<T> listAll(Query<T> query) throws UnifyException {
        List<? extends Entity> instList = environment().listAll(convertQuery(query));
        return createList(instList);
    }

    @Override
    public int countAll(Query<T> query) throws UnifyException {
        return environment().countAll(convertQuery(query));
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

    protected abstract T doCreate(Entity inst) throws UnifyException;

    @SuppressWarnings("unchecked")
    private Class<T> getSrcEntityClass() throws UnifyException {
        return (Class<T>) au.getEntityClassDef(srcEntityName).getEntityClass();
    }

    private Query<? extends Entity> convertQuery(Query<T> query) throws UnifyException {
        final Query<? extends Entity> _query = Query.of(getSrcEntityClass());
        _query.ignoreEmptyCriteria(query.isIgnoreEmptyCriteria());
        _query.ignoreTenancy(query.isIgnoreTenancy());
        if (!query.isEmptyCriteria()) {
            Restriction restriction = query.getRestrictions();
            restriction.fieldSwap(queryFieldMap);
            _query.addRestriction(restriction);
        }

        return _query;
    }

    private List<T> createList(List<? extends Entity> instList) throws UnifyException {
        List<T> resultList = new ArrayList<>();
        for (Entity inst : instList) {
            T _inst = create(inst);
            resultList.add(_inst);
        }

        return resultList;
    }

    private T create(Entity inst) throws UnifyException {
        if (inst != null) {
            return doCreate(inst);
        }

        return null;
    }

}
