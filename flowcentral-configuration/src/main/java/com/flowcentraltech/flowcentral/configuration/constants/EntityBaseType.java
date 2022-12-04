/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.configuration.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Entity base type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_ENTITYBASETYPE")
@StaticList(name = "entitybasetypelist", description = "$m{staticlist.entitybasetypelist}")
public enum EntityBaseType implements EnumConst {

    BASE_ENTITY(
            "ENI"),
    BASE_VERSION_ENTITY(
            "ENT"),
    BASE_AUDIT_ENTITY(
            "ADE"),
    BASE_STATUS_ENTITY(
            "STA"),
    BASE_WORK_ENTITY(
            "WKE"),
    BASE_STATUS_WORK_ENTITY(
            "SWK"),
    BASE_NAMED_ENTITY(
            "NME"),
    BASE_CONFIG_ENTITY(
            "CGE"),
    BASE_CONFIG_NAMED_ENTITY(
            "CNE"),
    BASE_APPLICATION_ENTITY(
            "APE");

    private final String code;

    private EntityBaseType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return BASE_ENTITY.code;
    }

    public boolean isAuditType() {
        return !BASE_ENTITY.equals(this) && !BASE_VERSION_ENTITY.equals(this);
    }

    public boolean isWorkEntityType() {
        return BASE_WORK_ENTITY.equals(this) || BASE_STATUS_WORK_ENTITY.equals(this);
    }

    public static EntityBaseType fromCode(String code) {
        return EnumUtils.fromCode(EntityBaseType.class, code);
    }

    public static EntityBaseType fromName(String name) {
        return EnumUtils.fromName(EntityBaseType.class, name);
    }
}
