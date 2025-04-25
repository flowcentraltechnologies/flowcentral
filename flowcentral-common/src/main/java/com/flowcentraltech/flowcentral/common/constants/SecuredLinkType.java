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

package com.flowcentraltech.flowcentral.common.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Secured link type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_SECUREDLINKTYPE")
@StaticList(name = "securedlinktypelist", description = "$m{staticlist.securedlinktypelist}")
public enum SecuredLinkType implements EnumConst {

    LOGIN(
            "LGI"),
    WORKFLOW_DECISION(
            "WFD"),
    OPEN(
            "OPN");

    private final String code;

    private SecuredLinkType(String code) {
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

    public boolean isLogin() {
        return LOGIN.equals(this);
    }

    public boolean isWorkflowDecision() {
        return WORKFLOW_DECISION.equals(this);
    }

    public boolean isOpen() {
        return OPEN.equals(this);
    }
    
    public static SecuredLinkType fromCode(String code) {
        return EnumUtils.fromCode(SecuredLinkType.class, code);
    }

    public static SecuredLinkType fromName(String name) {
        return EnumUtils.fromName(SecuredLinkType.class, name);
    }

}
