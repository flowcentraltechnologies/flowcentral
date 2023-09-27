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

package com.flowcentraltech.flowcentral.common.business;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;

/**
 * Environment delegate registrar.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface EnvironmentDelegateRegistrar extends FlowCentralComponent {

    /**
     * Gets environment delegate information by entity long name.
     * 
     * @param entityLongName
     *                       the entity long name
     * @return the delegate information
     * @throws UnifyException
     *                        if an error occurs
     */
    EnvironmentDelegateHolder getEnvironmentDelegateInfo(String entityLongName) throws UnifyException;

    /**
     * Gets environment delegate information by entity class.
     * 
     * @param entityClass
     *                    the entity class
     * @return the delegate information
     * @throws UnifyException
     *                        if an error occurs
     */
    EnvironmentDelegateHolder getEnvironmentDelegateInfo(Class<? extends Entity> entityClass) throws UnifyException;

    /**
     * Resolves long name for supplied entity class.
     * 
     * @param entityClass
     *                    the entity class
     * @return the long name
     * @throws UnifyException
     *                        if an error occurs
     */
    String resolveLongName(Class<? extends Entity> entityClass) throws UnifyException;

}
