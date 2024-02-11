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

package com.flowcentraltech.flowcentral.configuration.constants;

import com.flowcentraltech.flowcentral.connect.configuration.constants.ConnectEntityBaseType;
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
            "ENI",
            false,
            false,
            false),
    BASE_VERSION_ENTITY(
            "ENT",
            false,
            false,
            false),
    BASE_AUDIT_ENTITY(
            "ADE",
            true,
            false,
            false),
    BASE_STATUS_ENTITY(
            "STA",
            true,
            false,
            false),
    BASE_WORK_ENTITY(
            "WKE",
            true,
            true,
            false),
    BASE_STATUS_WORK_ENTITY(
            "SWK",
            true,
            true,
            false),
    BASE_NAMED_ENTITY(
            "NME",
            true,
            false,
            false),
    BASE_CONFIG_ENTITY(
            "CGE",
            true,
            false,
            false),
    BASE_CONFIG_NAMED_ENTITY(
            "CNE",
            true,
            false,
            false),
    BASE_APPLICATION_ENTITY(
            "APE",
            true,
            false,
            false),
    BASE_TENANT_ENTITY(
            "ENIT",
            false,
            false,
            true),
    BASE_VERSION_TENANT_ENTITY(
            "ENTT",
            false,
            false,
            true),
    BASE_AUDIT_TENANT_ENTITY(
            "ADET",
            true,
            false,
            true),
    BASE_STATUS_TENANT_ENTITY(
            "STAT",
            true,
            false,
            true),
    BASE_WORK_TENANT_ENTITY(
            "WKET",
            true,
            true,
            true),
    BASE_STATUS_WORK_TENANT_ENTITY(
            "SWKT",
            true,
            true,
            true);

    private final String code;

    private final boolean audit;

    private final boolean work;

    private final boolean tenant;

    private EntityBaseType(String code, boolean audit, boolean work, boolean tenant) {
        this.code = code;
        this.audit = audit;
        this.work = work;
        this.tenant = tenant;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return BASE_ENTITY.code;
    }

    public boolean isTenantType() {
        return tenant;
    }

    public boolean isAuditType() {
        return audit;
    }

    public boolean isWorkEntityType() {
        return work;
    }

    public static EntityBaseType fromCode(String code) {
        return EnumUtils.fromCode(EntityBaseType.class, code);
    }

    public static EntityBaseType fromName(String name) {
        return EnumUtils.fromName(EntityBaseType.class, name);
    }

    public static EntityBaseType fromInterconnect(ConnectEntityBaseType baseType) {
        return baseType != null ? fromName(baseType.name()) : null;
    }
}
