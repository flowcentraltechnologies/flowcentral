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
package com.flowcentraltech.flowcentral.configuration.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Lingual date type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_LINGUALDATETYPE")
@StaticList(name = "lingualdatetypelist", description = "$m{staticlist.lingualdatetypelist}")
public enum LingualDateType implements EnumConst {

    NOW(
            "NOW"),
    TODAY(
            "TDY"),
    YESTERDAY(
            "YST"),
    TOMORROW(
            "TMR"),
    LAST_15_DAYS(
            "L15"),
    NEXT_15_DAYS(
            "N15"),
    LAST_30_DAYS(
            "L30"),
    NEXT_30_DAYS(
            "N30"),
    LAST_45_DAYS(
            "L45"),
    NEXT_45_DAYS(
            "N45"),
    LAST_60_DAYS(
            "L60"),
    NEXT_60_DAYS(
            "N60"),
    LAST_90_DAYS(
            "L90"),
    NEXT_90_DAYS(
            "N90"),
    THIS_WEEK(
            "TWK"),
    LAST_WEEK(
            "LWK"),
    NEXT_WEEK(
            "NWK"),
    THIS_MONTH(
            "TMN"),
    LAST_MONTH(
            "LMN"),
    NEXT_MONTH(
            "NMN"),
    LAST_3MONTHS(
            "L3M"),
    NEXT_3MONTHS(
            "N3M"),
    LAST_6MONTHS(
            "L6M"),
    NEXT_6MONTHS(
            "N6M"),
    LAST_9MONTHS(
            "L9M"),
    NEXT_9MONTHS(
            "N9M"),
    LAST_12MONTHS(
            "L1M"),
    NEXT_12MONTHS(
            "N1M"),
    THIS_YEAR(
            "TYR"),
    LAST_YEAR(
            "LYR"),
    NEXT_YEAR(
            "NYR");

    private final String code;

    private LingualDateType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return TODAY.code;
    }

    public static LingualDateType fromCode(String code) {
        return EnumUtils.fromCode(LingualDateType.class, code);
    }

    public static LingualDateType fromName(String name) {
        return EnumUtils.fromName(LingualDateType.class, name);
    }
}
