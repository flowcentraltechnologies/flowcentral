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

package com.flowcentraltech.flowcentral.studio.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Studio chart color list.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@StaticList(name = "studiochartcolorlist", description="Studio Chart Color List")
public enum StudioChartColorType implements EnumConst {

    RED("#72170e"),
    ORANGE("#e65c00"),
    YELLOW("#f1c927"),
    GREEN("#1d6355"),
    CYAN("#199a95"),
    BLUE("#13496c"),
    VIOLET("#4a2759"),
    GRAY("#22262b");

    private final String code;
    
    private StudioChartColorType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return RED.code;
    }

    public static StudioChartColorType fromCode(String code) {
        return EnumUtils.fromCode(StudioChartColorType.class, code);
    }

    public static StudioChartColorType fromName(String name) {
        return EnumUtils.fromName(StudioChartColorType.class, name);
    }
}
