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

import java.util.List;

import com.flowcentraltech.flowcentral.configuration.constants.ChartSeriesDataType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Chart series definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class ChartSeries<T extends Number> {

    private String name;

    private List<T> data;

    protected ChartSeries(String name, List<T> data) {
        this.name = name;
        this.data = DataUtils.unmodifiableList(data);
    }

    protected ChartSeries(String name, T[] data) {
        this.name = name;
        this.data = DataUtils.unmodifiableList(data);
    }

    public abstract ChartSeriesDataType getSeriesType();

    public abstract Class<T> getDataType();

    public String getName() {
        return name;
    }

    public List<T> getData() {
        return data;
    }

    public T[] dataToArray() {
        return DataUtils.toArray(getDataType(), data);
    }

    public String dataToJsonArrayString() throws UnifyException {
        return DataUtils.asJsonArrayString(dataToArray());
    }

    public int size() {
        return data.size();
    }
}
