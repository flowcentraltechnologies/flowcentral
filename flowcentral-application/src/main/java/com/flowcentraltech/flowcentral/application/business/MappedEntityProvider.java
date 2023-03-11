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
package com.flowcentraltech.flowcentral.application.business;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;

/**
 * Mapped entity provider component.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface MappedEntityProvider<T extends BaseMappedEntityProviderContext> extends UnifyComponent {

    String destEntity();

    String srcEntity();

    Entity find(Long id) throws UnifyException;

    Entity find(Long id, long versionNo) throws UnifyException;

    Entity find(Query<? extends Entity> query) throws UnifyException;

    Entity findLean(Long id) throws UnifyException;

    Entity findLean(Long id, long versionNo) throws UnifyException;

    Entity findLean(Query<? extends Entity> query) throws UnifyException;

    List<? extends Entity> findAll(Query<? extends Entity> query) throws UnifyException;

    Entity list(Long id) throws UnifyException;

    Entity list(Long id, long versionNo) throws UnifyException;

    Entity list(Query<? extends Entity> query) throws UnifyException;

    Entity listLean(Long id) throws UnifyException;

    Entity listLean(Long id, long versionNo) throws UnifyException;

    Entity listLean(Query<? extends Entity> query) throws UnifyException;

    List<? extends Entity> listAll(Query<? extends Entity> query) throws UnifyException;

    int countAll(Query<? extends Entity> query) throws UnifyException;

    <U> List<U> valueList(Class<U> fieldClass, String fieldName, Query<? extends Entity> query) throws UnifyException;

    <U> U value(Class<U> fieldClass, String fieldName, Query<? extends Entity> query) throws UnifyException;

    <U> Set<U> valueSet(Class<U> fieldClass, String fieldName, Query<? extends Entity> query) throws UnifyException;

    <U, V> Map<U, V> valueMap(Class<U> keyClass, String keyName, Class<V> valueClass, String valueName,
            Query<? extends Entity> query) throws UnifyException;

    <U, V> Map<U, List<V>> valueListMap(Class<U> keyClass, String keyName, Class<V> valueClass, String valueName,
            Query<? extends Entity> query) throws UnifyException;

}
