/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.integration.web.lists;

import com.flowcentraltech.flowcentral.integration.endpoint.EndpointType;
import com.tcdng.unify.core.list.AbstractListParam;

/**
 * List end-point tipe value parameter.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EndpointTypeParam extends AbstractListParam {

    private EndpointType value;

    public EndpointTypeParam(EndpointType value) {
        this.value = value;
    }

    public EndpointType getValue() {
        return value;
    }

    @Override
    public boolean isPresent() {
        return value != null;
    }
}
