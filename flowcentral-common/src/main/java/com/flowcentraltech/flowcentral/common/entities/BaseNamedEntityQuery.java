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
package com.flowcentraltech.flowcentral.common.entities;

import java.util.Collection;

import com.tcdng.unify.core.criterion.CompoundRestriction;

/**
 * Base query object for base named entity sub-classes.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class BaseNamedEntityQuery<T extends BaseNamedEntity> extends BaseAuditEntityQuery<T> {

    public BaseNamedEntityQuery(Class<T> entityClass) {
        super(entityClass);
    }

    public BaseNamedEntityQuery(Class<T> entityClass, boolean applyAppQueryLimit) {
        super(entityClass, applyAppQueryLimit);
    }

    public BaseNamedEntityQuery(Class<T> entityClass, CompoundRestriction restrictions, boolean applyAppQueryLimit) {
        super(entityClass, restrictions, applyAppQueryLimit);
    }

    public final BaseNamedEntityQuery<T> name(String name) {
        return (BaseNamedEntityQuery<T>) addEquals("name", name);
    }

    public final BaseNamedEntityQuery<T> nameIn(Collection<String> name) {
        return (BaseNamedEntityQuery<T>) addAmongst("name", name);
    }

    public final BaseNamedEntityQuery<T> nameLike(String name) {
        return (BaseNamedEntityQuery<T>) addLike("name", name);
    }

    public final BaseNamedEntityQuery<T> descriptionLike(String description) {
        return (BaseNamedEntityQuery<T>) addLike("description", description);
    }

}
