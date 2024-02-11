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

package com.flowcentraltech.flowcentral.application.web.panels;

import java.util.List;

import com.flowcentraltech.flowcentral.common.business.policies.EntryTablePolicy;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Inline CRUD table policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface InlineCRUDTablePolicy<T extends InlineCRUDEntry> extends EntryTablePolicy {

    /**
     * Executed on item addition.
     * 
     * @param parentReader
     *                     optional parent value store reader
     * @param itemList
     *                     the target items list
     * @param item
     *                     the item to added
     * @throws UnifyException
     *                        if an error occurs
     */
    void onAddItem(ValueStoreReader parentReader, List<T> itemList, T item) throws UnifyException;
}
