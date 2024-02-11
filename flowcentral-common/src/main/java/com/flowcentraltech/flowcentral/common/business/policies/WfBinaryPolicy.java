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

import com.flowcentraltech.flowcentral.common.business.RuleListComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Workflow binary policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface WfBinaryPolicy extends RuleListComponent {

    /**
     * Executes a process using data from supplied value store reader.
     * 
     * @param entity the work item entity.
     * 
     * @param wfItemReader
     *                           workflow item reader
     * @param rule
     *                           the binary rule to use (optional)
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean evaluate(ValueStoreReader wfItemReader, String entity, String rule) throws UnifyException;
}
