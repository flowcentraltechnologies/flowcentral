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
package com.flowcentraltech.flowcentral.common.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Session parameter type enumeration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_SESSIONPARAMTYPE")
@StaticList(name = "sessionparamtypelist", description = "$m{staticlist.sessionparamtypelist}")
public enum SessionParamType implements EnumConst {

    USERNAME(
            "USN", FlowCentralSessionAttributeConstants.USERNAME),
    USERLOGINID(
            "USL", FlowCentralSessionAttributeConstants.USERLOGINID),
    BRANCHCODE(
            "BRC", FlowCentralSessionAttributeConstants.BRANCHCODE),
    BRANCHDESC(
            "BRD", FlowCentralSessionAttributeConstants.BRANCHDESC),
    DEPARTMENTCODE(
            "DPC", FlowCentralSessionAttributeConstants.DEPARTMENTCODE),
    ROLECODE(
            "RLC", FlowCentralSessionAttributeConstants.ROLECODE),
    ROLEDESCRIPTION(
            "RLD", FlowCentralSessionAttributeConstants.ROLEDESCRIPTION);

    private final String code;

    private final String attribute;

    private SessionParamType(String code, String attribute) {
        this.code = code;
        this.attribute = attribute;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return ROLECODE.code;
    }
    
    public String attribute() {
        return attribute;
    }
    
    public static SessionParamType fromCode(String code) {
        return EnumUtils.fromCode(SessionParamType.class, code);
    }

    public static SessionParamType fromName(String name) {
        return EnumUtils.fromName(SessionParamType.class, name);
    }

}
