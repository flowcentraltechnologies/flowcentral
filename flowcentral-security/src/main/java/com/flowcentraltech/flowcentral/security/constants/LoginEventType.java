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

package com.flowcentraltech.flowcentral.security.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Login event type..
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_LOGINEVENTYPE")
@StaticList(name = "logineventtypelist", description = "$m{staticlist.logineventtypelist}")
public enum LoginEventType implements EnumConst {

    LOGIN(
            "LGI"),
    LOGOUT(
            "LGO");

    private final String code;

    private LoginEventType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return LOGIN.code;
    }

    public static LoginEventType fromCode(String code) {
        return EnumUtils.fromCode(LoginEventType.class, code);
    }

    public static LoginEventType fromName(String name) {
        return EnumUtils.fromName(LoginEventType.class, name);
    }

}
