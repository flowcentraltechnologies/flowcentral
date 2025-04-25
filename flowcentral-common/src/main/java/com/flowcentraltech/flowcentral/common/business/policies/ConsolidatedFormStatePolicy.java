/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.common.data.TargetFormTabStates;
import com.flowcentraltech.flowcentral.common.data.TargetFormWidgetStates;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Consolidated form state policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface ConsolidatedFormStatePolicy extends FlowCentralComponent {

    /**
     * Performs auto updates.
     * 
     * @param instValueStore
     *                       the form bean value store
     * @return true if entity backed by value store was updated otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean performAutoUpdates(ValueStore instValueStore) throws UnifyException;
    
    /**
     * Executed on form construction.
     * 
     * @param instValueStore
     *                       the form value store
     * @throws UnifyException
     *                        if an error occurs
     */
    void onFormConstruct(ValueStore instValueStore) throws UnifyException;
    
    /**
     * Executed on form widget switch.
     * 
     * @param instValueStore
     *                       the form value store
     * @param trigger
     *                       optional form trigger
     * @throws UnifyException
     *                        if an error occurs
     */
    void onFormSwitch(ValueStore instValueStore, String trigger) throws UnifyException;
    
    /**
     * Perform the form tab states.
     * 
     * @param reader
     *                       the object value store reader
     * @param trigger
     *                       optional form trigger
     * @return the form state result
     * @throws UnifyException
     *                        if an error occurs
     */
    TargetFormTabStates evaluateTabStates(ValueStoreReader reader, String trigger) throws UnifyException;

    /**
     * Perform the form widget states.
     * 
     * @param reader
     *                       the object value store reader
     * @param trigger
     *                       optional form trigger
     * @return the form state result
     * @throws UnifyException
     *                        if an error occurs
     */
    TargetFormWidgetStates evaluateWidgetStates(ValueStoreReader reader, String trigger) throws UnifyException;
}
