/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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
import java.util.Date;
import java.util.List;

import com.flowcentraltech.flowcentral.configuration.constants.ChartCategoryDataType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartSeriesDataType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Chart data.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ChartData {

    private String title;
    
    private String subTitle;

    private int titleOffsetX;

    private int titleFontSize;

    private int subTitleOffsetX;

    private int subTitleFontSize;
    
    private ChartCategories<?> categories;

    private List<ChartSeries<?>> series;

    private ChartData(String title, String subTitle, int titleOffsetX, int titleFontSize, int subTitleOffsetX,
            int subTitleFontSize, ChartCategories<?> categories, List<ChartSeries<?>> series) {
        this.title = title;
        this.subTitle = subTitle;
        this.titleOffsetX = titleOffsetX;
        this.titleFontSize = titleFontSize;
        this.subTitleOffsetX = subTitleOffsetX;
        this.subTitleFontSize = subTitleFontSize;
        this.categories = categories;
        this.series = series;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
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

    public ChartCategories<?> getCategories() {
        return categories;
    }

    public List<ChartSeries<?>> getSeries() {
        return series;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private String title;
        
        private String subTitle;

        private int titleOffsetX;

        private int titleFontSize;

        private int subTitleOffsetX;

        private int subTitleFontSize;

        private ChartCategories<?> categories;

        private List<ChartSeries<?>> series;

        public Builder() {
            series = new ArrayList<ChartSeries<?>>();
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
        
        @SuppressWarnings("unchecked")
        public Builder categories(ChartCategoryDataType type, List<?> categories) throws UnifyException {
            switch (type) {
                case DATE:
                    categories(type,
                            DataUtils.asJsonArrayString(DataUtils.toArray(Date.class, (List<Date>) categories)));
                    break;
                case INTEGER:
                    categories(type,
                            DataUtils.asJsonArrayString(DataUtils.toArray(Integer.class, (List<Integer>) categories)));
                    break;
                case LONG:
                    categories(type,
                            DataUtils.asJsonArrayString(DataUtils.toArray(Long.class, (List<Long>) categories)));
                    break;
                case STRING:
                    categories(type,
                            DataUtils.asJsonArrayString(DataUtils.toArray(Date.class, (List<Date>) categories)));
                    break;
                default:
                    break;
            }
            
            return this;
        }
        
        public Builder categories(ChartCategoryDataType type, String categoriesJson) throws UnifyException {
            switch (type) {
                case DATE:
                    categories = new DateChartCategories(
                            DataUtils.convert(Date[].class, DataUtils.fromJsonString(Long[].class, categoriesJson)));
                    break;
                case INTEGER:
                    categories = new IntegerChartCategories(DataUtils.fromJsonString(Integer[].class, categoriesJson));
                    break;
                case LONG:
                    categories = new LongChartCategories(DataUtils.fromJsonString(Long[].class, categoriesJson));
                    break;
                case STRING:
                    categories = new StringChartCategories(DataUtils.fromJsonString(String[].class, categoriesJson));
                    break;
                default:
                    break;
            }
            return this;
        }

        @SuppressWarnings("unchecked")
        public Builder addSeries(ChartSeriesDataType type, String name, List<?> series) throws UnifyException {
            switch (type) {
                case DOUBLE:
                    addSeries(type, name,
                            DataUtils.asJsonArrayString(DataUtils.toArray(Double.class, (List<Double>) series)));
                    break;
                case INTEGER:
                    addSeries(type, name,
                            DataUtils.asJsonArrayString(DataUtils.toArray(Integer.class, (List<Integer>) series)));
                    break;
                case LONG:
                    addSeries(type, name,
                            DataUtils.asJsonArrayString(DataUtils.toArray(Long.class, (List<Long>) series)));
                    break;
                default:
                    break;

            }
            return this;
        }

        public Builder addSeries(ChartSeriesDataType type, String name, String seriesJson) throws UnifyException {
            switch (type) {
                case DOUBLE:
                    series.add(new DoubleChartSeries(name, DataUtils.fromJsonString(Double[].class, seriesJson)));
                    break;
                case INTEGER:
                    series.add(new IntegerChartSeries(name, DataUtils.fromJsonString(Integer[].class, seriesJson)));
                    break;
                case LONG:
                    series.add(new LongChartSeries(name, DataUtils.fromJsonString(Long[].class, seriesJson)));
                    break;
                default:
                    break;

            }
            return this;
        }

        public ChartData build() throws UnifyException {
            if (categories == null) {
                throw new RuntimeException("Chart categories is required.");
            }

            if (series.size() == 0) {
                throw new RuntimeException("At least one series is required.");
            }

            return new ChartData(title, subTitle, titleOffsetX, titleFontSize, subTitleOffsetX,
                    subTitleFontSize, categories, series);
        }
    }
}
