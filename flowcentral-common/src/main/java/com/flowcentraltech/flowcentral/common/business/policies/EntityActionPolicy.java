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

package com.flowcentraltech.flowcentral.common.business.policies;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;

/**
 * Entity action policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface EntityActionPolicy extends FlowCentralComponent {

    /**
     * Checks if entity action applies to instance.
     * 
     * @param inst
     *             the entity instance
     * @return true if action applies otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean checkAppliesTo(Entity inst) throws UnifyException;

    /**
     * Executes a pre-action operation.
     * 
     * @param ctx
     *            the entity action context
     * @return an entity action result. if null action proceeds otherwise action is
     *         not executed
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityActionResult executePreAction(EntityActionContext ctx) throws UnifyException;

    /**
     * Executes a post-action operation.
     * 
     * @param ctx
     *            the entity action context
     * @return the result object
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityActionResult executePostAction(EntityActionContext ctx) throws UnifyException;
}
