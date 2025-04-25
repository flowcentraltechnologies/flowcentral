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

package com.flowcentraltech.flowcentral.application.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Related list edit action type list.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_RELLISTEDITACTIONTYPE")
@StaticList(name = "rellisteditactionlist", description = "$m{staticlist.rellisteditactionlist}")
public enum RelatedListEditActionType implements EnumConst {

    OPEN_ASSIGNMENT_PAGE(
            "/assignToRelatedItem");

    private final String code;

    private RelatedListEditActionType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return OPEN_ASSIGNMENT_PAGE.code;
    }

    public static RelatedListEditActionType fromCode(String code) {
        return EnumUtils.fromCode(RelatedListEditActionType.class, code);
    }

    public static RelatedListEditActionType fromName(String name) {
        return EnumUtils.fromName(RelatedListEditActionType.class, name);
    }
}
