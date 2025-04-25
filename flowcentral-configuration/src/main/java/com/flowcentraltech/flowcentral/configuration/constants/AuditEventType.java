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
package com.flowcentraltech.flowcentral.configuration.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Audit event type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_AUDITEVENTTYPE")
@StaticList(name = "auditeventtypelist", description = "$m{staticlist.auditeventtypelist}")
public enum AuditEventType implements EnumConst {

    VIEW(
            "VIW"),
    VIEW_DRAFT(
            "VID"),
    VIEW_PHANTOM(
            "VIP"),
    CREATE(
            "CRE"),
    CREATE_NEXT(
            "CRN"),
    CREATE_CLOSE(
            "CRC"),
    CREATE_DRAFT(
            "CRD"),
    CREATE_SUBMIT(
            "CRS"),
    UPDATE(
            "UPD"),
    UPDATE_NEXT(
            "UPN"),
    UPDATE_CLOSE(
            "UPC"),
    UPDATE_SUBMIT(
            "UPS"),
    UPDATE_DRAFT(
            "UDD"),
    DELETE(
            "DEL"),
    DELETE_SUBMIT(
            "DES"),
    DELETE_DRAFT(
            "DED");

    private final String code;

    private AuditEventType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return VIEW.code;
    }

    public boolean isPhantom() {
        return VIEW_PHANTOM.equals(this);
    }

    public boolean isView() {
        return VIEW.equals(this) || VIEW_DRAFT.equals(this) || VIEW_PHANTOM.equals(this);
    }

    public boolean isCreate() {
        return CREATE.equals(this) || CREATE_NEXT.equals(this) || CREATE_DRAFT.equals(this) || CREATE_CLOSE.equals(this)
                || CREATE_SUBMIT.equals(this);
    }

    public boolean isUpdate() {
        return UPDATE.equals(this) || UPDATE_NEXT.equals(this) || UPDATE_DRAFT.equals(this) || UPDATE_CLOSE.equals(this)
                || UPDATE_SUBMIT.equals(this);
    }

    public boolean isDelete() {
        return DELETE.equals(this) || DELETE_DRAFT.equals(this) || DELETE_SUBMIT.equals(this);
    }

    public static AuditEventType fromCode(String code) {
        return EnumUtils.fromCode(AuditEventType.class, code);
    }

    public static AuditEventType fromName(String name) {
        return EnumUtils.fromName(AuditEventType.class, name);
    }
}
