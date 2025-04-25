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
package com.flowcentraltech.flowcentral.chart.data;

import java.util.Collections;
import java.util.Map;

import com.flowcentraltech.flowcentral.configuration.constants.ChartCategoryDataType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartSeriesDataType;

/**
 * Numeric integer series.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class NumericIntegerSeries extends AbstractNumericSeries<Integer, Integer> {

    public NumericIntegerSeries(Map<Object, String> categoryLabels, String name) {
        super(ChartCategoryDataType.INTEGER, ChartSeriesDataType.INTEGER, categoryLabels, name);
    }

    public NumericIntegerSeries(String name) {
        super(ChartCategoryDataType.INTEGER, ChartSeriesDataType.INTEGER, Collections.emptyMap(), name);
    }

}
