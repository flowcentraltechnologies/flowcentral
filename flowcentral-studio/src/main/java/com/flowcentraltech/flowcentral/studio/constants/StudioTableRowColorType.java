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

package com.flowcentraltech.flowcentral.studio.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Studio table row color list.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@StaticList(name = "studiotablerowcolorlist", description="Studio Table Row Color List")
public enum StudioTableRowColorType implements EnumConst {

    RED_LIGHT("#f8c9c4"),
    ORANGE_LIGHT("#fde3c0"),
    YELLOW_LIGHT("#fcf3cf"),
    GREEN_LIGHT("#baeae1"),
    CYAN_LIGHT("#baf3f2"),
    BLUE_LIGHT("#c2e0f3"),
    VIOLET_LIGHT("#dcc6e6"),
    GRAY_LIGHT("#c2c8ce"),
    RED_DARK("#f1948a"),
    ORANGE_DARK("#fcc981"),
    YELLOW_DARK("#f9e79f"),
    GREEN_DARK("#76d7c4"),
    CYAN_DARK("#75e9e5"),
    BLUE_DARK("#85c1e9"),
    VIOLET_DARK("#bb8fce"),
    GRAY_DARK("#85929e");

    private final String code;
    
    private StudioTableRowColorType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return RED_LIGHT.code;
    }

    public static StudioTableRowColorType fromCode(String code) {
        return EnumUtils.fromCode(StudioTableRowColorType.class, code);
    }

    public static StudioTableRowColorType fromName(String name) {
        return EnumUtils.fromName(StudioTableRowColorType.class, name);
    }
}
