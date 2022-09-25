/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import com.tcdng.unify.convert.constants.EnumConst;
import com.tcdng.unify.core.annotation.StaticList;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Studio table row color list.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@StaticList(name = "studiotablerowcolorlist", description="Studio Table Row Color List")
public enum StudioTableRowColorType implements EnumConst {

    RED("#f1948a"),
    YELLOW("#f7dc6f"),
    GREEN("#76d7c4"),
    BLUE("#85c1e9"),
    VIOLET("#bb8fce"),
    GRAY("#85929e");

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
        return RED.code;
    }

    public static StudioTableRowColorType fromCode(String code) {
        return EnumUtils.fromCode(StudioTableRowColorType.class, code);
    }

    public static StudioTableRowColorType fromName(String name) {
        return EnumUtils.fromName(StudioTableRowColorType.class, name);
    }
}
