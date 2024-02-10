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
package com.flowcentraltech.flowcentral.integration.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntityPolicy;
import com.flowcentraltech.flowcentral.integration.endpoint.Endpoint;
import com.flowcentraltech.flowcentral.integration.endpoint.EndpointType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.database.Entity;

/**
 * End-point configuration entity policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("endpointconfig-entitypolicy")
public class EndpointConfigPolicy extends BaseStatusEntityPolicy {

    @Override
    public Object preCreate(Entity record, Date now) throws UnifyException {
        setTransportType(record);
        return super.preCreate(record, now);
    }

    @Override
    public void preUpdate(Entity record, Date now) throws UnifyException {
        setTransportType(record);
        super.preUpdate(record, now);
    }

    @SuppressWarnings("unchecked")
    private void setTransportType(Entity record) throws UnifyException {
        EndpointConfig endpointConfig = (EndpointConfig) record;
        Class<? extends Endpoint> type = (Class<? extends Endpoint>) this
                .getComponentConfig(Endpoint.class, endpointConfig.getEndpoint()).getType();
        endpointConfig.setEndpointType(EndpointType.fromType(type));
    }
}
