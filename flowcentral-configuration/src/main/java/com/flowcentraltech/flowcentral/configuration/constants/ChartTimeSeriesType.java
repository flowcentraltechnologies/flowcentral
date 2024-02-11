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

    HOUR("HRD", TimeSeriesType.HOUR),
    HOUR_OVER_DAY("HRY", TimeSeriesType.HOUR),
    DAY("DYD", TimeSeriesType.DAY),
    DAY_OVER_WEEK("DYW", TimeSeriesType.DAY),
    DAY_OVER_MONTH("DYM", TimeSeriesType.DAY),
    DAY_OVER_YEAR("DYY", TimeSeriesType.DAY),
    WEEK("WKD", TimeSeriesType.WEEK),
    WEEK_OVER_MONTH("WKM", TimeSeriesType.WEEK),
    WEEK_OVER_YEAR("WKY", TimeSeriesType.WEEK),
    MONTH("MND", TimeSeriesType.MONTH),
    MONTH_OVER_YEAR("MNY", TimeSeriesType.MONTH),
    YEAR("YRD", TimeSeriesType.YEAR);

    private final String code;

    private final TimeSeriesType type;
    
    private ChartTimeSeriesType(String code, TimeSeriesType type) {
        this.code = code;
        this.type = type;
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
    
    public static ChartTimeSeriesType fromCode(String code) {
        return EnumUtils.fromCode(ChartTimeSeriesType.class, code);
    }

    public static ChartTimeSeriesType fromName(String name) {
        return EnumUtils.fromName(ChartTimeSeriesType.class, name);
    }
}
