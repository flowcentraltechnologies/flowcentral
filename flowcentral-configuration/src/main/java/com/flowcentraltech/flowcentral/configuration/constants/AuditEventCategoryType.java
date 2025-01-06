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

import java.util.Arrays;
import java.util.List;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Audit event category type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_AUDITEVENTCATTYPE")
@StaticList(name = "auditeventcattypelist", description = "$m{staticlist.auditeventcattypelist}")
public enum AuditEventCategoryType implements EnumConst {

    VIEW(
            "VIW",
            Arrays.asList(AuditEventType.VIEW, AuditEventType.VIEW_DRAFT)),
    CREATE(
            "CRE",
            Arrays.asList(AuditEventType.CREATE, AuditEventType.CREATE_NEXT, AuditEventType.CREATE_CLOSE,
                    AuditEventType.CREATE_DRAFT, AuditEventType.CREATE_SUBMIT)),
    UPDATE(
            "UPD",
            Arrays.asList(AuditEventType.UPDATE, AuditEventType.UPDATE_NEXT, AuditEventType.UPDATE_CLOSE,
                    AuditEventType.UPDATE_SUBMIT, AuditEventType.UPDATE_DRAFT)),
    DELETE(
            "DEL",
            Arrays.asList(AuditEventType.DELETE, AuditEventType.DELETE_SUBMIT, AuditEventType.DELETE_DRAFT));

    private final String code;

    private final List<AuditEventType> eventTypes;

    private AuditEventCategoryType(String code, List<AuditEventType> eventTypes) {
        this.code = code;
        this.eventTypes = eventTypes;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return VIEW.code;
    }

    public List<AuditEventType> eventTypes() {
        return eventTypes;
    }

    public boolean isView() {
        return VIEW.equals(this);
    }

    public boolean isCreate() {
        return CREATE.equals(this);
    }

    public boolean isUpdate() {
        return UPDATE.equals(this);
    }

    public boolean isDelete() {
        return DELETE.equals(this);
    }

    public static AuditEventCategoryType fromCode(String code) {
        return EnumUtils.fromCode(AuditEventCategoryType.class, code);
    }

    public static AuditEventCategoryType fromName(String name) {
        return EnumUtils.fromName(AuditEventCategoryType.class, name);
    }
}
