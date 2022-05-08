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
     * @param valueStore
     *                   the data value store object
     * @param selected
     *                   selected item index
     * @throws UnifyException
     *                        if an error occurs
     */
    void onEntryTableLoad(ValueStore valueStore, Set<Integer> selected) throws UnifyException;

    /**
     * Handles on entry table data change.
     * 
     * @param valueStore
     *                   the data value store object
     * @param selected
     *                   selected item index
     * @throws UnifyException
     *                        if an error occurs
     */
    void onEntryTableChange(ValueStore valueStore, Set<Integer> selected) throws UnifyException;
}
