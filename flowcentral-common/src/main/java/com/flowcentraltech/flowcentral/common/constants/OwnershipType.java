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
package com.flowcentraltech.flowcentral.common.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Ownership type enumeration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_OWNERSHIPTYPE")
@StaticList(name = "ownershiptypelist", description = "$m{staticlist.ownershiptypelist}")
public enum OwnershipType implements EnumConst {

    GLOBAL(
            "G"),
    USER(
            "U");

    private final String code;

    private OwnershipType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return GLOBAL.code;
    }

    public static OwnershipType fromCode(String code) {
        return EnumUtils.fromCode(OwnershipType.class, code);
    }

    public static OwnershipType fromName(String name) {
        return EnumUtils.fromName(OwnershipType.class, name);
    }

}
