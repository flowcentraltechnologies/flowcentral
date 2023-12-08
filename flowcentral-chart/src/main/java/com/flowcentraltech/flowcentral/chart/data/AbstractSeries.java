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

    public List<T> getXList() {
        List<T> list = new ArrayList<T>();
        for (AbstractSeriesData _data: data) {
            list.add(_data.getX());
        }
        
        return list;
    }

    public List<U> getYList() {
        List<U> list = new ArrayList<U>();
        for (AbstractSeriesData _data: data) {
            list.add(_data.getY());
        }
        
        return list;
    }
    
    public void writeAsObject(JsonWriter jw) {
        jw.beginObject();
        jw.write("name", name);
        jw.beginArray("data");
        for (AbstractSeriesData _data: data) {
            _data.writeAsObject(jw);
        }

        jw.endArray();
        jw.endObject();
    }

    public void writeXValuesArray(String field, JsonWriter jw) {
        String[] x = new String[data.size()];
        for (int i = 0; i < x.length; i++) {
            x[i] = String.valueOf(data.get(i).getX());
        }
        
        jw.write(field, x);
    }

    public void writeYValuesArray(String field, JsonWriter jw) {
        Number[] y = new Number[data.size()];
        for (int i = 0; i < y.length; i++) {
            y[i] = data.get(i).getY();
        }
        
        jw.write(field, y);
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

        public abstract void writeAsObject(JsonWriter jw);
        
    }

}
