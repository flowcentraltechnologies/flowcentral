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
package com.flowcentraltech.flowcentral.collaboration.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Frozen status enumeration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_FROZENSTATUS")
@StaticList(name = "frozenstatuslist", description = "$m{staticlist.frozenstatuslist}")
public enum FrozenStatus implements EnumConst {

    FROZEN(
            "FZ"),
    UNFROZEN(
            "UZ");

    private final String code;

    private FrozenStatus(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return UNFROZEN.code;
    }

    public static FrozenStatus fromCode(String code) {
        return EnumUtils.fromCode(FrozenStatus.class, code);
    }

    public static FrozenStatus fromName(String name) {
        return EnumUtils.fromName(FrozenStatus.class, name);
    }

}
