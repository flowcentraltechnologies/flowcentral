/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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
 * Chart palette type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_CHARTPALETTETYPE")
@StaticList(name = "chartpalettetypelist", description = "$m{staticlist.chartpalettetypelist}")
public enum ChartPaletteType implements EnumConst {

    PALETTE1(
            "PL1",
            "palette1"),
    PALETTE2(
            "PL2",
            "palette2"),
    PALETTE3(
            "PL3",
            "palette3"),
    PALETTE4(
            "PL4",
            "palette4"),
    PALETTE5(
            "PL5",
            "palette5"),
    PALETTE6(
            "PL6",
            "palette6"),
    PALETTE7(
            "PL7",
            "palette7"),
    PALETTE8(
            "PL8",
            "palette8"),
    PALETTE9(
            "PL9",
            "palette9"),
    PALETTE10(
            "PL10",
            "palette10");

    private final String code;

    private final String optionsType;

    private ChartPaletteType(String code, String optionsType) {
        this.code = code;
        this.optionsType = optionsType;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return PALETTE1.code;
    }

    public String optionsType() {
        return optionsType;
    }

    public static ChartPaletteType fromCode(String code) {
        return EnumUtils.fromCode(ChartPaletteType.class, code);
    }

    public static ChartPaletteType fromName(String name) {
        return EnumUtils.fromName(ChartPaletteType.class, name);
    }
}
