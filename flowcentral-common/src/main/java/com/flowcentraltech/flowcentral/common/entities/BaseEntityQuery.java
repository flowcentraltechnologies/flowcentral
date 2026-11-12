/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.common.entities;

import java.util.Collection;

import com.tcdng.unify.core.criterion.CompoundRestriction;
import com.tcdng.unify.core.system.entities.AbstractSequenceEntity;
import com.tcdng.unify.core.system.entities.SequenceEntityQuery;

/**
 * Base query object for base entity sub-classes.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class BaseEntityQuery<T extends AbstractSequenceEntity> extends SequenceEntityQuery<T> {

    public BaseEntityQuery(Class<T> entityClass) {
        super(entityClass);
    }

    public BaseEntityQuery(Class<T> entityClass, boolean applyAppQueryLimit) {
        super(entityClass, applyAppQueryLimit);
    }

    public BaseEntityQuery(Class<T> entityClass, CompoundRestriction restrictions, boolean applyAppQueryLimit) {
        super(entityClass, restrictions, applyAppQueryLimit);
    }

    public BaseEntityQuery<T> id(Long id) {
        return (BaseEntityQuery<T>) equals("id", id);
    }

    public BaseEntityQuery<T> idNot(Long id) {
        return (BaseEntityQuery<T>) notequals("id", id);
    }

    public BaseEntityQuery<T> idIn(Collection<Long> ids) {
        return (BaseEntityQuery<T>) amongst("id", ids);
    }

    public BaseEntityQuery<T> idNotIn(Collection<Long> ids) {
        return (BaseEntityQuery<T>) notamongst("id", ids);
    }

    public BaseEntityQuery<T> reserved() {
        return (BaseEntityQuery<T>) less("id", 0L);
    }

    public BaseEntityQuery<T> notReserved() {
        return (BaseEntityQuery<T>) greater("id", 0L);
    }

    public BaseEntityQuery<T> orderById() {
        return (BaseEntityQuery<T>) order("id");
    }

    public BaseEntityQuery<T> selectId() {
        return (BaseEntityQuery<T>) select("id");
    }
}
