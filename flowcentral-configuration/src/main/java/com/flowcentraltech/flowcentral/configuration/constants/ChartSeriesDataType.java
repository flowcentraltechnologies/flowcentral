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
 * Chart series data type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_CHARTSERDATATYPE")
@StaticList(name = "chartseriesdatatypelist", description = "$m{staticlist.chartseriesdatatypelist}")
public enum ChartSeriesDataType implements EnumConst {

    INTEGER(
            "INT",
            Integer.class,
            Integer.valueOf(0)),
    LONG(
            "LGN",
            Long.class,
            Long.valueOf(0)),
    DOUBLE(
            "DBL",
            Double.class,
            Double.valueOf(0));

    private final String code;

    private final Class<? extends Number> dataType;

    private final Number zero;

    private ChartSeriesDataType(String code, Class<? extends Number> dataType,
            Number zero) {
        this.code = code;
        this.dataType = dataType;
        this.zero = zero;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return INTEGER.code;
    }

    public Class<? extends Number> dataType() {
        return dataType;
    }

    public boolean isInteger() {
        return INTEGER.equals(this) || LONG.equals(this);
    }
    
    public Number zero() {
        return zero;
    }

    public Number add(Number a, Number b) {
        if (a != null) {
            if (b != null) {
                switch (this) {
                    case DOUBLE:
                        return Double.valueOf(a.doubleValue() + b.doubleValue());
                    case INTEGER:
                        return Integer.valueOf(a.intValue() + b.intValue());
                    case LONG:
                        return Long.valueOf(a.longValue() + b.longValue());
                    default:
                        break;
                }
            }

            return a;
        }

        return b;
    }

    public static ChartSeriesDataType fromCode(String code) {
        return EnumUtils.fromCode(ChartSeriesDataType.class, code);
    }

    public static ChartSeriesDataType fromName(String name) {
        return EnumUtils.fromName(ChartSeriesDataType.class, name);
    }
}
