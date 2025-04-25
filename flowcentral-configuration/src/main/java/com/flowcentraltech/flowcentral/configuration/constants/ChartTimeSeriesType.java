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
import com.tcdng.unify.core.constant.TimeSeriesType;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Chart time series type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_CHARTTIMESERIESTYPE")
@StaticList(name = "charttimeseriestypelist", description = "$m{staticlist.charttimeseriestypelist}")
public enum ChartTimeSeriesType implements EnumConst {

    HOUR("HRD", TimeSeriesType.HOUR, false, false),
    HOUR_OVER_DAY("HRY", TimeSeriesType.HOUR, false, true),
    HOUR_OVER_DAY_MERGED("HRYG", TimeSeriesType.HOUR, true, true),
    DAY("DYD", TimeSeriesType.DAY, false, false),
    DAY_OVER_WEEK("DYW", TimeSeriesType.DAY_OF_WEEK, false, true),
    DAY_OVER_MONTH("DYM", TimeSeriesType.DAY_OF_MONTH, false, true),
    DAY_OVER_YEAR("DYY", TimeSeriesType.DAY_OF_YEAR, false, true),
    DAY_OVER_WEEK_MERGED("DYWG", TimeSeriesType.DAY_OF_WEEK, true, true),
    DAY_OVER_MONTH_MERGED("DYMG", TimeSeriesType.DAY_OF_MONTH, true, true),
    DAY_OVER_YEAR_MERGED("DYYG", TimeSeriesType.DAY_OF_YEAR, true, true),
    WEEK("WKD", TimeSeriesType.WEEK, false, false),
    WEEK_MERGED("WKDG", TimeSeriesType.WEEK, true, true),
    MONTH("MND", TimeSeriesType.MONTH, false, false),
    MONTH_MERGED("MNDG", TimeSeriesType.MONTH, true, true),
    YEAR("YRD", TimeSeriesType.YEAR, false, false),
    YEAR_MERGED("YRDG", TimeSeriesType.YEAR, true, false);

    private final String code;

    private final TimeSeriesType type;

    private final boolean merged;

    private final boolean fill;
    
    private ChartTimeSeriesType(String code, TimeSeriesType type, boolean merged, boolean fill) {
        this.code = code;
        this.type = type;
        this.merged = merged;
        this.fill = fill;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return HOUR.code;
    }

    public TimeSeriesType type() {
        return type;
    }

    public boolean merged() {
        return merged;
    }

    public boolean fill() {
        return fill;
    }
    
    public static ChartTimeSeriesType fromCode(String code) {
        return EnumUtils.fromCode(ChartTimeSeriesType.class, code);
    }

    public static ChartTimeSeriesType fromName(String name) {
        return EnumUtils.fromName(ChartTimeSeriesType.class, name);
    }
}
