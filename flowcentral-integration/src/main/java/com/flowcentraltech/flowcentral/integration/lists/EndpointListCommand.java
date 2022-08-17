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
package com.flowcentraltech.flowcentral.integration.lists;

import com.flowcentraltech.flowcentral.integration.endpoint.FileEndpoint;
import com.flowcentraltech.flowcentral.integration.endpoint.JmsEndpoint;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.list.AbstractParamTypeListCommand;

/**
 * End-point list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("endpointlist")
public class EndpointListCommand extends AbstractParamTypeListCommand<EndpointTypeParam> {

    public EndpointListCommand() {
        super(EndpointTypeParam.class);
    }

    @Override
    protected Class<? extends UnifyComponent> getTypeFromParam(EndpointTypeParam params) throws UnifyException {
        if (params.isPresent()) {
            switch (params.getValue()) {
                case FILE:
                    return FileEndpoint.class;
                case JMS:
                    return JmsEndpoint.class;
                default:
                    break;
            }
        }

        return null;
    }

}
