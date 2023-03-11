/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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
 * Form review type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_FORMREVIEWTYPE")
@StaticList(name = "formreviewtypelist", description = "$m{staticlist.formreviewtypelist}")
public enum FormReviewType implements EnumConst {

    ON_SAVE(
            "ONS", false),
    ON_SAVE_NEXT(
            "OSN", true),
    ON_SAVE_CLOSE(
            "OSC", true),
    ON_UPDATE(
            "ONU", false),
    ON_UPDATE_CLOSE(
            "OUC", true),
    ON_DELETE(
            "OND", true),
   ON_SUBMIT(
            "ONB", true),
    ON_SUBMIT_NEXT(
            "OBN", true),
    ON_CLOSE(
            "OCL", true);

    private final String code;

    private final boolean formClosedOrReplaced;
    
    private FormReviewType(String code, boolean formClosedOrReplaced) {
        this.code = code;
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

    public boolean formClosedOrReplaced() {
        return formClosedOrReplaced;
    }
    
    public static FormReviewType fromCode(String code) {
        return EnumUtils.fromCode(FormReviewType.class, code);
    }

    public static FormReviewType fromName(String name) {
        return EnumUtils.fromName(FormReviewType.class, name);
    }
}
