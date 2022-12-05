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

package com.flowcentraltech.flowcentral.common.business.policies;

import java.util.List;

import com.flowcentraltech.flowcentral.common.data.FormMessages;
import com.flowcentraltech.flowcentral.common.data.PageLoadDetails;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;

/**
 * Child list edit policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface ChildListEditPolicy extends EntryTablePolicy {

    /**
     * Gets on load details.
     * 
     * @param entityClass
     *                      the entity class
     * @param baseFieldName
     *                      the base field name
     * @param baseId
     *                      the base ID
     * @return the page load details
     * @throws UnifyException
     *                        if an error occurs
     */
    PageLoadDetails getOnLoadDetails(Class<? extends Entity> entityClass, String baseFieldName, Object baseId)
            throws UnifyException;

    /**
     * Executes at the end of an assignment update.
     * 
     * @param entityClass
     *                      the entity class
     * @param baseFieldName
     *                      the base field name
     * @param baseId
     *                      the base (parent) object ID
     * @throws UnifyException
     *                        if an error occurs
     */
    void postAssignmentUpdate(Class<? extends Entity> entityClass, String baseFieldName, Object baseId)
            throws UnifyException;

    /**
     * Executes at the end of a entry table update.
     * 
     * @param entityClass
     *                      the entity class
     * @param baseFieldName
     *                      the base field name
     * @param baseId
     *                      the base (parent) object ID
     * @param instList
     *                      the entry list
     * @throws UnifyException
     *                        if an error occurs
     */
    void postEntryUpdate(Class<? extends Entity> entityClass, String baseFieldName, Object baseId, List<?> instList)
            throws UnifyException;

    /**
     * Validate entries,
     * 
     * @param entityClass
     *                      the entity class
     * @param baseFieldName
     *                      the base field name
     * @param baseId
     *                      the base (parent) object ID
     * @param instList
     *                      the entry list
     * @return Error message
     * @throws UnifyException
     *                        if an error occurs
     */
    FormMessages validateEntries(Class<? extends Entity> entityClass, String baseFieldName, Object baseId,
            List<?> instList) throws UnifyException;
}
