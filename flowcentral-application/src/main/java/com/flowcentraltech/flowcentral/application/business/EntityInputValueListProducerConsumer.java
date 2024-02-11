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

import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.InputValue;
import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Entity input value list producer-consumer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface EntityInputValueListProducerConsumer extends FlowCentralComponent {

    /**
     * Produces an input value list.
     * 
     * @param entityDef
     *                         the entity definition;
     * @param parentValueStore
     *                         the parent entity values tore
     * @return the produced input value list
     * @throws UnifyException
     *                        if an error occurs
     */
    List<InputValue> produce(EntityDef entityDef, ValueStore parentValueStore) throws UnifyException;

    /**
     * Consumes an input value list.
     * 
     * @param entityDef
     *                         the entity definition;
     * @param parentValueStore
     *                         the parent entity value store
     * @param values
     *                         the values to consume
     * @throws UnifyException
     *                        if an error occurs
     */
    void consume(EntityDef entityDef, ValueStore parentValueStore, Map<Object, InputValue> values)
            throws UnifyException;
}
