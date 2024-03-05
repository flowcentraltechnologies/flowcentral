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
package com.flowcentraltech.flowcentral.chart.data;

import java.util.Collections;
import java.util.Map;

import com.flowcentraltech.flowcentral.configuration.constants.ChartCategoryDataType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartSeriesDataType;

/**
 * Date time integer series.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class DateTimeIntegerSeries extends AbstractDateTimeSeries<Integer>{

    public DateTimeIntegerSeries(Map<Object, String> categoryLabels, String name) {
        super(ChartCategoryDataType.DATE, ChartSeriesDataType.INTEGER, categoryLabels, name);
    }

    public DateTimeIntegerSeries(String name) {
        super(ChartCategoryDataType.DATE, ChartSeriesDataType.INTEGER, Collections.emptyMap(), name);
    }

}
