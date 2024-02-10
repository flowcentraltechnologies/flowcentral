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

import java.util.LinkedHashMap;
import java.util.Map;

import com.flowcentraltech.flowcentral.configuration.constants.ChartCategoryDataType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartSeriesDataType;
import com.tcdng.unify.core.UnifyException;

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

    private Map<String, AbstractSeries<?, ?>> series;

    private ChartDetails(String title, String subTitle, int titleOffsetX, int titleFontSize, int subTitleOffsetX,
            int subTitleFontSize, ChartCategoryDataType categoryType, Map<String, AbstractSeries<?, ?>> series) {
        this.title = title;
        this.subTitle = subTitle;
        this.titleOffsetX = titleOffsetX;
        this.titleFontSize = titleFontSize;
        this.subTitleOffsetX = subTitleOffsetX;
        this.subTitleFontSize = subTitleFontSize;
        this.categoryType = categoryType;
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

    public ChartCategoryDataType getCategoryType() {
        return categoryType;
    }

    public Map<String, AbstractSeries<?, ?>> getSeries() {
        return series;
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

        private Map<String, AbstractSeries<?, ?>> series;

        public Builder(ChartCategoryDataType categoryType) {
            this.categoryType = categoryType;
            this.series = new LinkedHashMap<String, AbstractSeries<?, ?>>();
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
                    categoryType, series);
        }

        private AbstractSeries<?, ?> createSeries(ChartCategoryDataType categoryType, ChartSeriesDataType dataType,
                String name) {
            switch (categoryType) {
                case DATE:
                    switch (dataType) {
                        case DOUBLE:
                            return new DateTimeDoubleSeries(name);
                        case INTEGER:
                        case LONG:
                            return new DateTimeIntegerSeries(name);
                        default:
                            break;
                    }
                    break;
                case INTEGER:
                case LONG:
                    switch (dataType) {
                        case DOUBLE:
                            return new NumericDoubleSeries(name);
                        case INTEGER:
                        case LONG:
                            return new NumericIntegerSeries(name);
                        default:
                            break;
                    }
                    break;
                case STRING:
                    switch (dataType) {
                        case DOUBLE:
                            return new CategoryDoubleSeries(name);
                        case INTEGER:
                        case LONG:
                            return new CategoryIntegerSeries(name);
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
