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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private final Map<Object, String> categoryLabels;

    private final String name;

    private final Map<T, AbstractSeriesData> data;

    private List<AbstractSeriesData> dataList;

    private Set<String> categoryInclusion;

    protected AbstractSeries(ChartCategoryDataType categoryType, ChartSeriesDataType dataType,
            Map<Object, String> categoryLabels, String name) {
        this.categoryType = categoryType;
        this.dataType = dataType;
        this.categoryLabels = categoryLabels;
        this.name = name;
        this.data = new LinkedHashMap<T, AbstractSeriesData>();
        this.categoryInclusion = Collections.emptySet();
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

    public void setCategoryInclusion(Set<String> categoryInclusion) {
        if (categoryInclusion == null) {
            throw new IllegalArgumentException("Argument can not be null.");
        }

        this.categoryInclusion = categoryInclusion;
        dataList = null;
    }

    @SuppressWarnings("unchecked")
    public void addData(Object x, Number y) {
        data.put((T) x, createData((T) x, (U) y));
    }

    public U getData(Object x) {
        AbstractSeriesData seriesData = data.get(x);
        if (seriesData != null) {
            return seriesData.getY();
        }

        return null;
    }

    public int size() {
        return data.size();
    }

    public void clear() {
        data.clear();
    }

    public List<Object> getXList() {
        List<Object> list = new ArrayList<Object>();
        for (AbstractSeriesData _data : data.values()) {
            list.add(resolveX(_data.getX()));
        }

        return list;
    }

    public List<Number> getYList() {
        List<Number> list = new ArrayList<Number>();
        for (AbstractSeriesData _data : data.values()) {
            list.add(_data.getY());
        }

        return list;
    }

    public void writeAsObject(JsonWriter jw) {
        final boolean inclusion = !categoryInclusion.isEmpty();
        jw.beginObject();
        jw.write("name", name);
        jw.beginArray("data");
        for (AbstractSeriesData _data : data.values()) {
            if (inclusion && !categoryInclusion.contains(_data.getX())) {
                continue;
            }

            _data.writeAsObject(jw);
        }

        jw.endArray();
        jw.endObject();
    }

    public void writeXValuesArray(String field, JsonWriter jw) {
        List<AbstractSeriesData> _data = getDataList();
        String[] x = new String[_data.size()];
        for (int i = 0; i < x.length; i++) {
            x[i] = resolveX(_data.get(i).getX());
        }

        jw.write(field, x);
    }

    public void writeYValuesArray(String field, JsonWriter jw) {
        List<AbstractSeriesData> _data = getDataList();
        Number[] y = new Number[_data.size()];
        for (int i = 0; i < y.length; i++) {
            y[i] = _data.get(i).getY();
        }

        jw.write(field, y);
    }

    private String resolveX(Object xobj) {
        return categoryLabels.containsKey(xobj) ? categoryLabels.get(xobj) : String.valueOf(xobj);
    }

    private List<AbstractSeriesData> getDataList() {
        if (dataList == null) {
            synchronized (this) {
                if (dataList == null) {
                    if (!categoryInclusion.isEmpty()) {
                        dataList = new ArrayList<AbstractSeriesData>();
                        for (AbstractSeriesData _data : data.values()) {
                            if (!categoryInclusion.contains(_data.getX())) {
                                continue;
                            }

                            dataList.add(_data);
                        }
                    } else {
                        dataList = new ArrayList<AbstractSeriesData>(data.values());
                    }
                }
            }
        }

        return dataList;
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

        @Override
        public String toString() {
            return "AbstractSeriesData [x=" + x + ", y=" + y + "]";
        }

        protected String resolveX(Object xobj) {
            return AbstractSeries.this.resolveX(xobj);
        }

        public abstract void writeAsObject(JsonWriter jw);

    }

}
