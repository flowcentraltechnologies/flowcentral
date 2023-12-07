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

import com.flowcentraltech.flowcentral.configuration.constants.ChartCategoryDataType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartSeriesDataType;

/**
 * Abstract base class for category series.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractCategorySeries<U extends Number> extends AbstractSeries<String, U> {

    public AbstractCategorySeries(ChartCategoryDataType categoryType, ChartSeriesDataType dataType, String name) {
        super(categoryType, dataType, name);
    }

    @Override
    protected AbstractSeriesData createData(String x, U y) {
        return new CategorySeriesData(x, y);
    }
    
    private class CategorySeriesData extends AbstractSeriesData {

        public CategorySeriesData(String x, U y) {
            super(x, y);
        }

        @Override
        public void writeAsObject(StringBuilder sb) {
            sb.append("{x:\"").append(getX()).append("\", y:").append(getY()).append("}");
        }

    }

}