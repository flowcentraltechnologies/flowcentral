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

    private List<AbstractSeries<?, ?>> series;

    private ChartDetails(String title, String subTitle, int titleOffsetX, int titleFontSize, int subTitleOffsetX,
            int subTitleFontSize, List<AbstractSeries<?, ?>> series) {
        this.title = title;
        this.subTitle = subTitle;
        this.titleOffsetX = titleOffsetX;
        this.titleFontSize = titleFontSize;
        this.subTitleOffsetX = subTitleOffsetX;
        this.subTitleFontSize = subTitleFontSize;
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

    public List<AbstractSeries<?, ?>> getSeries() {
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

        private List<AbstractSeries<?, ?>> series;

        public Builder() {
            series = new ArrayList<AbstractSeries<?, ?>>();
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

        public Builder addSeries(AbstractSeries<?, ?> series) throws UnifyException {
            this.series.add(series);
            return this;
        }

        public ChartDetails build() throws UnifyException {
            return new ChartDetails(title, subTitle, titleOffsetX, titleFontSize, subTitleOffsetX, subTitleFontSize,
                    series);
        }
    }
}
