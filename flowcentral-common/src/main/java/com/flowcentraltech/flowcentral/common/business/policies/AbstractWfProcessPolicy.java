/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.constants.ProcessErrorConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Convenient abstract base class for workflow process policies.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractWfProcessPolicy extends AbstractFlowCentralComponent implements WfProcessPolicy {

    @Configurable
    private EnvironmentService environment;

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }
    
    protected final EnvironmentService environment() {
        return environment;
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
