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

package com.flowcentraltech.flowcentral.system.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * System color list.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@StaticList(name = "systemcolorlist", description="SystemColor List")
public enum SystemColorType implements EnumConst {

    BLACK("#000000"),
    GRAY("#7f7f7f"),
    DARKRED("#af2a2a"),
    RED("#ed3a3a"),
    ORANGE("#ff7f27"),
    YELLOW("#fff200"),
    GREEN("#22b14c"),
    TURQUOISE("#00a2e8"),
    INDIGO("#5f68cc"),
    PURPLE("#a349a4"),
    BROWN("#b97a57"),
    GOLD("#ffc90e"),
    ARMY_GREEN("#70925a"),
    BLUE_GRAY("#7092be"),
    LAVENDER("#c8bfe7");

    private final String code;
    
    private SystemColorType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return GRAY.code;
    }

    public static SystemColorType fromCode(String code) {
        return EnumUtils.fromCode(SystemColorType.class, code);
    }

    public static SystemColorType fromName(String name) {
        return EnumUtils.fromName(SystemColorType.class, name);
    }
}
