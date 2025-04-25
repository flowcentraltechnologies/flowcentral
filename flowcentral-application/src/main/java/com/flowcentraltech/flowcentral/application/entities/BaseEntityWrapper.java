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
package com.flowcentraltech.flowcentral.application.entities;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.common.entities.EntityWrapper;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.BeanValueListStore;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Base entity wrapper.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class BaseEntityWrapper implements EntityWrapper {

    protected final EntityClassDef entityClassDef;

    protected final ValueStore valueStore;

    public BaseEntityWrapper(EntityClassDef entityClassDef) throws UnifyException {
        this.entityClassDef = entityClassDef;
        this.valueStore = new BeanValueStore(entityClassDef.newInst());
    }

    public BaseEntityWrapper(EntityClassDef entityClassDef, ValueStore valueStore) {
        Class<?> valueStoreClass = valueStore.getValueObject().getClass();
        if (!Collection.class.isAssignableFrom(valueStoreClass)
                && !valueStoreClass.getName().equals(entityClassDef.getEntityClass().getName())
                && !valueStoreClass.isAssignableFrom(entityClassDef.getEntityClass())) {
            throw new IllegalArgumentException(
                    "Supplied value store entity instance is incompatible with entity class definition.");
        }

        this.entityClassDef = entityClassDef;
        this.valueStore = valueStore;
    }

    public BaseEntityWrapper(EntityClassDef entityClassDef, Entity inst) {
        if (!inst.getClass().getName().equals(entityClassDef.getEntityClass().getName())
                && !inst.getClass().isAssignableFrom(entityClassDef.getEntityClass())) {
            throw new IllegalArgumentException(
                    "Supplied entity instance is incompatible with entity class definition.");
        }

        this.entityClassDef = entityClassDef;
        this.valueStore = new BeanValueStore(inst);
    }

    public BaseEntityWrapper(EntityClassDef entityClassDef, List<? extends Entity> instList) {
        if (!DataUtils.isBlank(instList)
                && !instList.get(0).getClass().getName().equals(entityClassDef.getEntityClass().getName())
                && !instList.get(0).getClass().isAssignableFrom(entityClassDef.getEntityClass())) {
            throw new IllegalArgumentException(
                    "Supplied entity instance is incompatible with entity class definition.");
        }

        this.entityClassDef = entityClassDef;
        this.valueStore = new BeanValueListStore(instList);
    }

    @Override
    public ValueStore getValueStore() {
        return valueStore;
    }

    @Override
    public Entity getValueObject() {
        return (Entity) valueStore.getValueObject();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<? extends Entity> getValueListObject() {
        return (List<? extends Entity>) valueStore.getValueObject();
    }

    @Override
    public Entity getValueObjectAtDataIndex() {
        return (Entity) valueStore.getValueObjectAtDataIndex();
    }

    @Override
    public boolean isIndexed() {
        return valueStore.isIndexed();
    }

    @Override
    public int getDataIndex() {
        return valueStore.getDataIndex();
    }

    @Override
    public void setDataIndex(int dataIndex) {
        valueStore.setDataIndex(dataIndex);
    }

    @Override
    public boolean isPresent() {
        return size() > 0;
    }

    @Override
    public int size() {
        return valueStore.size();
    }

    @Override
    public int compare(EntityWrapper source) throws UnifyException {
        return valueStore.compare(source.getValueStore());
    }

    @Override
    public int compare(EntityWrapper source, String... inclusionFieldNames) throws UnifyException {
        return valueStore.compare(source.getValueStore(), inclusionFieldNames);
    }

    @Override
    public int compare(EntityWrapper source, Collection<String> inclusionFieldNames) throws UnifyException {
        return valueStore.compare(source.getValueStore(), inclusionFieldNames);
    }

    @Override
    public int compare(EntityWrapper source, Map<String, String> inclusionMapping) throws UnifyException {
        return valueStore.compare(source.getValueStore(), inclusionMapping);
    }

    @Override
    public void copy(EntityWrapper source) throws UnifyException {
        valueStore.copy(source.getValueStore());
    }

    @Override
    public void copyWithExclusions(EntityWrapper source, String... exclusionFieldNames) throws UnifyException {
        valueStore.copyWithExclusions(source.getValueStore(), exclusionFieldNames);
    }

    @Override
    public void copyWithInclusions(EntityWrapper source, String... inclusionFieldNames) throws UnifyException {
        valueStore.copyWithInclusions(source.getValueStore(), inclusionFieldNames);
    }

    @Override
    public void copyWithExclusions(EntityWrapper source, Collection<String> exclusionFieldNames) throws UnifyException {
        valueStore.copyWithExclusions(source.getValueStore(), exclusionFieldNames);
    }

    @Override
    public void copyWithInclusions(EntityWrapper source, Collection<String> inclusionFieldNames) throws UnifyException {
        valueStore.copyWithInclusions(source.getValueStore(), inclusionFieldNames);
    }
}
