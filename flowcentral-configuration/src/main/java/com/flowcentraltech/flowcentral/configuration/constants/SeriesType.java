/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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
import com.tcdng.unify.core.criterion.AggregateFunction;
import com.tcdng.unify.core.criterion.AggregateType;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Series type enumeration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_SERIESTYPE")
@StaticList(name = "seriestypelist", description = "$m{staticlist.seriestypelist}")
public enum SeriesType implements EnumConst {

    COUNT(
            "COUNT", AggregateType.COUNT),
    SUM(
            "SUM", AggregateType.SUM),
    AVERAGE(
            "AVG", AggregateType.AVERAGE),
    MINIMUM(
            "MIN", AggregateType.MINIMUM),
    MAXIMUM(
            "MAX", AggregateType.MAXIMUM);

    private final String code;
    
    private final AggregateType aggregateType;

    private SeriesType(String code, AggregateType aggregateType) {
        this.code = code;
        this.aggregateType = aggregateType;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return COUNT.code;
    }

    public AggregateType aggregateType() {
        return aggregateType;
    }

    public AggregateFunction function(String fieldName) {
        return aggregateType.function(fieldName);
    }
    
    public static SeriesType fromCode(String code) {
        return EnumUtils.fromCode(SeriesType.class, code);
    }

    public static SeriesType fromName(String name) {
        return EnumUtils.fromName(SeriesType.class, name);
    }

}
