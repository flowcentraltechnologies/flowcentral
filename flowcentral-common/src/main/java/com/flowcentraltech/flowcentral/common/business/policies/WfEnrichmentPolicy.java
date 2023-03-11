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

import com.flowcentraltech.flowcentral.common.business.RuleListComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.data.ValueStoreWriter;

/**
 * Workflow enrichment policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface WfEnrichmentPolicy extends RuleListComponent {

    /**
     * Enriches workflow entity instance using supplied reader and writer.
     * 
     * @param wfItemWriter
     *                           workflow item writer
     * @param wfItemReader
     *                           workflow item reader
     * @param rule
     *                           the enrichment rule to use (optional)
     * @throws UnifyException
     *                        if an error occurs
     */
    void enrich(ValueStoreWriter wfItemWriter, ValueStoreReader wfItemReader, String rule) throws UnifyException;
}
