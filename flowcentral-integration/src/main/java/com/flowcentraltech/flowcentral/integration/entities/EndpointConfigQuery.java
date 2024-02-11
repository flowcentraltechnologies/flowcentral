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

import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntityQuery;
import com.flowcentraltech.flowcentral.integration.endpoint.EndpointType;

/**
 * End-point configuration query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EndpointConfigQuery extends BaseStatusEntityQuery<EndpointConfig> {

    public EndpointConfigQuery() {
        super(EndpointConfig.class);
    }

    public EndpointConfigQuery name(String name) {
        return (EndpointConfigQuery) addEquals("name", name);
    }

    public EndpointConfigQuery nameStartsWith(String name) {
        return (EndpointConfigQuery) addIBeginsWith("name", name);
    }

    public EndpointConfigQuery descriptionLike(String description) {
        return (EndpointConfigQuery) addLike("description", description);
    }

    public EndpointConfigQuery endpointType(EndpointType endpointType) {
        return (EndpointConfigQuery) addEquals("endpointType", endpointType);
    }
}
