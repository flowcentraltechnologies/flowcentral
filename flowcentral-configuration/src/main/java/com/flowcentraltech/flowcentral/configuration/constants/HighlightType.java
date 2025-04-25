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
 * Highlight type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_HIGHLIGHTTYPE")
@StaticList(name = "highlighttypelist", description = "$m{staticlist.highlighttypelist}")
public enum HighlightType implements EnumConst {

    BLUE(
            "BLU",
            "fc-bluebutton"),
    GREEN(
            "GRN",
            "fc-greenbutton"),
    ORANGE(
            "ORG",
            "fc-orangebutton"),
    RED(
            "RED",
            "fc-redbutton"),
    GRAY(
            "GRA",
            "fc-graybutton");

    private final String code;

    private final String buttonClass;

    private HighlightType(String code, String buttonClass) {
        this.code = code;
        this.buttonClass = buttonClass;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return BLUE.code;
    }

    public String buttonClass() {
        return buttonClass;
    }

    public static HighlightType fromCode(String code) {
        return EnumUtils.fromCode(HighlightType.class, code);
    }

    public static HighlightType fromName(String name) {
        return EnumUtils.fromName(HighlightType.class, name);
    }
}
