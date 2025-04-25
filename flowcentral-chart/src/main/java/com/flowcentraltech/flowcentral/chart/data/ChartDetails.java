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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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

    private boolean dynamicCategories;

    private ChartCategoryDataType categoryType;

    private Map<Object, String> categoryLabels;

    private Map<String, AbstractSeries<?, ?>> series;

    private ChartTableColumn[] headers;

    private List<Object[]> tableSeries;

    private Set<String> seriesInclusion;

    private Set<String> seriesFieldInclusion;

    private Set<String> categoryInclusion;

    private Map<String, AbstractSeries<?, ?>> groupingSeries;

    private ChartDetails(String title, String subTitle, int titleOffsetX, int titleFontSize, int subTitleOffsetX,
            int subTitleFontSize, boolean dynamicCategories, ChartCategoryDataType categoryType, Map<Object, String> categoryLabels,
            Map<String, AbstractSeries<?, ?>> series, ChartTableColumn[] headers, List<Object[]> tableSeries,
            Set<String> seriesInclusion, Set<String> categoryInclusion) {
        this.title = title;
        this.subTitle = subTitle;
        this.titleOffsetX = titleOffsetX;
        this.titleFontSize = titleFontSize;
        this.subTitleOffsetX = subTitleOffsetX;
        this.subTitleFontSize = subTitleFontSize;
        this.dynamicCategories = dynamicCategories;
        this.categoryType = categoryType;
        this.categoryLabels = categoryLabels;
        this.series = series;
        this.headers = headers;
        this.tableSeries = tableSeries;
        this.seriesInclusion = seriesInclusion;
        this.seriesFieldInclusion = Collections.emptySet();
        this.categoryInclusion = categoryInclusion;
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

    public boolean isDynamicCategories() {
        return dynamicCategories;
    }

    public ChartCategoryDataType getCategoryType() {
        return categoryType;
    }

    public String getCategoryLabel(String name) {
        return categoryLabels.get(name);
    }

    public void setSeriesFieldInclusion(Set<String> seriesFieldInclusion) {
        this.seriesFieldInclusion = seriesFieldInclusion;
    }

    public boolean isSeriesFieldInclusion(String fieldName) {
        return seriesFieldInclusion == null || seriesFieldInclusion.isEmpty()
                || seriesFieldInclusion.contains(fieldName);
    }
    
    public Map<String, AbstractSeries<?, ?>> getSeries() {
        return series;
    }

    public Set<String> getSeriesInclusion() {
        return seriesInclusion;
    }

    public boolean isWithSeriesInclusion() {
        return !DataUtils.isBlank(seriesInclusion);
    }

    public Set<String> getCategoryInclusion() {
        return categoryInclusion;
    }

    public boolean isWithCategoryInclusion() {
        return !DataUtils.isBlank(categoryInclusion);
    }

    public Map<String, AbstractSeries<?, ?>> getSeries(Set<String> inclusion) {
        if (!inclusion.isEmpty()) {
            Map<String, AbstractSeries<?, ?>> _series = new LinkedHashMap<String, AbstractSeries<?, ?>>();
            for (Map.Entry<String, AbstractSeries<?, ?>> entry : series.entrySet()) {
                if (inclusion.contains(entry.getKey())) {
                    _series.put(entry.getKey(), entry.getValue());
                }
            }

            return _series;
        }

        return series;
    }

    public Map<String, AbstractSeries<?, ?>> getGroupingSeries(ChartDef chartDef) {
        if (groupingSeries == null) {
            synchronized (this) {
                if (groupingSeries == null) {
                    groupingSeries = new LinkedHashMap<String, AbstractSeries<?, ?>>();
                    if (!DataUtils.isBlank(tableSeries)) {
                        for (Object[] row : tableSeries) {
                            StringBuilder sb = new StringBuilder();
                            final int len = headers.length;
                            int i = 0;
                            for (; i < len; i++) {
                                ChartTableColumn col = headers[i];
                                if (col.isCategory()) {
                                    if (i > 0) {
                                        sb.append(' ');
                                    }

                                    sb.append(row[i]);
                                } else {
                                    break;
                                }
                            }

                            String cat = sb.toString();
                            for (; i < len; i++) {
                                ChartTableColumn col = headers[i];
                                String name = col.getFieldName();
                                if (chartDef.isSeriesInclusion(name)) {
                                    AbstractSeries<?, ?> _series = groupingSeries.get(name);
                                    if (_series == null) {
                                        _series = col.getType().isInteger() ? new CategoryIntegerSeries(name)
                                                : new CategoryDoubleSeries(name);
                                        groupingSeries.put(name, _series);
                                    }

                                    _series.addData(cat, (Number) row[i]);
                                }
                            }

                        }
                    }

                    groupingSeries = DataUtils.unmodifiableMap(groupingSeries);
                }
            }
        }

        return groupingSeries;
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

        private boolean dynamicCategories;

        private ChartCategoryDataType categoryType;

        private Map<Object, String> categoryLabels;

        private Map<String, AbstractSeries<?, ?>> series;

        private ChartTableColumn[] headers;

        private List<Object[]> tableSeries;

        private Set<String> seriesInclusion;

        private Set<String> categoryInclusion;

        public Builder(ChartCategoryDataType categoryType) {
            this.categoryType = categoryType;
            this.series = new LinkedHashMap<String, AbstractSeries<?, ?>>();
            this.categoryLabels = new HashMap<Object, String>();
            this.seriesInclusion = new HashSet<String>();
            this.categoryInclusion = new HashSet<String>();
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

        public Builder dynamicCategories(boolean dynamicCategories) {
            this.dynamicCategories = dynamicCategories;
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

        public Builder addSeriesInclusion(String series) {
            seriesInclusion.add(series);
            return this;
        }

        public Builder addCategoryInclusion(String category) {
            categoryInclusion.add(category);
            return this;
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
                    dynamicCategories, categoryType, DataUtils.unmodifiableMap(categoryLabels), series, headers, tableSeries,
                    Collections.unmodifiableSet(seriesInclusion), Collections.unmodifiableSet(categoryInclusion));
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
