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

package com.flowcentraltech.flowcentral.application.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Child list edit action type list.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_CHILDLISTEDITACTIONTYPE")
@StaticList(name = "childlisteditactionlist", description = "$m{staticlist.childlisteditactionlist}")
public enum ChildListEditActionType implements EnumConst {

    OPEN_ASSIGNMENT_PAGE(
            "/assignToChildItem"),
    OPEN_TABLEENTRY_PAGE(
            "/entryToChildItem"),
    OPEN_ENTITYCRUD_PAGE(
            "/crudToChildItem");

    private final String code;

    private ChildListEditActionType(String code) {
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

    public static ChildListEditActionType fromCode(String code) {
        return EnumUtils.fromCode(ChildListEditActionType.class, code);
    }

    public static ChildListEditActionType fromName(String name) {
        return EnumUtils.fromName(ChildListEditActionType.class, name);
    }
}
