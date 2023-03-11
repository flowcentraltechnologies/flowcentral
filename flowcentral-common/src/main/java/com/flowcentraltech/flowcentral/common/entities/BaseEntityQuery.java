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
package com.flowcentraltech.flowcentral.common.entities;

import com.tcdng.unify.core.criterion.CompoundRestriction;
import com.tcdng.unify.core.system.entities.SequencedEntityQuery;

/**
 * Base query object for base entity sub-classes.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class BaseEntityQuery<T extends BaseEntity> extends SequencedEntityQuery<T> {

    public BaseEntityQuery(Class<T> entityClass) {
        super(entityClass);
    }

    public BaseEntityQuery(Class<T> entityClass, boolean applyAppQueryLimit) {
        super(entityClass, applyAppQueryLimit);
    }

    public BaseEntityQuery(Class<T> entityClass, CompoundRestriction restrictions, boolean applyAppQueryLimit) {
        super(entityClass, restrictions, applyAppQueryLimit);
    }

    public final BaseEntityQuery<T> excludeSysRecords() {
        return (BaseEntityQuery<T>) addGreaterThan("id", Long.valueOf(0L));
    }
}
