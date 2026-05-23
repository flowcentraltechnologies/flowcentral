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

package com.flowcentraltech.flowcentral.application.business;

import java.util.Optional;

import com.flowcentraltech.flowcentral.application.data.EnumerationDef;
import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Dynamic list external accessibility provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface DynamicListExternalAccessibilityProvider extends FlowCentralComponent {

    /**
     * Checks if a dynamic list enumeration is stale.
     * 
     * @param longName
     *                       the dynamic list name
     * @param enumerationDef
     *                       the current enumeration definition
     * @return true if stale otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    Optional<Boolean> isStale(String longName, EnumerationDef enumerationDef) throws UnifyException;

    /**
     * Gets dynamic list enumeration definition.
     * 
     * @param longName
     *                 the dynamic list name
     * @return the optional enumeration definition
     * @throws UnifyException
     *                        if an error occurs
     */
    Optional<EnumerationDef> getEnumerationDef(String longName) throws UnifyException;

}
