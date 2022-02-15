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

package com.flowcentraltech.flowcentral.application.data;

import java.util.ArrayList;
import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.ReflectUtils;

/**
 * Entity class definition
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityClassDef {

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

    public Long getId() {
        return entityDef.getId();
    }

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
    
    @Override
    public String toString() {
        return "EntityClassDef [entityDef.getLongName()=" + entityDef.getLongName() + ", entityDef.getVersion()="
                + entityDef.getVersion() + ", entityClass=" + entityClass + "]";
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
