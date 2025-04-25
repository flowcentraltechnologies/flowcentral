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
package com.flowcentraltech.flowcentral.configuration.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Form review type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_FORMREVIEWTYPE")
@StaticList(name = "formreviewtypelist", description = "$m{staticlist.formreviewtypelist}")
public enum FormReviewType implements EnumConst {

    ON_SAVE(
            "ONS",
            AuditEventType.CREATE,
            false),
    ON_SAVE_NEXT(
            "OSN",
            AuditEventType.CREATE_NEXT,
            true),
    ON_SAVE_CLOSE(
            "OSC",
            AuditEventType.CREATE_CLOSE,
            true),
    ON_UPDATE(
            "ONU",
            AuditEventType.UPDATE,
            false),
    ON_UPDATE_CLOSE(
            "OUC",
            AuditEventType.UPDATE_CLOSE,
            true),
    ON_DELETE(
            "OND",
            AuditEventType.DELETE,
            true),
    ON_SUBMIT(
            "ONB",
            AuditEventType.CREATE_SUBMIT,
            true),
    ON_SUBMIT_NEXT(
            "OBN",
            AuditEventType.CREATE_SUBMIT,
            true),
    ON_CLOSE(
            "OCL",
            null,
            true);

    private final String code;

    private final AuditEventType auditEventType;

    private final boolean formClosedOrReplaced;

    private FormReviewType(String code, AuditEventType auditEventType, boolean formClosedOrReplaced) {
        this.code = code;
        this.auditEventType = auditEventType;
        this.formClosedOrReplaced = formClosedOrReplaced;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return ON_SAVE.code;
    }

    public AuditEventType auditEventType() {
        return auditEventType;
    }

    public boolean formClosedOrReplaced() {
        return formClosedOrReplaced;
    }

    public boolean formClosedOrReplacedAndNotDeleted() {
        return formClosedOrReplaced && !FormReviewType.ON_DELETE.equals(this);
    }

    public boolean isOnSave() {
        return ON_SAVE.equals(this);
    }

    public boolean isOnSaveNextSave() {
        return ON_SAVE_NEXT.equals(this);
    }

    public boolean isOnSaveClose() {
        return ON_SAVE_CLOSE.equals(this);
    }

    public boolean isOfSave() {
        return ON_SAVE.equals(this) || ON_SAVE_NEXT.equals(this) || ON_SAVE_CLOSE.equals(this);
    }

    public boolean isOnUpdate() {
        return ON_UPDATE.equals(this);
    }

    public boolean isOnUpdateClose() {
        return ON_UPDATE_CLOSE.equals(this);
    }

    public boolean isOfUpdate() {
        return ON_UPDATE.equals(this) || ON_UPDATE_CLOSE.equals(this);
    }

    public boolean isOnSubmit() {
        return ON_SUBMIT.equals(this);
    }

    public boolean isOnSubmitNext() {
        return ON_SUBMIT_NEXT.equals(this);
    }

    public boolean isOfSubmit() {
        return ON_SUBMIT.equals(this) || ON_SUBMIT_NEXT.equals(this);
    }

    public boolean isOnClose() {
        return ON_CLOSE.equals(this);
    }

    public boolean isOnDelete() {
        return ON_DELETE.equals(this);
    }

    public static FormReviewType fromCode(String code) {
        return EnumUtils.fromCode(FormReviewType.class, code);
    }

    public static FormReviewType fromName(String name) {
        return EnumUtils.fromName(FormReviewType.class, name);
    }
}
