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
package com.flowcentraltech.flowcentral.common.entities;

import com.tcdng.unify.core.criterion.CompoundRestriction;

/**
 * Base query object for base tenant entity sub-classes.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class BaseTenantEntityQuery<T extends BaseTenantEntity> extends BaseEntityQuery<T> {

    public BaseTenantEntityQuery(Class<T> entityClass, boolean applyAppQueryLimit) {
        super(entityClass, applyAppQueryLimit);
    }

    public BaseTenantEntityQuery(Class<T> entityClass, CompoundRestriction restrictions,
            boolean applyAppQueryLimit) {
        super(entityClass, restrictions, applyAppQueryLimit);
    }

    public BaseTenantEntityQuery(Class<T> entityClass) {
        super(entityClass);
    }

    public final BaseTenantEntityQuery<T> tenantId(Long tenantId) {
        return (BaseTenantEntityQuery<T>) addEquals("tenantId", tenantId);
    }

}
