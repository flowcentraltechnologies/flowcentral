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

import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Entity selection handler.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface EntitySelectHandler extends UnifyComponent {

    /**
     * Applies a selection to a form.
     * 
     * @param formValueStore
     *                            the form value store
     * @param selectionValueStore
     *                            the selection value store
     * @throws UnifyException
     *                        if an error occurs
     */
    void applySelection(ValueStore formValueStore, ValueStore selectionValueStore) throws UnifyException;
}
