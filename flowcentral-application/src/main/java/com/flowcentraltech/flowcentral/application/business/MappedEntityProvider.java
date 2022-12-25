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

import java.util.List;

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
public interface MappedEntityProvider<T extends Entity, U extends MappedEntityProviderContext> extends UnifyComponent {

    Class<T> getDestEntityClass();

    String srcEntity();

    T find(Long id) throws UnifyException;

    T find(Long id, long versionNo) throws UnifyException;

    T find(Query<T> query) throws UnifyException;

    T findLean(Long id) throws UnifyException;

    T findLean(Long id, long versionNo) throws UnifyException;

    T findLean(Query<T> query) throws UnifyException;

    List<T> findAll(Query<T> query) throws UnifyException;

    T list(Long id) throws UnifyException;

    T list(Long id, long versionNo) throws UnifyException;

    T list(Query<T> query) throws UnifyException;

    T listLean(Long id) throws UnifyException;

    T listLean(Long id, long versionNo) throws UnifyException;

    T listLean(Query<T> query) throws UnifyException;

    List<T> listAll(Query<T> query) throws UnifyException;

    int countAll(Query<T> query) throws UnifyException;
}
