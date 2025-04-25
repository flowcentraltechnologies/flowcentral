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

package com.flowcentraltech.flowcentral.application.data;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.common.data.VersionedEntityDef;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.common.database.WorkEntity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.JsonObjectComposition;
import com.tcdng.unify.core.util.ReflectUtils;

/**
 * Entity class definition
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EntityClassDef implements VersionedEntityDef {

    private EntityDef entityDef;

    private Class<?> entityClass;

    private List<String> copyFields;

    public EntityClassDef(EntityDef entityDef, Class<?> entityClass) {
        this.entityDef = entityDef;
        this.entityClass = entityClass;
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public JsonObjectComposition getJsonComposition(AppletUtilities au) throws UnifyException {
        return entityDef.getJsonComposition(au);
    }

    public List<String> validate(AppletUtilities au, Entity inst) throws UnifyException {
        return entityDef.validate(au, inst);
    }
    
    @Override
    public Long getId() {
        return entityDef.getId();
    }

    @Override
    public long getVersion() {
        return entityDef.getVersion();
    }

    @SuppressWarnings("unchecked")
    public <T> T newInst() throws UnifyException {
        return (T) ReflectUtils.newInstance(entityClass);
    }

    public <T> T newInst(T srcInst) throws UnifyException {
        T destInst = newInst();
        ReflectUtils.shallowBeanCopy(destInst, srcInst, getCopyFields());
        return destInst;
    }

    public <T> void copy(T destInst, T srcInst) throws UnifyException {
        ReflectUtils.shallowBeanCopy(destInst, srcInst, getCopyFields());
    }

    public String getLongName() {
        return entityDef.getLongName();
    }

    public String getDelegate() {
        return entityDef.getDelegate();
    }
    
    public boolean delegated() {
        return entityDef.delegated();
    }

    public EntityFieldDef getTenantIdDef() {
        return entityDef.getTenantIdDef();
    }

    public boolean isWithTenantId() {
        return entityDef.isWithTenantId();
    }

    public boolean isWorkType() {
        return entityDef.isWorkType();
    }

    public boolean isCompatible(WorkEntity inst) {
        if (inst != null) {
            return entityClass.getName().equals(inst.getClass().getName());
        }

        return false;
    }

    private List<String> getCopyFields() {
        if (copyFields == null) {
            copyFields = new ArrayList<String>();
            for (EntityFieldDef entityFieldDef : entityDef.getFieldDefList()) {
                if (!entityFieldDef.isBase() && !entityFieldDef.isScratch()) {
                    copyFields.add(entityFieldDef.getFieldName());
                }
            }
        }

        return copyFields;
    }
}
