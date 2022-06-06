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

import java.util.Set;

import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Entry table policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface EntryTablePolicy extends UnifyComponent {

    /**
     * Handles on entry table data load.
     * 
     * @param tableValueStore
     *                        the list data value store object
     * @param selected
     *                        selected item index
     * @throws UnifyException
     *                        if an error occurs
     */
    void onEntryTableLoad(ValueStore tableValueStore, Set<Integer> selected) throws UnifyException;

    /**
     * Handles on entry table data change.
     * 
     * @param tableValueStore
     *                        the list data value store object
     * @param selected
     *                        selected item index
     * @throws UnifyException
     *                        if an error occurs
     */
    void onEntryTableChange(ValueStore tableValueStore, Set<Integer> selected) throws UnifyException;

    /**
     * Handles on entry table data change.
     * 
     * @param tableValueStore
     *                        the list data store object
     * @param rowIndex
     *                        the row index
     * @throws UnifyException
     *                        if an error occurs
     */
    void onEntryRowChange(ValueStore tableValueStore, int rowIndex) throws UnifyException;

    /**
     * Apply table state overrides.
     * 
     * @param rowValueStore
     *                           the value store for the current row item
     * @param tableStateOverride
     *                           the table state override
     * @throws UnifyException
     *                        if an error occurs
     */
    void applyTableStateOverride(ValueStore rowValueStore, TableStateOverride tableStateOverride) throws UnifyException;

    /**
     * Resolves the table action index for the supplied value store.
     * 
     * @param valueStore
     *                   the value store
     * @param index
     *                   the value store index
     * @param size
     *                   the number of records in entry table
     * @return the action index
     * @throws UnifyException
     *                        if an error occurs
     */
    int resolveActionIndex(ValueStore valueStore, int index, int size) throws UnifyException;
}
