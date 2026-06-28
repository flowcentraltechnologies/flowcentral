/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
 * Cash refresh rate enumeration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_CACHEREFRESHRATE")
@StaticList(name = "cacherefreshratelist", description = "$m{staticlist.cacherefreshratelist}")
public enum CacheRefreshRate implements EnumConst {

    SECOND_30("S30", 30),
    MINUTE_1("M01", 60),
    MINUTE_2("M02", 1 * 60),
    MINUTE_5("M05", 5 * 60),
    MINUTE_10("M10", 10 * 60),
    MINUTE_15("M15", 15 * 60),
    MINUTE_20("M20", 20 * 60),
    MINUTE_30("M30", 30 * 60),
    HOUR_1("H01", 60 * 60),
    HOUR_2("H02", 2 * 60 * 60),
    HOUR_4("H04", 4 * 60 * 60),
    HOUR_6("H06", 6 * 60 * 60),
    HOUR_12("H12", 12 * 60 * 60);

    private final String code;

    private final long seconds;

    private CacheRefreshRate(String code, long seconds) {
        this.code = code;
        this.seconds = seconds;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return MINUTE_1.code;
    }

    public long seconds() {
        return seconds;
    }

    public static CacheRefreshRate fromCode(String code) {
        return EnumUtils.fromCode(CacheRefreshRate.class, code);
    }

    public static CacheRefreshRate fromName(String name) {
        return EnumUtils.fromName(CacheRefreshRate.class, name);
    }

}
