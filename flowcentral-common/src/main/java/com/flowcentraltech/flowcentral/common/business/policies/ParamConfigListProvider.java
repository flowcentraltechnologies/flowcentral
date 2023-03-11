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

package com.flowcentraltech.flowcentral.common.business.policies;

import java.util.List;

import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ParamConfig;
import com.tcdng.unify.core.database.Entity;

/**
 * Parameter configuration list provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface ParamConfigListProvider extends UnifyComponent {

    /**
     * Gets the parameter configuration list category based on supplied entity
     * instance.
     * 
     * @param entityInst
     *                   the entity instance
     * @return the category
     * @throws UnifyException
     *                        if an error occurs
     */
    String getCategory(Entity entityInst) throws UnifyException;

    /**
     * Get parameter configuration list based on supplied entity instance.
     * 
     * @param entityInst
     *                   the entity instance
     * @return list of parameter configurations
     * @throws UnifyException
     *                        if an error occurs
     */
    List<ParamConfig> getParamConfigList(Entity entityInst) throws UnifyException;
}
