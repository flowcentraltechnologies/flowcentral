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

package com.flowcentraltech.flowcentral.delegate.business.policy;

import com.flowcentraltech.flowcentral.connect.common.constants.FlowCentralInterconnectConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.IOUtils;

/**
 * Convenient abstract base class for HTTP post delegate workflow enrichment
 * policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractHttpPostDelegateWfEnrichmentPolicy extends AbstractDelegateWfEnrichmentPolicy {

    public AbstractHttpPostDelegateWfEnrichmentPolicy(String operation) {
        super(operation);
    }

    @Override
    protected String sendToDelegateProcedureService(String jsonReq, String endpoint) throws UnifyException {
        return extractResult(IOUtils.postJsonToEndpoint(
                endpoint + FlowCentralInterconnectConstants.INTERCONNECT_CONTROLLER + "/procedure", jsonReq));
    }

}
