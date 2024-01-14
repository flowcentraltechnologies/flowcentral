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
package com.flowcentraltech.flowcentral.integration.business;

import com.flowcentraltech.flowcentral.common.business.FlowCentralService;
import com.flowcentraltech.flowcentral.integration.data.EndpointDef;
import com.tcdng.unify.core.UnifyException;

/**
 * Integration module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface IntegrationModuleService extends FlowCentralService {

    /**
     * Get an end-point using supplied name.
     * 
     * @param endpointConfigName
     *                           the end-point configuration name
     * @return the end-point
     * @throws UnifyException
     *                        if an error occurs
     */
    EndpointDef getEndpointDef(String endpointConfigName) throws UnifyException;

}
