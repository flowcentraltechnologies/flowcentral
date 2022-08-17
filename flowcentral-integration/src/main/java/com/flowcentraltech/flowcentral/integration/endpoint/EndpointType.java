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
package com.flowcentraltech.flowcentral.integration.endpoint;

import com.tcdng.unify.convert.constants.EnumConst;
import com.tcdng.unify.core.annotation.StaticList;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Transport type enumeration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@StaticList(name = "endpointtypelist", description = "$m{staticlist.endpointtypelist}")
public enum EndpointType implements EnumConst {

    FILE(
            "FLE"),
    JMS(
            "JMS");

    private final String code;

    private EndpointType(String code) {
        this.code = code;
    }

    @Override
    public String defaultCode() {
        return JMS.code();
    }

    @Override
    public String code() {
        return this.code;
    }

    public static EndpointType fromCode(String code) {
        return EnumUtils.fromCode(EndpointType.class, code);
    }

    public static EndpointType fromName(String name) {
        return EnumUtils.fromName(EndpointType.class, name);
    }

    public static EndpointType fromType(Class<? extends Endpoint> type) {
        if (JmsEndpoint.class.isAssignableFrom(type)) {
            return JMS;
        }

        if (FileEndpoint.class.isAssignableFrom(type)) {
            return FILE;
        }

        return null;
    }
}
