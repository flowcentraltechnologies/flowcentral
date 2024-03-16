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

package com.flowcentraltech.flowcentral.system.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Delegate redirect type list.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@StaticList(name = "delegateredirecttypelist", description="Delegate Redirect Type List")
public enum DelegateRedirectType implements EnumConst {

    ENTITY("ENT"),
    PROCEDURE("PRC"),
    ALL("ALL");

    private final String code;
    
    private DelegateRedirectType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return ENTITY.code;
    }

    public boolean isEntityOnly() {
        return ENTITY.equals(this);
    }

    public boolean isProcedureOnly() {
        return PROCEDURE.equals(this);
    }

    public boolean isAll() {
        return ALL.equals(this);
    }
    
    public static DelegateRedirectType fromCode(String code) {
        return EnumUtils.fromCode(DelegateRedirectType.class, code);
    }

    public static DelegateRedirectType fromName(String name) {
        return EnumUtils.fromName(DelegateRedirectType.class, name);
    }
}
