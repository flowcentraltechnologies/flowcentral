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

package com.flowcentraltech.flowcentral.application.business;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Entity matcher.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface EntityMatcher extends FlowCentralComponent {

    /**
     * Matches an entity.
     * 
     * @param entityDef
     *                   the entity definition.
     * @param evaluationMode the evaluation mode
     * @param valueStore
     *                   the entity value store
     * @return true if matched, otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean match(EntityDef entityDef, EvaluationMode evaluationMode, ValueStore valueStore) throws UnifyException;
}
