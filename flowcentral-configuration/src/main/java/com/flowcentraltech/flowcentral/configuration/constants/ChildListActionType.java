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
 * Child list action type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_CHILDLISTACTIONTYPE")
@StaticList(name = "childlistactiontypelist", description = "$m{staticlist.childlistactiontypelist}")
public enum ChildListActionType implements EnumConst {

    SHOW_MULTISELECT(
            "SMS"),
    SHOW_TREEMULTISELECT(
            "STM"),
    HIDE_ADDWIDGET(
            "HAW");

    private final String code;

    private ChildListActionType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return SHOW_MULTISELECT.code;
    }

    public boolean isShowPopup() {
        return SHOW_MULTISELECT.equals(this) || SHOW_TREEMULTISELECT.equals(this);
    }

    public static ChildListActionType fromCode(String code) {
        return EnumUtils.fromCode(ChildListActionType.class, code);
    }

    public static ChildListActionType fromName(String name) {
        return EnumUtils.fromName(ChildListActionType.class, name);
    }
}
