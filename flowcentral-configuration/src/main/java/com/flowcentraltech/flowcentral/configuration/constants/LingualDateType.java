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
package com.flowcentraltech.flowcentral.configuration.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.constant.TimeResolutionType;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Lingual date type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_LINGUALDATETYPE")
@StaticList(name = "lingualdatetypelist", description = "$m{staticlist.lingualdatetypelist}")
public enum LingualDateType implements EnumConst {

    NOW(
            "NOW", TimeResolutionType.MINUTE),
    THIS_HOUR(
            "THO", TimeResolutionType.MINUTE),
    LAST_HOUR(
            "LHO", TimeResolutionType.MINUTE),
    NEXT_HOUR(
            "NHO", TimeResolutionType.MINUTE),
    TODAY(
            "TDY", TimeResolutionType.HOUR),
    YESTERDAY(
            "YST", TimeResolutionType.HOUR),
    TOMORROW(
            "TMR", TimeResolutionType.HOUR),
    LAST_15_DAYS(
            "L15", TimeResolutionType.DAY),
    NEXT_15_DAYS(
            "N15", TimeResolutionType.DAY),
    LAST_30_DAYS(
            "L30", TimeResolutionType.DAY),
    NEXT_30_DAYS(
            "N30", TimeResolutionType.DAY),
    LAST_45_DAYS(
            "L45", TimeResolutionType.DAY),
    NEXT_45_DAYS(
            "N45", TimeResolutionType.DAY),
    LAST_60_DAYS(
            "L60", TimeResolutionType.DAY),
    NEXT_60_DAYS(
            "N60", TimeResolutionType.DAY),
    LAST_90_DAYS(
            "L90", TimeResolutionType.DAY),
    NEXT_90_DAYS(
            "N90", TimeResolutionType.DAY),
    THIS_WEEK(
            "TWK", TimeResolutionType.DAY),
    LAST_WEEK(
            "LWK", TimeResolutionType.DAY),
    NEXT_WEEK(
            "NWK", TimeResolutionType.DAY),
    THIS_MONTH(
            "TMN", TimeResolutionType.WEEK),
    LAST_MONTH(
            "LMN", TimeResolutionType.WEEK),
    NEXT_MONTH(
            "NMN", TimeResolutionType.WEEK),
    LAST_3MONTHS(
            "L3M", TimeResolutionType.MONTH),
    NEXT_3MONTHS(
            "N3M", TimeResolutionType.MONTH),
    LAST_6MONTHS(
            "L6M", TimeResolutionType.MONTH),
    NEXT_6MONTHS(
            "N6M", TimeResolutionType.MONTH),
    LAST_9MONTHS(
            "L9M", TimeResolutionType.MONTH),
    NEXT_9MONTHS(
            "N9M", TimeResolutionType.MONTH),
    LAST_12MONTHS(
            "L1M", TimeResolutionType.MONTH),
    NEXT_12MONTHS(
            "N1M", TimeResolutionType.MONTH),
    THIS_YEAR(
            "TYR", TimeResolutionType.MONTH),
    LAST_YEAR(
            "LYR", TimeResolutionType.MONTH),
    NEXT_YEAR(
            "NYR", TimeResolutionType.MONTH);

    private final String code;

    private final TimeResolutionType maxResolution;

    private LingualDateType(String code, TimeResolutionType maxResolution) {
        this.code = code;
        this.maxResolution = maxResolution;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return TODAY.code;
    }

    public TimeResolutionType maxResolution() {
        return maxResolution;
    }

    public boolean isNow() {
        return NOW.equals(this);
    }
    
    public static LingualDateType fromCode(String code) {
        return EnumUtils.fromCode(LingualDateType.class, code);
    }

    public static LingualDateType fromName(String name) {
        return EnumUtils.fromName(LingualDateType.class, name);
    }
}
