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
 * Widget color constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_WIDGETCOLOR")
@StaticList(name = "widgetcolorlist", description = "$m{staticlist.widgetcolorlist}")
public enum WidgetColor implements EnumConst {

    WHITE(
            "WHT",
            "#ffffff"),
    RED(
            "RED",
            "#eacbc8"),
    ORANGE(
            "ORN",
            "#ead5c3"),
    YELLOW(
            "YLW",
            "#efefc1"),
    GREEN(
            "GRN",
            "#c5e5d3"),
    CYAN(
            "CYN",
            "#c0dcd7"),
    BLUE(
            "BLU",
            "#c6dae8"),
    PURPLE(
            "PPL",
            "#dbcee0"),
    NAVY_GRAY(
            "NGR",
            "#d6dbdf");

    private final String code;

    private final String hex;

    private WidgetColor(String code, String hex) {
        this.code = code;
        this.hex = hex;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return BLUE.code;
    }

    public String hex() {
        return hex;
    }

    public static WidgetColor fromCode(String code) {
        return EnumUtils.fromCode(WidgetColor.class, code);
    }

    public static WidgetColor fromName(String name) {
        return EnumUtils.fromName(WidgetColor.class, name);
    }
}
