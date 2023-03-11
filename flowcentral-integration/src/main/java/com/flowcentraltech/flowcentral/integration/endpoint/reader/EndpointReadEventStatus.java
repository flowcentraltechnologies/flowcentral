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
package com.flowcentraltech.flowcentral.integration.endpoint.reader;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * End-point read event status constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@StaticList(name = "endpointreadeventstatuslist", description = "$m{staticlist.endpointreadeventstatuslist}")
public enum EndpointReadEventStatus implements EnumConst {

    UNPROCESSED(
            "U"),
    SUCCESSFUL(
            "S"),
    FAILED(
            "F");

    private final String code;

    private EndpointReadEventStatus(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return UNPROCESSED.code();
    }

    public static EndpointReadEventStatus fromCode(String code) {
        return EnumUtils.fromCode(EndpointReadEventStatus.class, code);
    }

    public static EndpointReadEventStatus fromName(String name) {
        return EnumUtils.fromName(EndpointReadEventStatus.class, name);
    }
}
