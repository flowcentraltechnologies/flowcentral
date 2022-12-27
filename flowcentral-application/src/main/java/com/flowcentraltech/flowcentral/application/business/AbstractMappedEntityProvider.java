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
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.ReflectUtils;

/**
 * Convenient abstract base class for mapped entity providers..
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractMappedEntityProvider<U extends BaseMappedEntityProviderContext>
        extends AbstractUnifyComponent implements MappedEntityProvider<U> {

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

    public final void setAu(AppletUtilities au) {
        this.au = au;
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
    public Entity find(Long id) throws UnifyException {
        Entity record = environment().find(getSrcEntityClass(), id);
        return create(record);
    }

    @Override
    public Entity find(Long id, long versionNo) throws UnifyException {
        Entity record = environment().find(getSrcEntityClass(), id);
        return create(record);
    }

    @Override
    public Entity find(Query<? extends Entity> query) throws UnifyException {
        Entity record = environment().find(convertQuery(query));
        return create(record);
    }

    @Override
    public Entity findLean(Long id) throws UnifyException {
        Entity record = environment().findLean(getSrcEntityClass(), id);
        return create(record);
    }

    @Override
    public Entity findLean(Long id, long versionNo) throws UnifyException {
        Entity record = environment().findLean(getSrcEntityClass(), id);
        return create(record);
    }

    @Override
    public Entity findLean(Query<? extends Entity> query) throws UnifyException {
        Entity record = environment().findLean(convertQuery(query));
        return create(record);
    }

    @Override
    public List<? extends Entity> findAll(Query<? extends Entity> query) throws UnifyException {
        List<? extends Entity> instList = environment().findAll(convertQuery(query));
        return createList(instList);
    }

    @Override
    public Entity list(Long id) throws UnifyException {
        Entity record = environment().list(getSrcEntityClass(), id);
        return create(record);
    }

    @Override
    public Entity list(Long id, long versionNo) throws UnifyException {
        Entity record = environment().list(getSrcEntityClass(), id);
        return create(record);
    }

    @Override
    public Entity list(Query<? extends Entity> query) throws UnifyException {
        Entity record = environment().list(convertQuery(query));
        return create(record);
    }

    @Override
    public Entity listLean(Long id) throws UnifyException {
        Entity record = environment().listLean(getSrcEntityClass(), id);
        return create(record);
    }

    @Override
    public Entity listLean(Long id, long versionNo) throws UnifyException {
        Entity record = environment().listLean(getSrcEntityClass(), id);
        return create(record);
    }

    @Override
    public Entity listLean(Query<? extends Entity> query) throws UnifyException {
        Entity record = environment().listLean(convertQuery(query));
        return create(record);
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

    protected abstract void doMappedCopy(U context, Entity destInst, Entity srcInst) throws UnifyException;

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
            for (Entity srcInst : srcInstList) {
                Entity destInst = ReflectUtils.newInstance(destEntityClass);
                doMappedCopy(context, destInst, srcInst);
                resultList.add(destInst);
            }

            return resultList;
        }

        return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    private Entity create(Entity inst) throws UnifyException {
        if (inst != null) {
            Class<? extends Entity> destEntityClass = (Class<? extends Entity>) au.getEntityClassDef(destEntityName)
                    .getEntityClass();
            U context = ReflectUtils.newInstance(contextClass);
            Entity destInst = ReflectUtils.newInstance(destEntityClass);
            doMappedCopy(context, destInst, inst);
            return destInst;
        }

        return null;
    }

}
