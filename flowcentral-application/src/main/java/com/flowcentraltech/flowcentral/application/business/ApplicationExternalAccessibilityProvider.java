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

import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Application external accessibility provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface ApplicationExternalAccessibilityProvider extends FlowCentralComponent {

    /**
     * Checks if reference is stale if component exists in external provider.
     * 
     * @param longName
     *                 the reference long name
     * @param refDef
     *                 the reference object
     * @return true if expired otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    Optional<Boolean> stale(String longName, RefDef refDef) throws UnifyException;

    /**
     * Creates a reference definition if component exists in external provider.
     * 
     * @param longName
     *                 the reference long name
     * @return the reference definition
     * @throws UnifyException
     *                        if an error occurs
     */
    Optional<RefDef> createRefDef(String longName) throws UnifyException;
}
