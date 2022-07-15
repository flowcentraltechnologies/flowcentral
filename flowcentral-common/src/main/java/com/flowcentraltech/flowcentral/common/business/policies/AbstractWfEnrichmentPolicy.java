/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.common.constants.ProcessErrorConstants;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Convenient abstract base class for workflow enrichment policies.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractWfEnrichmentPolicy extends AbstractUnifyComponent implements WfEnrichmentPolicy {

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    /**
     * Sets the process error document.
     * 
     * @param wfItemReader
     *                     the workflow item reader
     * @param errorDoc
     *                     the error document
     * @throws UnifyException
     *                        if an error occurs
     */
    protected void setErrorDoc(ValueStoreReader wfItemReader, Object errorDoc) throws UnifyException {
        if (errorDoc != null) {
            wfItemReader.setTempValue(ProcessErrorConstants.ERROR_DOC, errorDoc);
        }
    }
}
