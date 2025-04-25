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

package com.flowcentraltech.flowcentral.connect.common.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Lingual date type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public enum LingualDateType {

    TODAY(
            "TDY"),
    YESTERDAY(
            "YST"),
    TOMORROW(
            "TMR"),
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

    private static final Map<String, LingualDateType> typesByCode;
    static {
        Map<String, LingualDateType> map = new HashMap<String, LingualDateType>();
        for (LingualDateType type: LingualDateType.values()) {
            map.put(type.code, type);
        }
        
        typesByCode = Collections.unmodifiableMap(map);
    }

    private LingualDateType(String code) {
        this.code = code;
    }

    public String code() {
        return this.code;
    }

    public static LingualDateType fromCode(String code) {
        return code != null? typesByCode.get(code) : null;
    }

    public static LingualDateType fromName(String name) {
        return name != null? LingualDateType.valueOf(name.toUpperCase()) : null;
    }
}
