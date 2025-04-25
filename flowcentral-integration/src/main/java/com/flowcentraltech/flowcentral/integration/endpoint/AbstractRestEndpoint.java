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
package com.flowcentraltech.flowcentral.integration.endpoint;

import com.flowcentraltech.flowcentral.integration.constants.RestEndpointConstants;
import com.flowcentraltech.flowcentral.integration.data.EndpointDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Parameters;

/**
 * Abstract REST end-point.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Parameters({
    @Parameter(name = RestEndpointConstants.CREDENTIAL_NAME, description = "$m{restendpoint.credential}",
    editor = "!ui-select list:$s{credentiallist} listKey:$s{name} blankOption:$s{}", type = String.class,
    order = 32) })
public abstract class AbstractRestEndpoint extends AbstractEndpoint implements RestEndpoint {

    @Override
    public void setup(EndpointDef endpointDef) throws UnifyException {

    }
}
