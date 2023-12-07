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
package com.flowcentraltech.flowcentral.chart.data;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.configuration.constants.ChartCategoryDataType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartSeriesDataType;
import com.tcdng.unify.core.util.json.JsonWriter;

/**
 * Abstract base class for series.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractSeries<T, U extends Number> {

    private final ChartCategoryDataType categoryType;

    private final ChartSeriesDataType dataType;

    private final String name;

    private final List<AbstractSeriesData> data;

    protected AbstractSeries(ChartCategoryDataType categoryType, ChartSeriesDataType dataType, String name) {
        this.categoryType = categoryType;
        this.dataType = dataType;
        this.name = name;
        this.data = new ArrayList<AbstractSeriesData>();
    }

    public ChartCategoryDataType getCategoryType() {
        return categoryType;
    }

    public ChartSeriesDataType getDataType() {
        return dataType;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unchecked")
    public void addData(Object x, Number y) {
        data.add(createData((T) x, (U) y));
    }

    public int size() {
        return data.size();
    }

    public void clear() {
        data.clear();
    }

    public void writeAsObject(JsonWriter jw) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean sym = false;
        for (AbstractSeriesData _data : data) {
            if (sym) {
                sb.append(",");
            } else {
                sym = true;
            }

            _data.writeAsObject(sb);
        }
        sb.append("]");

        jw.beginObject();
        jw.write("name", name);
        jw.write("data", sb.toString());
        jw.endObject();
    }

    protected abstract AbstractSeriesData createData(T x, U y);

    protected abstract class AbstractSeriesData {

        private final T x;

        private final U y;

        public AbstractSeriesData(T x, U y) {
            this.x = x;
            this.y = y;
        }

        public T getX() {
            return x;
        }

        public U getY() {
            return y;
        }

        public abstract void writeAsObject(StringBuilder sb);
    }

}
