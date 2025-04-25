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

package com.flowcentraltech.flowcentral.application.business;

import com.flowcentraltech.flowcentral.common.business.RuleListComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Entity based filter generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface EntityBasedFilterGenerator extends RuleListComponent {

    /**
     * Generates restriction base on supplied entity instance.
     * 
     * @param entityInstReader
     *                         entity instance reader
     * @param rule
     *                         the process rule to use (optional)
     * @throws UnifyException
     *                        if an error occurs
     */
    Restriction generate(ValueStoreReader entityInstReader, String rule) throws UnifyException;
}
