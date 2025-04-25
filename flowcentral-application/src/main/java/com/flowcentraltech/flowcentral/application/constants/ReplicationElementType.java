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

package com.flowcentraltech.flowcentral.application.constants;

import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Replication element type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public enum ReplicationElementType implements EnumConst {
    
    NAME(
            "NME"),
    COMPONENT(
            "CMP"),
    MESSAGE(
            "MSG"),
    CLASS(
            "CLS"),
    ENTITY(
            "ENT"),
    FIELD(
            "FLD"),
    TABLE(
            "TBL"),
    AUTOFORMAT(
            "AUT");

    private final String code;

    private ReplicationElementType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return NAME.code;
    }

    public static ReplicationElementType fromCode(String code) {
        return EnumUtils.fromCode(ReplicationElementType.class, code);
    }

    public static ReplicationElementType fromName(String name) {
        return EnumUtils.fromName(ReplicationElementType.class, name);
    }
}
