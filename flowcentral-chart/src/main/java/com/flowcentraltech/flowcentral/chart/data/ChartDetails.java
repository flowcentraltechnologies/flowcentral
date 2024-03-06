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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.configuration.constants.ChartCategoryDataType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartSeriesDataType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Chart details.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ChartDetails {

    private String title;

    private String subTitle;

    private int titleOffsetX;

    private int titleFontSize;

    private int subTitleOffsetX;

    private int subTitleFontSize;

    private ChartCategoryDataType categoryType;

    private Map<Object, String> categoryLabels;

    private Map<String, AbstractSeries<?, ?>> series;

    private ChartTableColumn[] headers;

    private List<Object[]> tableSeries;

    private ChartDetails(String title, String subTitle, int titleOffsetX, int titleFontSize, int subTitleOffsetX,
            int subTitleFontSize, ChartCategoryDataType categoryType, Map<Object, String> categoryLabels,
            Map<String, AbstractSeries<?, ?>> series, ChartTableColumn[] headers, List<Object[]> tableSeries) {
        this.title = title;
        this.subTitle = subTitle;
        this.titleOffsetX = titleOffsetX;
        this.titleFontSize = titleFontSize;
        this.subTitleOffsetX = subTitleOffsetX;
        this.subTitleFontSize = subTitleFontSize;
        this.categoryType = categoryType;
        this.categoryLabels = categoryLabels;
        this.series = series;
        this.headers = headers;
        this.tableSeries = tableSeries;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public boolean isWithTitle() {
        return !StringUtils.isBlank(title);
    }

    public boolean isWithSubtitle() {
        return !StringUtils.isBlank(subTitle);
    }

    public int getTitleOffsetX() {
        return titleOffsetX;
    }

    public int getTitleFontSize() {
        return titleFontSize;
    }

    public int getSubTitleOffsetX() {
        return subTitleOffsetX;
    }

    public int getSubTitleFontSize() {
        return subTitleFontSize;
    }

    public ChartCategoryDataType getCategoryType() {
        return categoryType;
    }

    public String getCategoryLabel(String name) {
        return categoryLabels.get(name);
    }

    public Map<String, AbstractSeries<?, ?>> getSeries() {
        return series;
    }

    public Map<String, AbstractSeries<?, ?>> getSeries(Set<String> inclusion) {
        if (!inclusion.isEmpty()) {
            Map<String, AbstractSeries<?, ?>> _series = new HashMap<String, AbstractSeries<?, ?>>();
            for (Map.Entry<String, AbstractSeries<?, ?>> entry : series.entrySet()) {
                if (inclusion.contains(entry.getKey())) {
                    _series.put(entry.getKey(), entry.getValue());
                }
            }
            
            return _series;
        }
        
        return series;
    }

    public ChartTableColumn[] getTableHeaders() {
        return headers;
    }

    public List<Object[]> getTableSeries() {
        return tableSeries;
    }

    public boolean isWithTableSeries() {
        return headers != null;
    }

    @Override
    public String toString() {
        return "ChartDetails [title=" + title + ", subTitle=" + subTitle + ", titleOffsetX=" + titleOffsetX
                + ", titleFontSize=" + titleFontSize + ", subTitleOffsetX=" + subTitleOffsetX + ", subTitleFontSize="
                + subTitleFontSize + ", categoryType=" + categoryType + ", categoryLabels=" + categoryLabels
                + ", series=" + series + ", headers=" + Arrays.toString(headers) + ", tableSeries=" + tableSeries + "]";
    }

    public static Builder newBuilder(ChartCategoryDataType categoryType) {
        return new Builder(categoryType);
    }

    public static class Builder {

        private String title;

        private String subTitle;

        private int titleOffsetX;

        private int titleFontSize;

        private int subTitleOffsetX;

        private int subTitleFontSize;

        private ChartCategoryDataType categoryType;

        private Map<Object, String> categoryLabels;

        private Map<String, AbstractSeries<?, ?>> series;

        private ChartTableColumn[] headers;

        private List<Object[]> tableSeries;

        public Builder(ChartCategoryDataType categoryType) {
            this.categoryType = categoryType;
            this.series = new LinkedHashMap<String, AbstractSeries<?, ?>>();
            this.categoryLabels = new HashMap<Object, String>();
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder titleOffsetX(int titleOffsetX) {
            this.titleOffsetX = titleOffsetX;
            return this;
        }

        public Builder titleFontSize(int titleFontSize) {
            this.titleFontSize = titleFontSize;
            return this;
        }

        public Builder subTitle(String subTitle) {
            this.subTitle = subTitle;
            return this;
        }

        public Builder subTitleOffsetX(int subTitleOffsetX) {
            this.subTitleOffsetX = subTitleOffsetX;
            return this;
        }

        public Builder subTitleFontSize(int subTitleFontSize) {
            this.subTitleFontSize = subTitleFontSize;
            return this;
        }

        public ChartCategoryDataType getCategoryType() {
            return categoryType;
        }

        public Map<String, AbstractSeries<?, ?>> getSeries() {
            return series;
        }

        public AbstractSeries<?, ?> getSeries(String seriesName) {
            return series.get(seriesName);
        }

        public boolean isWithSeries(String seriesName) {
            return series.containsKey(seriesName);
        }

        public boolean isWithTableSeries() {
            return this.headers != null;
        }

        public Builder setCategoryLabel(Object name, String label) {
            categoryLabels.put(name, label);
            return this;
        }

        public Builder createTableSeries(ChartTableColumn[] headers) {
            if (isWithTableSeries()) {
                throw new RuntimeException("Table series already defined for this builder.");
            }

            this.headers = headers;
            this.tableSeries = new ArrayList<Object[]>();
            return this;
        }

        public Builder addTableSeries(Object[] tableRow) {
            if (tableSeries == null) {
                throw new RuntimeException("No table series defined for this builder.");
            }

            if (tableRow.length != headers.length) {
                throw new RuntimeException("Series length does not match table header length.");
            }

            tableSeries.add(tableRow);
            return this;
        }

        public Builder createSeries(ChartSeriesDataType dataType, String seriesName) throws UnifyException {
            if (series.containsKey(seriesName)) {
                throw new RuntimeException("Series with name [" + seriesName + "] already exists with this builder.");
            }

            AbstractSeries<?, ?> _series = createSeries(categoryType, dataType, seriesName);
            series.put(seriesName, _series);
            return this;
        }

        public Builder addSeriesData(String seriesName, Object x, Number y) throws UnifyException {
            AbstractSeries<?, ?> _series = series.get(seriesName);
            if (_series == null) {
                throw new RuntimeException("Series with name [" + seriesName + "] is unknown.");
            }

            _series.addData(x, y);
            return this;
        }

        public ChartDetails build() throws UnifyException {
            return new ChartDetails(title, subTitle, titleOffsetX, titleFontSize, subTitleOffsetX, subTitleFontSize,
                    categoryType, DataUtils.unmodifiableMap(categoryLabels), series, headers, tableSeries);
        }

        private AbstractSeries<?, ?> createSeries(ChartCategoryDataType categoryType, ChartSeriesDataType dataType,
                String name) {
            switch (categoryType) {
                case DATE:
                    switch (dataType) {
                        case DOUBLE:
                            return new DateTimeDoubleSeries(categoryLabels, name);
                        case INTEGER:
                        case LONG:
                            return new DateTimeIntegerSeries(categoryLabels, name);
                        default:
                            break;
                    }
                    break;
                case INTEGER:
                case LONG:
                    switch (dataType) {
                        case DOUBLE:
                            return new NumericDoubleSeries(categoryLabels, name);
                        case INTEGER:
                        case LONG:
                            return new NumericIntegerSeries(categoryLabels, name);
                        default:
                            break;
                    }
                    break;
                case STRING:
                    switch (dataType) {
                        case DOUBLE:
                            return new CategoryDoubleSeries(categoryLabels, name);
                        case INTEGER:
                        case LONG:
                            return new CategoryIntegerSeries(categoryLabels, name);
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }

            return null;
        }

    }
}
