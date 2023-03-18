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
package com.flowcentraltech.flowcentral.integration.endpoint.writer;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * End-point write event status constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@StaticList(name = "endpointwriteeventstatuslist", description = "$m{staticlist.endpointwriteeventstatuslist}")
public enum EndpointWriteEventStatus implements EnumConst {

    UNPROCESSED(
            "U"),
    SUCCESSFUL(
            "S"),
    FAILED(
            "F");

    private final String code;

    private EndpointWriteEventStatus(String code) {
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

    public boolean isSuccess() {
        return SUCCESSFUL.equals(this);
    }
    
    public static EndpointWriteEventStatus fromCode(String code) {
        return EnumUtils.fromCode(EndpointWriteEventStatus.class, code);
    }

    public static EndpointWriteEventStatus fromName(String name) {
        return EnumUtils.fromName(EndpointWriteEventStatus.class, name);
    }
}
