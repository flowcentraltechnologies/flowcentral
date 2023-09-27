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

import java.util.List;

import com.flowcentraltech.flowcentral.application.data.Usage;
import com.flowcentraltech.flowcentral.application.data.UsageType;
import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Usage list provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface UsageListProvider extends FlowCentralComponent {

    /**
     * Finds usages.
     * 
     * @param instReader
     *                   entity instance reader
     * @param usageType
     *                   optional usage type restriction
     * @return list of usages
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Usage> findUsages(ValueStoreReader instReader, UsageType usageType) throws UnifyException;

    /**
     * Count usages.
     * 
     * @param instReader
     *                   entity instance reader
     * @param usageType
     *                   optional usage type restriction
     * @return the usage count
     * @throws UnifyException
     *                        if an error occurs
     */
    long countUsages(ValueStoreReader instReader, UsageType usageType) throws UnifyException;
}
