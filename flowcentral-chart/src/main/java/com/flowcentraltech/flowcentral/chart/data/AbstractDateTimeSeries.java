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

import java.util.Date;
import java.util.Map;

import com.flowcentraltech.flowcentral.configuration.constants.ChartCategoryDataType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartSeriesDataType;
import com.tcdng.unify.core.util.json.JsonWriter;

/**
 * Abstract base class for date time series.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractDateTimeSeries<U extends Number> extends AbstractSeries<Date, U> {

    public AbstractDateTimeSeries(ChartCategoryDataType categoryType, ChartSeriesDataType dataType,
            Map<Object, String> categoryLabels, String name) {
        super(categoryType, dataType, categoryLabels, name);
    }

    @Override
    protected AbstractSeriesData createData(Date x, U y) {
        return new DateTimeSeriesData(x, y);
    }

    private class DateTimeSeriesData extends AbstractSeriesData {

        public DateTimeSeriesData(Date x, U y) {
            super(x, y);
        }

        @Override
        public void writeAsObject(JsonWriter jw) {
            jw.beginObject();
            jw.write("x", getX().getTime());
            jw.write("y", getY());
            jw.endObject();
        }

    }

}
