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

package com.flowcentraltech.flowcentral.common.business.policies;

import com.flowcentraltech.flowcentral.configuration.constants.AuditEventType;
import com.flowcentraltech.flowcentral.configuration.constants.FormReviewType;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Form review context.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class FormReviewContext {

    private FormReviewType reviewType;

    private String action;

    public FormReviewContext(FormReviewType reviewType, String action) {
        this.reviewType = reviewType;
        this.action = action;
    }

    public FormReviewContext(FormReviewType reviewType) {
        this.reviewType = reviewType;
    }

    public FormReviewType getReviewType() {
        return reviewType;
    }

    public AuditEventType auditEventType() {
        return reviewType.auditEventType();
    }
    
    public boolean formClosedOrReplaced() {
        return reviewType.formClosedOrReplaced();
    }
    
    public boolean formClosedOrReplacedAndNotDeleted() {
        return reviewType.formClosedOrReplacedAndNotDeleted();
    }

    public boolean isOnSave() {
        return reviewType.isOnSave();
    }

    public boolean isOnSaveNextSave() {
        return reviewType.isOnSaveNextSave();
    }

    public boolean isOnSaveClose() {
        return reviewType.isOnSaveClose();
    }

    public boolean isOfSave() {
        return reviewType.isOfSave();
    }

    public boolean isOnUpdate() {
        return reviewType.isOnUpdate();
    }

    public boolean isOnUpdateClose() {
        return reviewType.isOnUpdateClose();
    }

    public boolean isOfUpdate() {
        return reviewType.isOfUpdate();
    }

    public boolean isOnSubmit() {
        return reviewType.isOnSubmit();
    }

    public boolean isOnSubmitNext() {
        return reviewType.isOnSubmitNext();
    }

    public boolean isOfSubmit() {
        return reviewType.isOfSubmit();
    }

    public boolean isOnClose() {
        return reviewType.isOnClose();
    }

    public boolean isOnDelete() {
        return reviewType.isOnDelete();
    }

    public String getAction() {
        return action;
    }

    public boolean isWithAction() {
        return !StringUtils.isBlank(action);
    }

}
