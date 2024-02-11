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
 * Workflow process policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface WfProcessPolicy extends RuleListComponent {

    /**
     * Executes a process using data from supplied workflow entity instance reader.
     * 
     * @param wfItemReader
     *                           workflow item reader
     * @param rule
     *                           the process rule to use (optional)
     * @throws UnifyException
     *                        if an error occurs
     */
    void execute(ValueStoreReader wfItemReader, String rule) throws UnifyException;
}
